package com.example.aghezty.View;


import android.content.IntentFilter;
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

import com.example.aghezty.BroadcastReceiver.NetworkReceiver;
import com.example.aghezty.Interface.CompletableListener;
import com.example.aghezty.Interface.InternetStatus;
import com.example.aghezty.POJO.AddressInfo;
import com.example.aghezty.POJO.CityInfo;
import com.example.aghezty.POJO.GovernorateInfo;
import com.example.aghezty.POJO.UserInfo;
import com.example.aghezty.R;
import com.example.aghezty.ViewModel.UserViewModel;
import com.example.aghezty.ViewModel.ViewModelFactory;
import com.gturedi.views.CustomStateOptions;
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
public class AddNewAddress extends Fragment implements InternetStatus {


    public static String UPDATE_ADDRESS="AddressData";

    @Inject
    ViewModelFactory viewModelFactory;
    private UserViewModel userViewModel;


    private Observer governorateobserver;
    private Observer citiesobserver;

    private AddressInfo addressInfo;

    private List<GovernorateInfo> governorateInfoList=new ArrayList<>();
    private List<CityInfo> cityInfoList=new ArrayList<>();

    private CityInfo city_choice;
    private GovernorateInfo governorate_choice;

    private NavController navController;

    private CustomStateOptions networkCustom=new CustomStateOptions().image(R.drawable.ic_signal_wifi_off_black_24dp);
    private NetworkReceiver networkReceiver;



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
                    items.add(governorateInfo.getGovernorate());
                }

                ArrayAdapter arrayAdapter=new ArrayAdapter(getContext(),R.layout.text_spinner_layout,items);
                arrayAdapter.setDropDownViewResource(R.layout.spinner_item);

                governorate.setAdapter(arrayAdapter);

                if (addressInfo!=null){
                    governorate.setSelection(governoratePosition(addressInfo));
                    address.setText(addressInfo.getAddress());
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
                    items.add(cityInfo.getCity());
                }

                ArrayAdapter arrayAdapter=new ArrayAdapter(getContext(),R.layout.text_spinner_layout,items);
                arrayAdapter.setDropDownViewResource(R.layout.spinner_item);

                city.setAdapter(arrayAdapter);

                if (city_choice==null&&addressInfo!=null){
                    city.setSelection(getCityPosition(cityInfos,addressInfo.getCity_en()));
                }

            }
        };


    }




    @Override
    public void onStart() {
        super.onStart();

        IntentFilter netFilter=new IntentFilter();
        netFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        getActivity().registerReceiver(networkReceiver,netFilter);

        userViewModel.getGovernorateListMediatorLiveData().observe(this,governorateobserver);
        userViewModel.getCitiesListMediatorLiveData().observe(this,citiesobserver);

    }


    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(networkReceiver);
        userViewModel.getGovernorateListMediatorLiveData().removeObservers(this);
        userViewModel.getCitiesListMediatorLiveData().removeObservers(this);

    }





    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSupportInjection.inject(this);
        userViewModel= ViewModelProviders.of(this,viewModelFactory).get(UserViewModel.class);
        addressInfo=getArguments().getParcelable(UPDATE_ADDRESS);
        networkReceiver=new NetworkReceiver(this);



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

            if (addressInfo!=null){

                updateAddress();

            }else {
                addNewAddress();
            }


        }else {
            Toasty.error(getContext(),getResources().getString(R.string.complete_fields_warining), Toast.LENGTH_SHORT).show();
        }
    }




    private void addNewAddress(){
        AddressInfo addressData=new AddressInfo(city_choice.getId(),address.getText().toString(),city_choice.getCity_en(),city_choice.getCity_ar(),governorate_choice.getGovernorate_en(),governorate_choice.getGovernorate_ar());

        userViewModel.addNewAddress(addressData, new CompletableListener() {
            @Override
            public void onSuccess() {
                navController.navigateUp();
                Toasty.success(getContext(),getResources().getString(R.string.success_add_address),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String message) {
               // Toasty.error(getContext(),message,Toast.LENGTH_SHORT).show();
                statefulLayout.showContent();
            }
        });
    }


    private void updateAddress(){


        addressInfo.setCity_id(city_choice.getId());
        addressInfo.setAddress(address.getText().toString());
        addressInfo.setCity_ar(city_choice.getCity_ar());
        addressInfo.setCity_en(city_choice.getCity_en());
        addressInfo.setGovernorate_ar(governorate_choice.getGovernorate_ar());
        addressInfo.setGovernorate_en(governorate_choice.getGovernorate_en());


        userViewModel.updateAddress(addressInfo, new CompletableListener() {
            @Override
            public void onSuccess() {
                navController.navigateUp();
                Toasty.success(getContext(),getResources().getString(R.string.successful_update_address),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String message) {
               // Toasty.error(getContext(),message,Toast.LENGTH_SHORT).show();
                statefulLayout.showContent();
            }
        });
    }




    private int governoratePosition(AddressInfo addressInfo){

        for (int i=0;i<governorateInfoList.size();i++){

            if (governorateInfoList.get(i).getGovernorate_en().matches(addressInfo.getGovernorate_en())){
                return i;
            }
        }

        return 0;
    }

    private int getCityPosition(List<CityInfo> cityInfos,String name){

        for (int i=0;i<cityInfos.size();i++){

            if (cityInfos.get(i).getCity_en().matches(name)){
                return i;
            }

        }

        return 0;

    }


    @Override
    public void Connect() {
        if (governorateInfoList.isEmpty()) {
            statefulLayout.showLoading(" ");
            userViewModel.getAllGovernorate();
        }else {
            statefulLayout.showContent();
        }
    }

    @Override
    public void notConnect() {
        statefulLayout.showCustom(networkCustom.message(getResources().getString(R.string.check_connection)));
    }
}
