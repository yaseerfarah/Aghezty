package com.example.aghezty.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.aghezty.View.Confirm;
import com.example.aghezty.View.Payment;
import com.example.aghezty.View.Shipping;

public class CheckOutViewPagerAdapter extends FragmentPagerAdapter {

    private Fragment[] checkOutFragment={
            new Shipping(),
            new Payment(),
            new Confirm()
    };

    public CheckOutViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return checkOutFragment[position];
    }

    @Override
    public int getCount() {
        return checkOutFragment.length;
    }
}
