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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.aghezty.BroadcastReceiver.NetworkReceiver;
import com.example.aghezty.Interface.CompletableListener;
import com.example.aghezty.Interface.InternetStatus;
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
public class Login extends Fragment implements InternetStatus {

    @Inject
    ViewModelFactory viewModelFactory;
    private UserViewModel userViewModel;

    private UserInfo userInfo;

    private NavController navController;

    private boolean isOnline=false;

    private NetworkReceiver networkReceiver;


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



    public Login() {
        // Required empty public constructor
    }


    @Override
    public void onStart() {
        super.onStart();

        IntentFilter netFilter=new IntentFilter();
        netFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        getActivity().registerReceiver(networkReceiver,netFilter);

    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(networkReceiver);
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSupportInjection.inject(this);
        userViewModel= ViewModelProviders.of(this,viewModelFactory).get(UserViewModel.class);
        networkReceiver=new NetworkReceiver(this);

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
            if (isOnline) {
                navController.navigate(R.id.action_login_to_register1);
            }else {
                Toasty.warning(getContext(),getResources().getString(R.string.check_connection_warning)).show();
            }
        });

        signIn.setOnClickListener(v -> {
            if (isOnline) {
                validationFields();
            }else {
                Toasty.warning(getContext(),getResources().getString(R.string.check_connection_warning)).show();
            }
        });



    }


    private void validationFields(){

        if (!email.getText().toString().isEmpty()&&!password.getText().toString().isEmpty()){

             if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()){

                email.setError(getResources().getString(R.string.email_not_correct_warning));
            }else {
                 statefulLayout.showLoading(" ");
                 userViewModel.userLogin(email.getText().toString(), password.getText().toString(), new CompletableListener() {
                     @Override
                     public void onSuccess() {
                         Toasty.success(getContext(),getResources().getString(R.string.successful_login),Toast.LENGTH_SHORT).show();
                         getActivity().finish();
                     }

                     @Override
                     public void onFailure(String message) {
                         statefulLayout.showContent();
                     }
                 });

            }


        }else {
            Toasty.error(getContext(),getResources().getString(R.string.complete_fields_warining), Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void Connect() {
        isOnline=true;
    }

    @Override
    public void notConnect() {
        isOnline=false;
    }
}
