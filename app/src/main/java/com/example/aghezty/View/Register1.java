package com.example.aghezty.View;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.aghezty.POJO.UserInfo;
import com.example.aghezty.R;
import com.example.aghezty.ViewModel.ProductViewModel;
import com.example.aghezty.ViewModel.UserViewModel;
import com.example.aghezty.ViewModel.ViewModelFactory;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;
import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class Register1 extends Fragment {

    @Inject
    ViewModelFactory viewModelFactory;
    private UserViewModel userViewModel;

    private  UserInfo userInfo;

    private NavController navController;

    @BindView(R.id.first_edit)
    EditText firstName;
    @BindView(R.id.last_edit)
    EditText lastName;
    @BindView(R.id.email_edit)
    EditText email;
    @BindView(R.id.phone_edit)
    EditText phoneNumber;
    @BindView(R.id.next)
    Button next;
    @BindView(R.id.sign_in)
    LinearLayout signIn;

    public Register1() {
        // Required empty public constructor
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
        return inflater.inflate(R.layout.fragment_register1, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        navController= Navigation.findNavController(view);



        userInfo=userViewModel.getCurrentUserInfo();
        if (userInfo!=null){
            assignFields();
        }else {
            userInfo=new UserInfo();
        }
        next.setOnClickListener(v -> {
            validationFields();
        });


        signIn.setOnClickListener(v -> {
            navController.navigate(R.id.action_register1_to_login);
        });

    }



    private void validationFields(){

        if (!firstName.getText().toString().isEmpty()&&!lastName.getText().toString().isEmpty()&&!email.getText().toString().isEmpty()&&!phoneNumber.getText().toString().isEmpty()){

            if(phoneNumber.getText().length()!=11){
                phoneNumber.setError("Phone Number Should be 11 Number");
            }
            else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()){

                email.setError("Email Format Not Correct");
            }else {


                userInfo.setName(firstName.getText().toString()+" "+lastName.getText().toString());
                userInfo.setEmail(email.getText().toString());
                userInfo.setPhoneNumber(phoneNumber.getText().toString());

                userViewModel.setCurrentUserInfo(userInfo);
                navController.navigate(R.id.action_register1_to_register2);



            }


        }else {
            Toasty.error(getContext(),"Complete All Fields Please", Toast.LENGTH_SHORT).show();
        }


    }



    private void assignFields(){

        String[]name=userInfo.getName().split("\\s+");

        firstName.setText(name[0]);
        if (name[1]!=null){
            lastName.setText(name[1]);
        }
        email.setText(userInfo.getEmail());
        phoneNumber.setText(userInfo.getPhoneNumber());

    }


}
