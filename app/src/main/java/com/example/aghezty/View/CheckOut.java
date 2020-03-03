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

import com.badoualy.stepperindicator.StepperIndicator;
import com.example.aghezty.Adapter.CheckOutViewPagerAdapter;
import com.example.aghezty.POJO.UserInfo;
import com.example.aghezty.R;
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

    private CheckOutViewPagerAdapter checkOutViewPagerAdapter;

    @BindView(R.id.stepper_indicator)
    StepperIndicator stepperIndicator;

    @BindView(R.id.check_out_viewpager)
    ViewPager checkOutFragment;

    @BindView(R.id.next)
    Button next;
    @BindView(R.id.back)
    Button back;


    public CheckOut() {
        // Required empty public constructor
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

        checkOutViewPagerAdapter=new CheckOutViewPagerAdapter(getActivity().getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        checkOutFragment.setAdapter(checkOutViewPagerAdapter);

        stepperIndicator.setViewPager(checkOutFragment);

        checkOutFragment.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }
}
