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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.aghezty.Interface.CompletableListener;
import com.example.aghezty.POJO.UserInfo;
import com.example.aghezty.R;
import com.example.aghezty.ViewModel.UserViewModel;
import com.example.aghezty.ViewModel.ViewModelFactory;
import com.gturedi.views.StatefulLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;
import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class Login extends Fragment {

    @Inject
    ViewModelFactory viewModelFactory;
    private UserViewModel userViewModel;

    private UserInfo userInfo;

    private NavController navController;

    @BindView(R.id.email_edit)
    EditText email;
    @BindView(R.id.password_edit)
    EditText password;
    @BindView(R.id.login)
    Button signIn;
    @BindView(R.id.sign_up)
    LinearLayout signUp;
    @BindView(R.id.stateful)
    StatefulLayout statefulLayout;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSupportInjection.inject(this);
        userViewModel= ViewModelProviders.of(this,viewModelFactory).get(UserViewModel.class);

    }

    public Login() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        navController= Navigation.findNavController(view);


        signUp.setOnClickListener(v -> {
            navController.navigate(R.id.action_login_to_register1);
        });

        signIn.setOnClickListener(v -> {
            validationFields();
        });



    }


    private void validationFields(){

        if (!email.getText().toString().isEmpty()&&!password.getText().toString().isEmpty()){

             if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()){

                email.setError("Email Format Not Correct");
            }else {
                 statefulLayout.showLoading(" ");
                 userViewModel.userLogin(email.getText().toString(), password.getText().toString(), new CompletableListener() {
                     @Override
                     public void onSuccess() {
                         Toasty.success(getContext(),"Successful Login",Toast.LENGTH_SHORT).show();
                         getActivity().finish();
                     }

                     @Override
                     public void onFailure(String message) {
                         statefulLayout.showContent();
                     }
                 });

            }


        }else {
            Toasty.error(getContext(),"Complete All Fields Please", Toast.LENGTH_SHORT).show();
        }


    }


}
