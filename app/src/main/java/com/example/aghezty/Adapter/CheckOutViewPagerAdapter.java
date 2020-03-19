package com.example.aghezty.Adapter;

import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.aghezty.Interface.PaypalSubmit;
import com.example.aghezty.Util.CheckOutViewPager;
import com.example.aghezty.View.Confirm;
import com.example.aghezty.View.CustomerInformation;
import com.example.aghezty.View.Payment;
import com.example.aghezty.View.Shipping;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class CheckOutViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> checkOutFragment=new ArrayList<>();
    private WeakReference<CheckOutViewPager> viewPagerWeakReference;
    private PaypalSubmit paypalSubmit;





    public CheckOutViewPagerAdapter(@NonNull FragmentManager fm, int behavior,CheckOutViewPager checkOutViewPager,PaypalSubmit paypalSubmit) {
        super(fm, behavior);

        this.viewPagerWeakReference=new WeakReference<>(checkOutViewPager);
        this.paypalSubmit=paypalSubmit;
        checkOutFragment.add(new CustomerInformation(viewPagerWeakReference));
        checkOutFragment.add(new Shipping(viewPagerWeakReference));
        checkOutFragment.add(new Payment(viewPagerWeakReference,paypalSubmit));
        checkOutFragment.add(new Confirm(viewPagerWeakReference));
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return checkOutFragment.get(position);
    }

    @Override
    public int getCount() {
        return checkOutFragment.size();
    }



}
