package com.example.aghezty.View;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.example.aghezty.Interface.CompletableListener;
import com.example.aghezty.POJO.AddressInfo;
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
public class AddNewAddress extends Fragment {



    @Inject
    ViewModelFactory viewModelFactory;
    private UserViewModel userViewModel;


    private Observer governorateobserver;
    private Observer citiesobserver;


    private List<GovernorateInfo> governorateInfoList=new ArrayList<>();
    private List<CityInfo> cityInfoList=new ArrayList<>();

    private CityInfo city_choice;
    private GovernorateInfo governorate_choice;

    private NavController navController;

    @BindView(R.id.city_edit)
    Spinner city;
    @BindView(R.id.governorate_edit)
    Spinner governorate;
    @BindView(R.id.address_edit)
    EditText address;

    @BindView(R.id.save)
    Button save;

    @BindView(R.id.stateful)
    StatefulLayout statefulLayout;


    @BindView(R.id.city_info)
    LinearLayout cityInfo;







    public AddNewAddress() {
        // Required empty public constructor


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


            }
        };


    }




    @Override
    public void onStart() {
        super.onStart();
        userViewModel.getAllGovernorate();
        userViewModel.getGovernorateListMediatorLiveData().observe(this,governorateobserver);
        userViewModel.getCitiesListMediatorLiveData().observe(this,citiesobserver);

    }


    @Override
    public void onStop() {
        super.onStop();
        userViewModel.getGovernorateListMediatorLiveData().removeObservers(this);
        userViewModel.getCitiesListMediatorLiveData().removeObservers(this);
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
        return inflater.inflate(R.layout.fragment_add_new_address, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        navController= Navigation.findNavController(view);



        governorate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                governorate_choice=governorateInfoList.get(position);
                userViewModel.getCitiesByGovernorateId(governorateInfoList.get(position).getId());
                cityInfo.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city_choice=cityInfoList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


       save.setOnClickListener(v -> {

           validationFields();

       });

    }



    private void validationFields(){

        if (!address.getText().toString().isEmpty()&&city_choice!=null){

            statefulLayout.showLoading(" ");
            AddressInfo addressInfo=new AddressInfo(city_choice.getId(),address.getText().toString(),city_choice.getCity_en(),city_choice.getCity_ar(),governorate_choice.getGovernorate_en(),governorate_choice.getGovernorate_ar());

            userViewModel.addNewAddress(addressInfo, new CompletableListener() {
                @Override
                public void onSuccess() {
                    navController.navigateUp();
                    Toasty.success(getContext(),"Successful Add New Address",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(String message) {
                    Toasty.error(getContext(),message,Toast.LENGTH_SHORT).show();
                    statefulLayout.showContent();
                }
            });

        }else {
            Toasty.error(getContext(),"Complete All Fields Please", Toast.LENGTH_SHORT).show();
        }
    }






}
