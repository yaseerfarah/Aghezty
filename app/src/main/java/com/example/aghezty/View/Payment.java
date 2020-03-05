package com.example.aghezty.View;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.aghezty.POJO.CheckOutInfo;
import com.example.aghezty.POJO.UserInfo;
import com.example.aghezty.R;
import com.example.aghezty.Util.CheckOutViewPager;
import com.example.aghezty.ViewModel.UserViewModel;
import com.example.aghezty.ViewModel.ViewModelFactory;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

/**
 * A simple {@link Fragment} subclass.
 */
public class Payment extends Fragment {


    @Inject
    ViewModelFactory viewModelFactory;
    private UserViewModel userViewModel;

    private CheckOutInfo checkOutInfo;

    private WeakReference<CheckOutViewPager> viewPagerWeakReference;

    @BindView(R.id.radio_group)
    RadioGroup paymentMethod;

    @BindView(R.id.next)
    Button next;
    @BindView(R.id.back)
    Button back;

    @BindView(R.id.cash_layout)
    RadioButton cash;
    @BindView(R.id.credit_layout)
    RadioButton creditCard;
    @BindView(R.id.paypal_layout)
    RadioButton paypal;

    public Payment(WeakReference<CheckOutViewPager> viewPagerWeakReference) {
        // Required empty public constructor

        this.viewPagerWeakReference=viewPagerWeakReference;

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSupportInjection.inject(this);
        userViewModel= ViewModelProviders.of(this,viewModelFactory).get(UserViewModel.class);
        checkOutInfo=userViewModel.getCheckOutInfo();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment2, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);






        cash.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked){
                checkOutInfo.setPaymentId(1);
                checkOutInfo.setPaymentMethod(cash.getText().toString());
                userViewModel.setCheckOutInfo(checkOutInfo);
            }

        });

        creditCard.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked){
                checkOutInfo.setPaymentId(2);
                checkOutInfo.setPaymentMethod(creditCard.getText().toString());
                userViewModel.setCheckOutInfo(checkOutInfo);
            }

        });

        paypal.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked){
                checkOutInfo.setPaymentId(3);
                checkOutInfo.setPaymentMethod(paypal.getText().toString());
                userViewModel.setCheckOutInfo(checkOutInfo);
            }

        });



        switch (checkOutInfo.getPaymentId()){

            case 0:
                cash.setChecked(true);
                break;
            case 1:
                cash.setChecked(true);
                break;

            case 2:
                creditCard.setChecked(true);
                break;

            case 3:
                paypal.setChecked(true);
                break;

        }



        next.setOnClickListener(v -> {

            if (viewPagerWeakReference.get() != null) {
                viewPagerWeakReference.get().setCurrentItem(3);
            }

        });
        back.setOnClickListener(v -> {
            if (viewPagerWeakReference.get()!=null){
                viewPagerWeakReference.get().setCurrentItem(1);
            }
        });



    }
}
