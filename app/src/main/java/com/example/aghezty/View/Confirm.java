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
public class Confirm extends Fragment {

    @Inject
    ViewModelFactory viewModelFactory;
    private UserViewModel userViewModel;

    private UserInfo userInfo;
    private CheckOutInfo checkOutInfo;

    private NumberFormat numberFormat;

    private Observer addressesObserver;

    private List<AddressInfo> addressInfoList=new ArrayList<>();
    private List<CartInfo> cartInfoList =new ArrayList<>();

    private WeakReference<CheckOutViewPager> viewPagerWeakReference;

    private AddressInfo addressInfo;

    @BindView(R.id.an_name)
    TextView name;
    @BindView(R.id.an_email)
    TextView email;
    @BindView(R.id.an_phone)
    TextView phone;

    @BindView(R.id.an_city)
    TextView city;
    @BindView(R.id.an_governorate)
    TextView governorate;
    @BindView(R.id.an_address)
    TextView address;

    @BindView(R.id.an_payment_method)
    TextView paymentMethod;

    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.back)
    Button back;


    @BindView(R.id.an_sub_total)
    TextView subTotal;
    @BindView(R.id.an_coupon_discount)
    TextView couponDiscount;
    @BindView(R.id.an_total_price)
    TextView totalPrice;
    @BindView(R.id.an_shipping_amount)
    TextView shippingAmount;

    @BindView(R.id.stateful)
    StatefulLayout statefulLayout;

    NavController navController;





    public Confirm(WeakReference<CheckOutViewPager> viewPagerWeakReference) {
        // Required empty public constructor

        this.viewPagerWeakReference=viewPagerWeakReference;

    }








    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSupportInjection.inject(this);
        userViewModel= ViewModelProviders.of(this,viewModelFactory).get(UserViewModel.class);
        userInfo=userViewModel.getCurrentUserInfo();
        checkOutInfo=userViewModel.getCheckOutInfo();
        cartInfoList.clear();
        cartInfoList.addAll(userViewModel.getCartInfolist());
        addressInfoList.clear();
        addressInfoList.addAll(userViewModel.getAddressInfoList());
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        navController= Navigation.findNavController(view);
        numberFormat=NumberFormat.getInstance(Locale.US);

        submit.setOnClickListener(v -> {
            statefulLayout.showLoading(" ");
            userViewModel.checkOut(new CompletableListener() {
                @Override
                public void onSuccess() {
                    Toasty.success(view.getContext(),getResources().getString(R.string.successful_checkout),Toast.LENGTH_SHORT).show();
                    statefulLayout.showContent();
                    navController.navigateUp();

                }

                @Override
                public void onFailure(String message) {
                    statefulLayout.showContent();
                }
            });

        });


        back.setOnClickListener(v -> {
            if (viewPagerWeakReference.get()!=null){
                viewPagerWeakReference.get().setCurrentItem(2);
            }

        });

    }


    @Override
    public void onResume() {
        super.onResume();
        assignView();
    }

    private void assignView(){

        name.setText(userInfo.getName());
        email.setText(userInfo.getEmail());
        phone.setText(userInfo.getPhoneNumber());

       addressInfo=getSelectedAddressInfoByID(checkOutInfo.getAddressId());
        city.setText(addressInfo.getCity_en());
        governorate.setText(addressInfo.getGovernorate_en());
        address.setText(addressInfo.getAddress());



        paymentMethod.setText(checkOutInfo.getPaymentMethod());

        subTotal.setText(numberFormat.format(countSubTotal())+" "+getResources().getString(R.string.egp));

        couponDiscount.setText(numberFormat.format(userViewModel.getCouponDiscount()) +" "+getResources().getString(R.string.egp));

       // shippingAmount.setText(numberFormat.format(addressInfo.getShippingAmount())+" EGP");

        shippingAmount.setText("50.99 EGP");

        int total=countSubTotal()-userViewModel.getCouponDiscount()-addressInfo.getShippingAmount();
        totalPrice.setText(numberFormat.format(total)+" "+getResources().getString(R.string.egp));




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
