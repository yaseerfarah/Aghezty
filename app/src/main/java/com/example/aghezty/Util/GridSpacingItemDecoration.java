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

    private int layoutView;
    public static final int HomeLayout=1;
    public static final int ListLayout=2;

    public GridSpacingItemDecoration(int spanCount, int spacing, int layoutView) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.layoutView=layoutView;
    }


    /**
     * Converting dp to pixel
     */
    public static int dpToPx(int dp, Resources resources) {
        Resources r = resources;
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column



        if (layoutView==HomeLayout) {
            outRect.left = spacing; // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)
        }
        else {
            outRect.left = spacing; // spacing - column * ((1f / spanCount) * spacing)

        }

        outRect.top = spacing;



    }
}

