package com.example.aghezty.Util;

import android.content.res.Resources;
import android.graphics.Rect;

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
            spacing2=(displayWidth/2)-(card_width+spacing*2);
        }else {
            spacing2=(displayWidth/2)-(card_width+spacing*3);
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
                outRect.left = spacing*2;
                outRect.right = spacing;//(column + 1) * spacing / spanCount;
            }
        }
        else if (layoutView==Category) {
            if (column%2!=0){
                outRect.left = spacing2;
                outRect.right =  spacing2 ;
            }else {
                outRect.left = spacing;
                outRect.right = spacing;//(column + 1) * spacing / spanCount;
            }
        }

        outRect.top = spacing;



    }
}

