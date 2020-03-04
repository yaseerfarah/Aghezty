package com.example.aghezty.Adapter;

import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.aghezty.Util.CheckOutViewPager;
import com.example.aghezty.View.Confirm;
import com.example.aghezty.View.Payment;
import com.example.aghezty.View.Shipping;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class CheckOutViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> checkOutFragment=new ArrayList<>();
    private WeakReference<CheckOutViewPager> viewPagerWeakReference;



    public CheckOutViewPagerAdapter(@NonNull FragmentManager fm, int behavior,ViewPager viewPager) {
        super(fm, behavior);
        this.viewPagerWeakReference=new WeakReference(viewPager);
        checkOutFragment.add(new Shipping(viewPagerWeakReference));
        checkOutFragment.add(new Payment());
        checkOutFragment.add(new Confirm());
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
