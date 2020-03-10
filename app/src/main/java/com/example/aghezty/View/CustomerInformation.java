package com.example.aghezty.View;


import android.content.IntentFilter;
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
import android.widget.Toast;

import com.example.aghezty.BroadcastReceiver.NetworkReceiver;
import com.example.aghezty.Interface.CompletableListener;
import com.example.aghezty.Interface.InternetStatus;
import com.example.aghezty.POJO.CheckOutInfo;
import com.example.aghezty.POJO.UserInfo;
import com.example.aghezty.R;
import com.example.aghezty.Util.CheckOutViewPager;
import com.example.aghezty.ViewModel.UserViewModel;
import com.example.aghezty.ViewModel.ViewModelFactory;
import com.gturedi.views.StatefulLayout;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;
import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerInformation extends Fragment implements InternetStatus {


    @Inject
    ViewModelFactory viewModelFactory;
    private UserViewModel userViewModel;


    private UserInfo userInfo;

    private WeakReference<CheckOutViewPager> viewPagerWeakReference;

    private boolean isOnline=false;

    private NetworkReceiver networkReceiver;



    @BindView(R.id.name_edit)
    EditText name;
    @BindView(R.id.email_edit)
    EditText email;
    @BindView(R.id.phone_edit)
    EditText phoneNumber;

    @BindView(R.id.next)
    Button next;



    @BindView(R.id.stateful)
    StatefulLayout statefulLayout;

    public CustomerInformation(WeakReference<CheckOutViewPager> viewPagerWeakReference) {
        // Required empty public constructor

        this.viewPagerWeakReference=viewPagerWeakReference;

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
        userInfo=userViewModel.getCurrentUserInfo();
        networkReceiver=new NetworkReceiver(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_information, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        assignView();

        next.setOnClickListener(v -> {
            if (isOnline) {
                validationFields();
            }else {
                Toasty.warning(getContext(),getResources().getString(R.string.check_connection_warning)).show();
            }
        });

    }



    private void assignView(){

        name.setText(userInfo.getName());
        email.setText(userInfo.getEmail());
        phoneNumber.setText(userInfo.getPhoneNumber());

    }



    private void validationFields(){

        if (!name.getText().toString().isEmpty()&&!email.getText().toString().isEmpty()&&!phoneNumber.getText().toString().isEmpty()){


            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()){

                email.setError(getResources().getString(R.string.email_not_correct_warning));

            }else if (!name.getText().toString().matches(userInfo.getName())||!email.getText().toString().matches(userInfo.getEmail())||!phoneNumber.getText().toString().matches(userInfo.getPhoneNumber())) {
                statefulLayout.showLoading(" ");
                userInfo.setName(name.getText().toString());
                userInfo.setEmail(email.getText().toString());
                userInfo.setPhoneNumber(phoneNumber.getText().toString());
                userViewModel.updateProfile(userInfo, null, new CompletableListener() {
                    @Override
                    public void onSuccess() {
                        statefulLayout.showContent();
                        if (viewPagerWeakReference.get()!=null){
                            viewPagerWeakReference.get().setCurrentItem(1);
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        statefulLayout.showContent();
                    }
                });

            }else {
                if (viewPagerWeakReference.get()!=null){
                    viewPagerWeakReference.get().setCurrentItem(1);
                }
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
