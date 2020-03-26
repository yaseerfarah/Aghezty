package com.example.aghezty.View;


import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aghezty.Adapter.OrderCardViewAdapter;
import com.example.aghezty.BroadcastReceiver.NetworkReceiver;
import com.example.aghezty.Interface.InternetStatus;
import com.example.aghezty.POJO.CartInfo;
import com.example.aghezty.POJO.OrderInfo;
import com.example.aghezty.R;
import com.example.aghezty.ViewModel.UserViewModel;
import com.example.aghezty.ViewModel.ViewModelFactory;
import com.gturedi.views.CustomStateOptions;
import com.gturedi.views.StatefulLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class Order extends Fragment implements InternetStatus {
    @Inject
    ViewModelFactory viewModelFactory;
    private UserViewModel userViewModel;
    private Observer orderObserver;

    private List<OrderInfo> orderInfoList=new ArrayList<>();

    OrderCardViewAdapter orderCardViewAdapter;

    @BindView(R.id.order_recycler)
    RecyclerView orderRecycler;

    @BindView(R.id.stateful)
    StatefulLayout statefulLayout;

    private CustomStateOptions networkCustom=new CustomStateOptions().image(R.drawable.ic_signal_wifi_off_black_24dp);
    private NetworkReceiver networkReceiver;



    public Order() {
        // Required empty public constructor

        orderObserver=new Observer<List<OrderInfo>>() {
            @Override
            public void onChanged(List<OrderInfo> orderInfos) {

                if (!orderInfos.isEmpty()) {
                    Collections.reverse(orderInfos);
                    orderCardViewAdapter.updateOrderList(orderInfos);
                    statefulLayout.showContent();
                    orderInfoList.clear();
                    orderInfoList.addAll(orderInfos);
                }else {
                    statefulLayout.showEmpty(getResources().getString(R.string.no_order));
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

        userViewModel.getOrderInfoListMediatorLiveData().observe(this,orderObserver);

    }


    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(networkReceiver);
        userViewModel.getOrderInfoListMediatorLiveData().removeObservers(this);
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
        return inflater.inflate(R.layout.fragment_order, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        orderCardViewAdapter=new OrderCardViewAdapter(getContext(),orderInfoList);

        orderRecycler.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        orderRecycler.setAdapter(orderCardViewAdapter);

    }

    @Override
    public void Connect() {
            statefulLayout.showLoading(" ");
            userViewModel.getUserOrders();

    }

    @Override
    public void notConnect() {
        statefulLayout.showCustom(networkCustom.message(getResources().getString(R.string.check_connection)));

    }
}
