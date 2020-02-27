package com.example.aghezty.View;


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
import android.widget.TextView;

import com.example.aghezty.Adapter.CartCardViewAdapter;
import com.example.aghezty.POJO.CartInfo;
import com.example.aghezty.POJO.UserInfo;
import com.example.aghezty.R;
import com.example.aghezty.ViewModel.UserViewModel;
import com.example.aghezty.ViewModel.ViewModelFactory;
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
public class Cart extends Fragment {

    @Inject
    ViewModelFactory viewModelFactory;
    private UserViewModel userViewModel;
    private Observer cartObserver;

    private CartCardViewAdapter cartCardViewAdapter;

    private List<CartInfo> cartInfoList=new ArrayList<>();

    private NavController navController;



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

    @BindView(R.id.card_checkout)
    Button checkout;


    @BindView(R.id.cart_recycleview)
    RecyclerView cartRecycler;



    public Cart() {
        // Required empty public constructor

        cartObserver=new Observer<List<CartInfo>>() {
            @Override
            public void onChanged(List<CartInfo> cartInfos) {
                if (!cartInfos.isEmpty()) {
                    cartCardViewAdapter.updateCartList(cartInfos);
                    statefulLayout.showContent();
                    calculateTotalPrice(cartInfos);
                    cartInfoList.clear();
                    cartInfoList.addAll(cartInfos);
                }else {
                    statefulLayout.showEmpty("The Cart is Empty");
                }

            }

        };

    }


    @Override
    public void onStart() {
        super.onStart();
        userViewModel.getCartListMediatorLiveData().observe(this,cartObserver);
    }


    @Override
    public void onStop() {
        super.onStop();
        userViewModel.getCartListMediatorLiveData().removeObservers(this);
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
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        navController= Navigation.findNavController(view);

        cartCardViewAdapter=new CartCardViewAdapter(getContext(),cartInfoList,userViewModel);

        cartRecycler.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));

        cartRecycler.setAdapter(cartCardViewAdapter);

        couponDiscount.setText(String.valueOf(NumberFormat.getInstance(Locale.US).format(0)));

    }



    private void calculateTotalPrice(List<CartInfo> cartInfos){
        int total=0;

        for (CartInfo cartInfo:cartInfos){
            total+=cartInfo.getPro_price()*cartInfo.getQuantity();
        }

        totalPrice.setText(String.valueOf(NumberFormat.getInstance(Locale.US).format(total)));

    }


}
