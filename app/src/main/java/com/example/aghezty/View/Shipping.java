package com.example.aghezty.View;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aghezty.Adapter.CheckOutAddressCardViewAdapter;
import com.example.aghezty.Adapter.CheckOutViewPagerAdapter;
import com.example.aghezty.Adapter.FilterOrderCardViewAdapter;
import com.example.aghezty.Interface.CompletableListener;
import com.example.aghezty.POJO.AddressInfo;
import com.example.aghezty.POJO.CheckOutInfo;
import com.example.aghezty.POJO.FilterInfo;
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

import static com.example.aghezty.View.AddNewAddress.UPDATE_ADDRESS;

/**
 * A simple {@link Fragment} subclass.
 */
public class Shipping extends Fragment {

    @Inject
    ViewModelFactory viewModelFactory;
    private UserViewModel userViewModel;

    private Observer addressesObserver;
    private List<AddressInfo> addressInfoList=new ArrayList<>();


    private AddressInfo addressInfo;

    private NavController navController;

    private WeakReference<CheckOutViewPager> viewPagerWeakReference;



    @BindView(R.id.an_city)
    TextView city;
    @BindView(R.id.an_governorate)
    TextView governorate;
    @BindView(R.id.an_address)
    TextView address;
    @BindView(R.id.an_shipping_amount)
    TextView shippingAmount;

    @BindView(R.id.next)
    Button next;
    @BindView(R.id.back)
    Button back;


    @BindView(R.id.my_addresses)
    RelativeLayout myAddresses;

    @BindView(R.id.add_new_address)
    RelativeLayout addNewAddress;

    @BindView(R.id.stateful)
    StatefulLayout statefulLayout;

    @BindView(R.id.address_info)
    RelativeLayout address_layout;


    public Shipping( WeakReference<CheckOutViewPager> viewPagerWeakReference) {
        // Required empty public constructor

        this.viewPagerWeakReference=viewPagerWeakReference;

        addressesObserver=new Observer<List<AddressInfo>>() {
            @Override
            public void onChanged(List<AddressInfo> addressInfos) {

                statefulLayout.showContent();
                addressInfoList.clear();
                addressInfoList.addAll(addressInfos);
                assignView();

            }

        };
    }



    @Override
    public void onStart() {
        super.onStart();
        userViewModel.getAllUserAddress();
        userViewModel.getAddressListMediatorLiveData().observe(this,addressesObserver);
    }

    @Override
    public void onStop() {
        super.onStop();
        userViewModel.getAddressListMediatorLiveData().removeObserver(addressesObserver);

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
        return inflater.inflate(R.layout.fragment_shipping, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        navController= Navigation.findNavController(view);
        statefulLayout.showLoading(" ");
        addNewAddress.setOnClickListener(v -> {
            Bundle bundle=new Bundle();
            bundle.putParcelable(UPDATE_ADDRESS,null);
            navController.navigate(R.id.action_global_addNewAddress,bundle);

        });


        myAddresses.setOnClickListener(v -> {

            myAddressDialog();

        });
        next.setOnClickListener(v -> {

            if (addressInfo.getId()>0) {

                if (viewPagerWeakReference.get() != null) {
                    viewPagerWeakReference.get().setCurrentItem(2);
                }
            }
        });
        back.setOnClickListener(v -> {
            if (viewPagerWeakReference.get()!=null){
                viewPagerWeakReference.get().setCurrentItem(0);
            }
        });


    }


    private void assignView(){




        if (addressInfoList.isEmpty()){
            myAddresses.setVisibility(View.GONE);
            address_layout.setVisibility(View.GONE);

        }else {

            addressInfo=addressInfoList.get(addressInfoList.size()-1);
            city.setText(addressInfoList.get(addressInfoList.size()-1).getCity_en());
            governorate.setText(addressInfoList.get(addressInfoList.size()-1).getGovernorate_en());
            address.setText(addressInfoList.get(addressInfoList.size()-1).getAddress());
            CheckOutInfo checkOutInfo=userViewModel.getCheckOutInfo();
            checkOutInfo.setAddressId(addressInfo.getId());
            userViewModel.setCheckOutInfo(checkOutInfo);
           // shippingAmount.setText(NumberFormat.getInstance(Locale.US).format(addressInfoList.get(addressInfoList.size()-1).getShippingAmount()));

            shippingAmount.setText("50.99 EGP");

        }

    }



    private void myAddressDialog(){

        final Dialog dialog=new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_filter);

        TextView title=dialog.findViewById(R.id.title);
        Button done=dialog.findViewById(R.id.done);
        RecyclerView recyclerView=dialog.findViewById(R.id.filter_recycler);
        title.setText("My Address");


        CheckOutAddressCardViewAdapter  checkOutAddressCardViewAdapter=new CheckOutAddressCardViewAdapter(getContext(),addressInfoList,addressInfo);




        recyclerView.setAdapter(checkOutAddressCardViewAdapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setHasFixedSize(true);

        done.setOnClickListener(v -> {
           addressInfo= checkOutAddressCardViewAdapter.getSelectedItem();
            city.setText(addressInfo.getCity_en());
            governorate.setText(addressInfo.getGovernorate_en());
            address.setText(addressInfo.getAddress());
            CheckOutInfo checkOutInfo=userViewModel.getCheckOutInfo();
            checkOutInfo.setAddressId(addressInfo.getId());
            userViewModel.setCheckOutInfo(checkOutInfo);
            //shippingAmount.setText(addressInfo.getShippingAmount());
            dialog.dismiss();

        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();


    }











}
