package com.example.aghezty.Util;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.example.aghezty.POJO.CartInfo;
import com.example.aghezty.POJO.OrderInfo;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

/**
 * Created by DELL on 9/13/2019.
 */

public class OrderDiffUtil extends DiffUtil.Callback {

   public static final String SHIPPING_AMOUNT="Shipping_Amount";
    public static final String STATUS="Status";

    private List<OrderInfo> oldList;
    private List<OrderInfo> newList;

    Context context;

    public OrderDiffUtil(Context context, List<OrderInfo> oldList, List<OrderInfo> newList) {
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
        return oldList.get(oldItemPosition).getOrderID()==newList.get(newItemPosition).getOrderID();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).compare(newList.get(newItemPosition));
    }


    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {


        OrderInfo newModel = newList.get(newItemPosition);
        OrderInfo oldModel = oldList.get(oldItemPosition);

        Bundle diff = new Bundle();

        if (newModel.getShippingAmount()!=oldModel.getShippingAmount()) {
            diff.putInt(SHIPPING_AMOUNT, newModel.getShippingAmount());
        }else if (newModel.getOrderStatus().matches(oldModel.getOrderStatus())){
            diff.putString(STATUS,newModel.getOrderStatus());

        }

        if (diff.size() == 0) {
            return super.getChangePayload(oldItemPosition, newItemPosition);
        }
        return diff;

    }
}
