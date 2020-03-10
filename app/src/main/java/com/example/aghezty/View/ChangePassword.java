package com.example.aghezty.View;


import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aghezty.BroadcastReceiver.NetworkReceiver;
import com.example.aghezty.Interface.CompletableListener;
import com.example.aghezty.Interface.InternetStatus;
import com.example.aghezty.POJO.UserInfo;
import com.example.aghezty.R;
import com.example.aghezty.ViewModel.UserViewModel;
import com.example.aghezty.ViewModel.ViewModelFactory;
import com.gturedi.views.CustomStateOptions;
import com.gturedi.views.StatefulLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;
import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePassword extends Fragment implements InternetStatus {


    @Inject
    ViewModelFactory viewModelFactory;
    private UserViewModel userViewModel;

    private UserInfo userInfo;

    private NavController navController;



    @BindView(R.id.old_edit)
    EditText old_password;
    @BindView(R.id.new_edit)
    EditText new_password;
    @BindView(R.id.confirm_edit)
    EditText confirm_password;
    @BindView(R.id.save)
    Button save;

    @BindView(R.id.stateful)
    StatefulLayout statefulLayout;

    private boolean isConfirm=false;
    private boolean isOnline=false;

    private NetworkReceiver networkReceiver;


    public ChangePassword() {
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
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        navController= Navigation.findNavController(view);

        userInfo=userViewModel.getCurrentUserInfo();

        save.setOnClickListener(v -> {
            if (isOnline) {
                validationFields();
            }else {
                Toasty.warning(getContext(),getResources().getString(R.string.check_connection_warning)).show();
            }
        });

        confirm_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isConfirm=s.toString().matches(new_password.getText().toString());
                if (!isConfirm)
                confirm_password.setError("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }



    private void validationFields(){

        if (!old_password.getText().toString().isEmpty()&&!new_password.getText().toString().isEmpty()&&!confirm_password.getText().toString().isEmpty()){

            if (new_password.getText().toString().length()<6){

                new_password.setError(getResources().getString(R.string.pass_least_6_char_warning));

            }
            else if(!isConfirm){

                Toasty.error(getContext(),getResources().getString(R.string.confirm_pass), Toast.LENGTH_SHORT).show();
            }else {

                statefulLayout.showLoading(" ");
                userViewModel.updateUserPassword(old_password.getText().toString(), new_password.getText().toString(), new CompletableListener() {
                    @Override
                    public void onSuccess() {
                        Toasty.success(getContext(),getResources().getString(R.string.successful_change_pass),Toast.LENGTH_SHORT).show();
                        navController.navigateUp();
                    }

                    @Override
                    public void onFailure(String e) {
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
