package com.example.aghezty.View;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aghezty.POJO.UserInfo;
import com.example.aghezty.R;
import com.example.aghezty.ViewModel.UserViewModel;
import com.example.aghezty.ViewModel.ViewModelFactory;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {

    @Inject
    ViewModelFactory viewModelFactory;
    private UserViewModel userViewModel;

    private UserInfo userInfo;

    private NavController navController;


    @BindView(R.id.person_name)
    TextView userName;
    @BindView(R.id.person_email)
    TextView userEmail;
    @BindView(R.id.an_first)
    TextView userFirstName;
    @BindView(R.id.an_last)
    TextView userLastName;
    @BindView(R.id.an_city)
    TextView userCity;
    @BindView(R.id.an_governorate)
    TextView userGovernorate;
    @BindView(R.id.an_address)
    TextView userAddress;
    @BindView(R.id.an_phone)
    TextView userPhoneNumber;


    @BindView(R.id.user_image)
    ImageView imageView;

    @BindView(R.id.setting)
    ImageButton editProfile;


    public Profile() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSupportInjection.inject(this);
        userViewModel= ViewModelProviders.of(this,viewModelFactory).get(UserViewModel.class);
        userInfo=userViewModel.getCurrentUserInfo();
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        assignView();

    }



    private void assignView(){

        userName.setText(userInfo.getName());
        String[]name=userInfo.getName().split(" ");
        userFirstName.setText(name[0]);
        if (name[1] != null) {
            userLastName.setText(name[1]);
        } else {
            userLastName.setText(" ");
        }

        userEmail.setText(userInfo.getEmail());
        userGovernorate.setText(userInfo.getGovernorate());
        userCity.setText(userInfo.getCity());
        userAddress.setText(userInfo.getAddress());
        userPhoneNumber.setText(userInfo.getPhoneNumber());

    }

}
