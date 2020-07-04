package com.example.aghezty.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

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





    public CheckOutViewPagerAdapter(@NonNull FragmentManager fm, int behavior,CheckOutViewPager checkOutViewPager) {
        super(fm, behavior);

        this.viewPagerWeakReference=new WeakReference<>(checkOutViewPager);
        checkOutFragment.add(new CustomerInformation(viewPagerWeakReference));
        checkOutFragment.add(new Shipping(viewPagerWeakReference));
        checkOutFragment.add(new Payment(viewPagerWeakReference));
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
