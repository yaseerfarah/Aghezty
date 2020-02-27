package com.example.aghezty.Util;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.example.aghezty.POJO.CartInfo;

import java.util.List;

/**
 * Created by DELL on 9/13/2019.
 */

public class CartDiffUtil extends DiffUtil.Callback {

    public static final String DIFF_PRICE="Price";
    public static final String DIFF_QUANTITY="Quantity";

    private List<CartInfo> oldList;
    private List<CartInfo> newList;

    Context context;

    public CartDiffUtil(Context context, List<CartInfo> oldList, List<CartInfo> newList) {
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
        return oldList.get(oldItemPosition).getProduct_id()==newList.get(newItemPosition).getProduct_id();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).compare(newList.get(newItemPosition));
    }


    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {

        CartInfo newModel = newList.get(newItemPosition);
        CartInfo oldModel = oldList.get(oldItemPosition);

        Bundle diff = new Bundle();

        if (newModel.getPro_price()!=oldModel.getPro_price()) {
            diff.putInt(DIFF_PRICE, newModel.getPro_price());
        }else if (newModel.getQuantity()!=oldModel.getQuantity()){
            diff.putInt(DIFF_QUANTITY,newModel.getQuantity());

        }

        if (diff.size() == 0) {
            return super.getChangePayload(oldItemPosition, newItemPosition);
        }
        return diff;

    }
}
