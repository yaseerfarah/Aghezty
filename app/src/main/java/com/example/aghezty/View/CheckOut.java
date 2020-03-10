package com.example.aghezty.View;


import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.badoualy.stepperindicator.StepperIndicator;
import com.example.aghezty.Adapter.CheckOutViewPagerAdapter;
import com.example.aghezty.BroadcastReceiver.NetworkReceiver;
import com.example.aghezty.Interface.InternetStatus;
import com.example.aghezty.POJO.UserInfo;
import com.example.aghezty.R;
import com.example.aghezty.Util.CheckOutViewPager;
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
public class CheckOut extends Fragment implements InternetStatus {

    @Inject
    ViewModelFactory viewModelFactory;
    private UserViewModel userViewModel;

    private UserInfo userInfo;

    private NavController navController;

    private CustomerInformation customerInformation;
    private Shipping shipping;
    private Payment payment;
    private Confirm confirm;

    private CheckOutViewPagerAdapter checkOutViewPagerAdapter;

    @BindView(R.id.stepper_indicator)
    StepperIndicator stepperIndicator;

    @BindView(R.id.check_out_viewpager)
    CheckOutViewPager checkOutFragment;

    @BindView(R.id.stateful)
    StatefulLayout statefulLayout;


    private NetworkReceiver networkReceiver;
    private CustomStateOptions networkCustom=new CustomStateOptions().image(R.drawable.ic_signal_wifi_off_black_24dp);


    public CheckOut() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        IntentFilter netFilter=new IntentFilter();
        netFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        getActivity().registerReceiver(networkReceiver,netFilter);


        checkOutViewPagerAdapter=new CheckOutViewPagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,checkOutFragment);

        customerInformation=(CustomerInformation) checkOutViewPagerAdapter.getItem(0);
        shipping=(Shipping) checkOutViewPagerAdapter.getItem(1);
        payment =(Payment) checkOutViewPagerAdapter.getItem(2);
        confirm =(Confirm) checkOutViewPagerAdapter.getItem(3);

        checkOutFragment.setAdapter(checkOutViewPagerAdapter);

        stepperIndicator.setViewPager(checkOutFragment);

    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(networkReceiver);
        checkOutFragment.setAdapter(null);


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
        return inflater.inflate(R.layout.fragment_check_out, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        navController= Navigation.findNavController(view);


    }

    @Override
    public void Connect() {
        statefulLayout.showContent();
    }

    @Override
    public void notConnect() {
        statefulLayout.showCustom(networkCustom.message(getResources().getString(R.string.check_connection)));
    }
}
