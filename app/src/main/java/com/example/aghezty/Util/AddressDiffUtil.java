package com.example.aghezty.Util;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.example.aghezty.POJO.AddressInfo;
import com.example.aghezty.POJO.CartInfo;

import java.util.List;

/**
 * Created by DELL on 9/13/2019.
 */

public class AddressDiffUtil extends DiffUtil.Callback {

    public static final String DIFF_ADDRESS="Address";
    public static final String DIFF_CITY="City";
    public static final String DIFF_GOVERNORATE="Governorate";


    private List<AddressInfo> oldList;
    private List<AddressInfo> newList;

    Context context;

    public AddressDiffUtil(Context context, List<AddressInfo> oldList, List<AddressInfo> newList) {
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

        Toast.makeText(context,"hi",Toast.LENGTH_SHORT).show();
        AddressInfo newModel = newList.get(newItemPosition);
        AddressInfo oldModel = oldList.get(oldItemPosition);

        Bundle diff = new Bundle();

        if (newModel.getAddress()!=oldModel.getAddress()) {
            diff.putString(DIFF_ADDRESS, newModel.getAddress());
        }else if (newModel.getCity_en().matches(oldModel.getCity_en())){
            diff.putString(DIFF_CITY,newModel.getCity_en());

        }else if (newModel.getGovernorate_en().matches(oldModel.getGovernorate_en())){
            diff.putString(DIFF_GOVERNORATE,newModel.getGovernorate_en());

        }

        if (diff.size() == 0) {
            return super.getChangePayload(oldItemPosition, newItemPosition);
        }
        return diff;

    }
}
