package com.example.aghezty.View;


import android.app.Activity;
import android.content.Intent;
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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.badoualy.stepperindicator.StepperIndicator;
import com.example.aghezty.Adapter.CheckOutViewPagerAdapter;
import com.example.aghezty.BroadcastReceiver.NetworkReceiver;
import com.example.aghezty.Constants;
import com.example.aghezty.Interface.CompletableListener;
import com.example.aghezty.Interface.InternetStatus;
import com.example.aghezty.Interface.PaypalSubmit;
import com.example.aghezty.POJO.UserInfo;
import com.example.aghezty.R;
import com.example.aghezty.Util.CheckOutViewPager;
import com.example.aghezty.ViewModel.UserViewModel;
import com.example.aghezty.ViewModel.ViewModelFactory;
import com.gturedi.views.CustomStateOptions;
import com.gturedi.views.StatefulLayout;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;
import es.dmoral.toasty.Toasty;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckOut extends Fragment implements InternetStatus , PaypalSubmit {

    private static final int PAYPAL_REQUEST_CODE =250 ;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Constants.PAYPAL_CLIENT_ID);


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


        checkOutViewPagerAdapter=new CheckOutViewPagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,checkOutFragment,this);

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

        //start paypal service
        Intent intent = new Intent(getContext(),PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        getActivity().startService(intent);

    }

    @Override
    public void onDestroy() {
        getActivity().stopService(new Intent(getContext(), PayPalService.class));
        super.onDestroy();
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == PAYPAL_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null){
                    try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        Log.e("Payment",paymentDetails);
                        submitOrder();
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)
                Toast.makeText(getContext(), "Cancel", Toast.LENGTH_SHORT).show();
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            Toast.makeText(getContext(), "Invalid", Toast.LENGTH_SHORT).show();

    }



    @Override
    public void Connect() {
        statefulLayout.showContent();
    }

    @Override
    public void notConnect() {
        statefulLayout.showCustom(networkCustom.message(getResources().getString(R.string.check_connection)));
    }

    @Override
    public void paypalSubmit(int total) {
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(total)),"USD",
                "Order Total Price",PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(getContext(), PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);
    }


    private void submitOrder(){

        statefulLayout.showLoading(" ");
        userViewModel.checkOut(new CompletableListener() {
            @Override
            public void onSuccess() {
                Toasty.success(getContext(),getResources().getString(R.string.successful_checkout),Toast.LENGTH_SHORT).show();
                statefulLayout.showContent();

                    checkOutFragment.setCurrentItem(3);


            }

            @Override
            public void onFailure(String message) {
                statefulLayout.showContent();


            }
        });

    }


}
