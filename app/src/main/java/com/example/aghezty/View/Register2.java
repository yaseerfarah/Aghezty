package com.example.aghezty.View;


import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.aghezty.POJO.CityInfo;
import com.example.aghezty.POJO.GovernorateInfo;
import com.example.aghezty.POJO.UserInfo;
import com.example.aghezty.R;
import com.example.aghezty.ViewModel.UserViewModel;
import com.example.aghezty.ViewModel.ViewModelFactory;
import com.gturedi.views.StatefulLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;
import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class Register2 extends Fragment {

    @Inject
    ViewModelFactory viewModelFactory;
    private UserViewModel userViewModel;

    private Observer currentUserInfoObserver;
    private Observer governorateobserver;
    private Observer citiesobserver;

    private UserInfo userInfo;

    private List<GovernorateInfo> governorateInfoList=new ArrayList<>();
    private List<CityInfo> cityInfoList=new ArrayList<>();

    private CityInfo cityId;

    private NavController navController;

    @BindView(R.id.city_edit)
    Spinner city;
    @BindView(R.id.governorate_edit)
    Spinner governorate;
    @BindView(R.id.address_edit)
    EditText address;
    @BindView(R.id.password_edit)
    EditText password;
    @BindView(R.id.register)
    Button signUp;
    @BindView(R.id.sign_in)
    LinearLayout signIn;

    @BindView(R.id.stateful)
    StatefulLayout statefulLayout;



    @BindView(R.id.city_info)
    LinearLayout cityInfo;




    public Register2() {
        // Required empty public constructor

        currentUserInfoObserver=new Observer<UserInfo>() {
            @Override
            public void onChanged(UserInfo userInfo) {

                Toasty.success(getContext(),"Success Sign Up").show();
                getActivity().finish();

            }
        };

        governorateobserver=new Observer<List<GovernorateInfo>>() {
            @Override
            public void onChanged(List<GovernorateInfo> governorateInfos) {
               statefulLayout.showContent();
                governorateInfoList.clear();
                governorateInfoList.addAll(governorateInfos);
                List<String> items=new ArrayList<>();

                for (GovernorateInfo governorateInfo:governorateInfos){
                    items.add(governorateInfo.getGovernorate_en());
                }

                ArrayAdapter arrayAdapter=new ArrayAdapter(getContext(),R.layout.text_spinner_layout,items);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                governorate.setAdapter(arrayAdapter);
                if (userInfo.getGovernorate()!=null){
                    governorate.setSelection(userInfo.getGovernoratePosition());
                }


            }
        };

        citiesobserver=new Observer<List<CityInfo>>() {
            @Override
            public void onChanged(List<CityInfo> cityInfos) {
                cityInfo.setVisibility(View.VISIBLE);

                cityInfoList.clear();
                cityInfoList.addAll(cityInfos);
                List<String> items=new ArrayList<>();

                for (CityInfo cityInfo:cityInfos){
                    items.add(cityInfo.getCity_en());
                }

                ArrayAdapter arrayAdapter=new ArrayAdapter(getContext(),R.layout.text_spinner_layout,items);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                city.setAdapter(arrayAdapter);

                if (userInfo.getCity()!=null)
                city.setSelection(getCityPosition(cityInfos,userInfo.getCity()));

            }
        };

    }


    @Override
    public void onStart() {
        super.onStart();
        userViewModel.getAllGovernorate();
        userViewModel.getGovernorateListMediatorLiveData().observe(this,governorateobserver);
        userViewModel.getCitiesListMediatorLiveData().observe(this,citiesobserver);
        userViewModel.getCurrentUserInfoLiveData().observe(this,currentUserInfoObserver);

    }


    @Override
    public void onStop() {
        super.onStop();
        userViewModel.getGovernorateListMediatorLiveData().removeObservers(this);
        userViewModel.getCitiesListMediatorLiveData().removeObservers(this);
        userViewModel.getCurrentUserInfoLiveData().removeObservers(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSupportInjection.inject(this);
        userViewModel= ViewModelProviders.of(this,viewModelFactory).get(UserViewModel.class);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register2, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        navController= Navigation.findNavController(view);

        userInfo=userViewModel.getCurrentUserInfo();
        assignFields();
        //progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getContext(), R.color.orange), PorterDuff.Mode.SRC_IN);

        statefulLayout.showLoading(" ");


        governorate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userInfo.setGovernorate(governorateInfoList.get(position).getGovernorate_en());
                userInfo.setGovernoratePosition(position);
                userViewModel.getCitiesByGovernorateId(governorateInfoList.get(position).getId());
                cityInfo.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userInfo.setCity(cityInfoList.get(position).getCity_en());
                cityId=cityInfoList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        signUp.setOnClickListener(v -> {
            validationFields();
        });


        signIn.setOnClickListener(v -> {
            navController.navigate(R.id.action_register2_to_login);
        });

    }



    private void validationFields(){

        if (!address.getText().toString().isEmpty()&&!password.getText().toString().isEmpty()&&cityId!=null){

                userInfo.setCityId(cityId.getId());
                userInfo.setAddress(address.getText().toString());
                userInfo.setPassword(password.getText().toString());
                userViewModel.setCurrentUserInfo(userInfo);
                userViewModel.registerUser(userInfo);

        }else {
            Toasty.error(getContext(),"Complete All Fields Please", Toast.LENGTH_SHORT).show();
        }
    }



    private void assignFields(){

        address.setText(userInfo.getAddress()!=null?userInfo.getAddress():"");
        password.setText(userInfo.getPassword()!=null?userInfo.getPassword():"");

    }



    private int getCityPosition(List<CityInfo> cityInfos,String name){

        for (int i=0;i<cityInfos.size();i++){

            if (cityInfos.get(i).getCity_en().matches(name)){
                return i;
            }

        }

        return 0;

    }




}
