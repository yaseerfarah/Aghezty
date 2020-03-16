package com.example.aghezty.Util;

import android.content.res.Resources;
import android.graphics.Rect;

import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by DELL on 9/11/2019.
 */
public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {


    private int spanCount;
    private int spacing;
    private int spacing2;

    private int layoutView;
    private int displayWidth;
    private int card_width;
    public static final int HomeLayout=1;
    public static final int ListLayout=2;
    public static final int Category=3;



    public GridSpacingItemDecoration(int spanCount, int spacing,int layoutView,int displayWidth,int card_width) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.layoutView=layoutView;
        this.displayWidth=displayWidth;
        this.card_width=card_width;
        if(layoutView==ListLayout){
            spacing2=displayWidth-card_width;
        }else {
            int perfectSpacing=displayWidth-(card_width*spanCount);
            Log.e("display Width",String.valueOf(displayWidth));
            Log.e("Card Width",String.valueOf(card_width));
            Log.e("Spacing",String.valueOf(perfectSpacing/3));
            int frameWidth = (int) ((displayWidth - (float) perfectSpacing * (spanCount - 1)) / spanCount);
            int padding =  perfectSpacing/3;
            spacing2=padding;
        }


    }

    /**
     * Converting dp to pixel
     */
    public static int dpToPx(int dp,Resources resources) {
        Resources r = resources;
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column



        if (layoutView==ListLayout) {
            if (column%2==0){
                outRect.left = spacing2;
                outRect.right =  spacing2 ;
            }else {
                outRect.left = spacing2/2;
                outRect.right = spacing2/2;//(column + 1) * spacing / spanCount;
            }
        }
        else if (layoutView==Category) {
            if (column%2!=0){
                outRect.left = spacing2/2;
                outRect.right =  spacing2/2 ;
            }else {
                outRect.left = spacing2;
                outRect.right = spacing2;//(column + 1) * spacing / spanCount;
            }
        }


        outRect.top = spacing;



    }
}

