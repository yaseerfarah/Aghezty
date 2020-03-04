package com.example.aghezty.Util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class CheckOutViewPager extends ViewPager {

    private boolean isPagingEnabled = false;

    public CheckOutViewPager(Context context) {
        super(context);
    }

    public CheckOutViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onInterceptTouchEvent(event);
    }


}