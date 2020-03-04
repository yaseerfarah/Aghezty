package com.example.aghezty.View;


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
import com.example.aghezty.POJO.UserInfo;
import com.example.aghezty.R;
import com.example.aghezty.Util.CheckOutViewPager;
import com.example.aghezty.ViewModel.UserViewModel;
import com.example.aghezty.ViewModel.ViewModelFactory;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckOut extends Fragment {

    @Inject
    ViewModelFactory viewModelFactory;
    private UserViewModel userViewModel;

    private UserInfo userInfo;

    private NavController navController;

    private Shipping shipping;
    private Payment payment;
    private Confirm confirm;

    private CheckOutViewPagerAdapter checkOutViewPagerAdapter;

    @BindView(R.id.stepper_indicator)
    StepperIndicator stepperIndicator;

    @BindView(R.id.check_out_viewpager)
    CheckOutViewPager checkOutFragment;

    @BindView(R.id.next)
    Button next;
    @BindView(R.id.back)
    Button back;


    public CheckOut() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        checkOutViewPagerAdapter=new CheckOutViewPagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,checkOutFragment);

        shipping=(Shipping) checkOutViewPagerAdapter.getItem(0);
        payment =(Payment) checkOutViewPagerAdapter.getItem(1);
        confirm =(Confirm) checkOutViewPagerAdapter.getItem(2);

        checkOutFragment.setAdapter(checkOutViewPagerAdapter);

        stepperIndicator.setViewPager(checkOutFragment);

    }

    @Override
    public void onStop() {
        super.onStop();
        checkOutFragment.setAdapter(null);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSupportInjection.inject(this);
        userViewModel= ViewModelProviders.of(this,viewModelFactory).get(UserViewModel.class);
        userInfo=userViewModel.getCurrentUserInfo();
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



        checkOutFragment.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position == 0) {
                    back.setVisibility(View.GONE);
                }else {
                    back.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        next.setOnClickListener(v -> {
            switch (checkOutFragment.getCurrentItem()){

                case 0:
                    shipping.validationFields();
                    break;

                case 1:
                    checkOutFragment.setCurrentItem(2);
                    break;

                case 2:
                    break;


            }
        });

        back.setOnClickListener(v -> {
            switch (checkOutFragment.getCurrentItem()){

                case 1:
                    checkOutFragment.setCurrentItem(0);
                    break;

                case 2:
                    checkOutFragment.setCurrentItem(1);
                    break;


            }
        });



    }
}
