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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.aghezty.Adapter.AddressCardViewAdapter;
import com.example.aghezty.Adapter.CartCardViewAdapter;
import com.example.aghezty.BroadcastReceiver.NetworkReceiver;
import com.example.aghezty.Interface.InternetStatus;
import com.example.aghezty.POJO.AddressInfo;
import com.example.aghezty.POJO.CartInfo;
import com.example.aghezty.R;
import com.example.aghezty.ViewModel.UserViewModel;
import com.example.aghezty.ViewModel.ViewModelFactory;
import com.gturedi.views.CustomStateOptions;
import com.gturedi.views.StatefulLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

import static com.example.aghezty.View.AddNewAddress.UPDATE_ADDRESS;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAddresses extends Fragment implements InternetStatus {

    @Inject
    ViewModelFactory viewModelFactory;
    private UserViewModel userViewModel;
    private Observer addressesObserver;

    private AddressCardViewAdapter addressCardViewAdapter;

    private List<AddressInfo> addressInfoList=new ArrayList<>();

    private NavController navController;

    private CustomStateOptions networkCustom=new CustomStateOptions().image(R.drawable.ic_signal_wifi_off_black_24dp);
    private NetworkReceiver networkReceiver;


    @BindView(R.id.addresses_recycler)
    RecyclerView addresses_recycler;

    @BindView(R.id.add_new)
    Button addNewAddress;


    @BindView(R.id.stateful)
    StatefulLayout statefulLayout;


    public MyAddresses() {
        // Required empty public constructor

        addressesObserver=new Observer<List<AddressInfo>>() {
            @Override
            public void onChanged(List<AddressInfo> addressInfos) {

                    addressCardViewAdapter.updateAddressList(addressInfos);
                    addressInfoList.clear();
                    addressInfoList.addAll(addressInfos);

                if (!addressInfos.isEmpty()){
                    statefulLayout.showContent();
                }else {
                    statefulLayout.showEmpty("You have no Addresses");
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


        userViewModel.getAddressListMediatorLiveData().observe(this,addressesObserver);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(networkReceiver);
        userViewModel.getAddressListMediatorLiveData().removeObservers(this);
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
        return inflater.inflate(R.layout.fragment_my_addresses, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        navController= Navigation.findNavController(view);



        addressCardViewAdapter=new AddressCardViewAdapter(getContext(),addressInfoList,navController,userViewModel);
        addresses_recycler.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));

        addresses_recycler.setAdapter(addressCardViewAdapter);


        addNewAddress.setOnClickListener(v -> {

            Bundle bundle=new Bundle();
            bundle.putParcelable(UPDATE_ADDRESS,null);
            navController.navigate(R.id.action_myAddresses_to_addNewAddress,bundle);

        });


    }


    @Override
    public void Connect() {
        if (addressInfoList.isEmpty()) {
            statefulLayout.showLoading(" ");
            userViewModel.getAllUserAddress();
        }else {
            statefulLayout.showContent();
        }
    }

    @Override
    public void notConnect() {
        statefulLayout.showCustom(networkCustom.message("Oooopss...  Check your Connection"));
    }
}
