package com.example.aghezty.View;


import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.aghezty.Adapter.CartCardViewAdapter;
import com.example.aghezty.BroadcastReceiver.NetworkReceiver;
import com.example.aghezty.Interface.CheckCoupon;
import com.example.aghezty.Interface.InternetStatus;
import com.example.aghezty.POJO.CartInfo;
import com.example.aghezty.POJO.UserInfo;
import com.example.aghezty.R;
import com.example.aghezty.ViewModel.UserViewModel;
import com.example.aghezty.ViewModel.ViewModelFactory;
import com.gturedi.views.CustomStateOptions;
import com.gturedi.views.StatefulLayout;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

/**
 * A simple {@link Fragment} subclass.
 */
public class Cart extends Fragment implements InternetStatus {

    @Inject
    ViewModelFactory viewModelFactory;
    private UserViewModel userViewModel;
    private Observer cartObserver;


    private  final int CONTENT=0;
    private  final int EMPTY=1;
    private  final int NETWORK=2;

    private int currentSate;

    private CartCardViewAdapter cartCardViewAdapter;

    private List<CartInfo> cartInfoList=new ArrayList<>();

    private NavController navController;

    private String couponText="";
    private int integerCouponDiscount;
    private int integerTotalPrice;


    private NumberFormat numberFormat;

    @BindView(R.id.stateful)
    StatefulLayout statefulLayout;

    @BindView(R.id.coupon_edit)
    EditText coupon;

    @BindView(R.id.coupon_apply)
    Button checkCoupon;

    @BindView(R.id.an_coupon_discount)
    TextView couponDiscount;

    @BindView(R.id.total_price)
    TextView totalPrice;

    @BindView(R.id.warning)
    TextView warning;

    @BindView(R.id.card_checkout)
    Button checkout;


    @BindView(R.id.progress)
    ProgressBar progressBar;




    @BindView(R.id.cart_recycleview)
    RecyclerView cartRecycler;

    private CustomStateOptions networkCustom=new CustomStateOptions().image(R.drawable.ic_signal_wifi_off_black_24dp);
    private NetworkReceiver networkReceiver;


    public Cart() {
        // Required empty public constructor

        cartObserver=new Observer<List<CartInfo>>() {
            @Override
            public void onChanged(List<CartInfo> cartInfos) {

                    cartCardViewAdapter.updateCartList(cartInfos);
                    cartInfoList.clear();
                    cartInfoList.addAll(cartInfos);
                    calculateTotalPrice(cartInfos);

                if (!cartInfos.isEmpty()&&currentSate!=CONTENT) {
                    currentSate=CONTENT;
                    statefulLayout.showContent();

                }else if (cartInfos.isEmpty()&&currentSate!=EMPTY){
                    currentSate=EMPTY;
                    statefulLayout.showEmpty("The Cart is Empty");
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

        userViewModel.getCartListMediatorLiveData().observe(this,cartObserver);
    }


    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(networkReceiver);
        userViewModel.getCartListMediatorLiveData().removeObservers(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSupportInjection.inject(this);
        userViewModel= ViewModelProviders.of(this,viewModelFactory).get(UserViewModel.class);
        integerCouponDiscount=userViewModel.getCouponDiscount();
        networkReceiver=new NetworkReceiver(this);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        navController= Navigation.findNavController(view);

        numberFormat=NumberFormat.getInstance(Locale.US);

        cartCardViewAdapter=new CartCardViewAdapter(getContext(),cartInfoList,userViewModel);

        cartRecycler.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));

        cartRecycler.setAdapter(cartCardViewAdapter);

        couponDiscount.setText(String.valueOf(numberFormat.format(integerCouponDiscount)));


        checkCoupon.setOnClickListener(v -> {

            if (coupon.getText().length()>0&&!coupon.getText().toString().matches(couponText)){
                progressBar.setVisibility(View.VISIBLE);
                warning.setVisibility(View.GONE);
                couponText=coupon.getText().toString();
                userViewModel.getCouponDiscount(couponText, new CheckCoupon() {
                    @Override
                    public void onSuccess(int discount) {
                        progressBar.setVisibility(View.GONE);
                        warning.setVisibility(View.GONE);
                        integerCouponDiscount=discount;
                        integerTotalPrice-=discount;
                        if (integerTotalPrice<0){
                            integerTotalPrice=0;
                        }
                        couponDiscount.setText(numberFormat.format(integerCouponDiscount));
                        totalPrice.setText(numberFormat.format(integerTotalPrice));
                    }

                    @Override
                    public void onFailure(String message) {
                        progressBar.setVisibility(View.GONE);
                        warning.setText(message);
                        warning.setVisibility(View.VISIBLE);
                    }
                });
            }


        });



        checkout.setOnClickListener(v -> {

            navController.navigate(R.id.action_cart_to_checkOut);

        });


    }



    private void calculateTotalPrice(List<CartInfo> cartInfos){
        int total=0;

        for (CartInfo cartInfo:cartInfos){
            total+=cartInfo.getPro_price()*cartInfo.getQuantity();
        }

        integerTotalPrice=total;

        integerTotalPrice-=integerCouponDiscount;
        if (integerTotalPrice<0){
            integerTotalPrice=0;
        }

        totalPrice.setText(String.valueOf(numberFormat.format(integerTotalPrice)));

    }


    @Override
    public void Connect() {
        if (cartInfoList.isEmpty()){
            userViewModel.getAllCart();
        }else if (currentSate!=CONTENT){
            currentSate=CONTENT;
            statefulLayout.showContent();
        }



    }

    @Override
    public void notConnect() {
        if (currentSate!=NETWORK) {
            currentSate=NETWORK;
            statefulLayout.showCustom(networkCustom.message("Oooopss...  Check your Connection"));
        }
    }
}
