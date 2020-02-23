package com.example.aghezty.Util;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.example.aghezty.POJO.ProductInfo;

import java.util.List;


/**
 * Created by DELL on 9/13/2019.
 */

public class ProductDiffUtil extends DiffUtil.Callback {

    private List<ProductInfo> oldList;
    private List<ProductInfo> newList;

    Context context;

    public ProductDiffUtil(Context context, List<ProductInfo> oldList, List<ProductInfo> newList) {
        this.oldList = oldList;
        this.newList = newList;
        this.context=context;
    }



    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getId()==newList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).compare(newList.get(newItemPosition));
    }


    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
