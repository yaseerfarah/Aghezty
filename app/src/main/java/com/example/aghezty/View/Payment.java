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
import android.widget.TextView;
import android.widget.Toast;

import com.example.aghezty.Interface.CompletableListener;
import com.example.aghezty.POJO.AddressInfo;
import com.example.aghezty.POJO.CartInfo;
import com.example.aghezty.POJO.CheckOutInfo;
import com.example.aghezty.POJO.UserInfo;
import com.example.aghezty.R;
import com.example.aghezty.Util.CheckOutViewPager;
import com.example.aghezty.ViewModel.UserViewModel;
import com.example.aghezty.ViewModel.ViewModelFactory;
import com.gturedi.views.StatefulLayout;

import java.lang.ref.WeakReference;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;
import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class Payment extends Fragment {


    @Inject
    ViewModelFactory viewModelFactory;
    private UserViewModel userViewModel;

    private CheckOutInfo checkOutInfo;
    NumberFormat numberFormat;
    private AddressInfo addressInfo;

    private WeakReference<CheckOutViewPager> viewPagerWeakReference;

    private List<AddressInfo> addressInfoList=new ArrayList<>();
    private List<CartInfo> cartInfoList =new ArrayList<>();

    @BindView(R.id.radio_group)
    RadioGroup paymentMethod;

    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.back)
    Button back;

    @BindView(R.id.paypal)
    Button paypalPayment;

    @BindView(R.id.an_sub_total)
    TextView subTotal;
    @BindView(R.id.an_coupon_discount)
    TextView couponDiscount;
    @BindView(R.id.an_total_price)
    TextView totalPrice;
    @BindView(R.id.an_shipping_amount)
    TextView shippingAmount;

    @BindView(R.id.cash_layout)
    RadioButton cash;
    @BindView(R.id.credit_layout)
    RadioButton creditCard;
    @BindView(R.id.paypal_layout)
    RadioButton paypal;

    @BindView(R.id.stateful)
    StatefulLayout statefulLayout;

    public Payment(WeakReference<CheckOutViewPager> viewPagerWeakReference) {
        // Required empty public constructor

        this.viewPagerWeakReference=viewPagerWeakReference;

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSupportInjection.inject(this);
        userViewModel= ViewModelProviders.of(this,viewModelFactory).get(UserViewModel.class);

        cartInfoList.clear();
        cartInfoList.addAll(userViewModel.getCartInfolist());
        addressInfoList.clear();
        addressInfoList.addAll(userViewModel.getAddressInfoList());
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment2, container, false);
    }


    @Override
    public void onResume() {
        super.onResume();
        assignView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        numberFormat= NumberFormat.getInstance(Locale.US);




        cash.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked){
                checkOutInfo.setPaymentId(1);
                paypalPayment.setVisibility(View.GONE);
                checkOutInfo.setPaymentMethod(cash.getText().toString());
                userViewModel.setCheckOutInfo(checkOutInfo);
            }

        });

        creditCard.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked){
                checkOutInfo.setPaymentId(2);
                paypalPayment.setVisibility(View.GONE);
                checkOutInfo.setPaymentMethod(creditCard.getText().toString());
                userViewModel.setCheckOutInfo(checkOutInfo);
            }

        });

        paypal.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked){
                checkOutInfo.setPaymentId(3);
                paypalPayment.setVisibility(View.VISIBLE);
                checkOutInfo.setPaymentMethod(paypal.getText().toString());
                userViewModel.setCheckOutInfo(checkOutInfo);
            }

        });







        submit.setOnClickListener(v -> {

            statefulLayout.showLoading(" ");
            userViewModel.checkOut(new CompletableListener() {
                @Override
                public void onSuccess() {
                    Toasty.success(view.getContext(),getResources().getString(R.string.successful_checkout),Toast.LENGTH_SHORT).show();
                    statefulLayout.showContent();

                    if (viewPagerWeakReference.get() != null) {
                        viewPagerWeakReference.get().setCurrentItem(3);
                    }

                }

                @Override
                public void onFailure(String message) {
                    statefulLayout.showContent();


                }
            });



        });
        back.setOnClickListener(v -> {
            if (viewPagerWeakReference.get()!=null){
                viewPagerWeakReference.get().setCurrentItem(1);
            }
        });

    }


    private void assignView(){
        checkOutInfo=userViewModel.getCheckOutInfo();
        addressInfo=getSelectedAddressInfoByID(checkOutInfo.getAddressId());

        subTotal.setText(numberFormat.format(countSubTotal())+" "+getResources().getString(R.string.egp));

        couponDiscount.setText(numberFormat.format(userViewModel.getCouponDiscount()) +" "+getResources().getString(R.string.egp));

        shippingAmount.setText(numberFormat.format(addressInfo.getShippingAmount())+" "+getResources().getString(R.string.egp));



        int total=countSubTotal()-userViewModel.getCouponDiscount()+addressInfo.getShippingAmount();
        totalPrice.setText(numberFormat.format(total)+" "+getResources().getString(R.string.egp));



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


    }


    private AddressInfo getSelectedAddressInfoByID(int id){

        for (AddressInfo addressInfo:addressInfoList){
            if (addressInfo.getId()==id){
                return addressInfo;
            }
        }
        return null;
    }

    private int countSubTotal(){

        int subTotal=0;

        for (CartInfo cartInfo:cartInfoList){

            subTotal+=cartInfo.getPro_totalPrice();

        }
        return subTotal;
    }




}
