package com.example.aghezty.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aghezty.POJO.AddressInfo;
import com.example.aghezty.POJO.FilterInfo;
import com.example.aghezty.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CheckOutAddressCardViewAdapter extends RecyclerView.Adapter<CheckOutAddressCardViewAdapter.Item_holder>  {

    private Context context;
    private List<AddressInfo> itemList;
    private AddressInfo selectedItem;
    private AddressInfo prevSelectedItem;
    private List<RadioButton> radioButtons;


    public CheckOutAddressCardViewAdapter(Context context, List<AddressInfo> itemList,  AddressInfo prevSelectedItem) {
        this.context = context;
        this.itemList = itemList;
        this.selectedItem = new AddressInfo();
        this.prevSelectedItem = prevSelectedItem;
        this.radioButtons = new ArrayList<>();
    }

    @NonNull
    @Override
    public Item_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=null;


            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_filter_view, parent, false);
            return new Item_holder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull Item_holder holder, int position) {



        holder.radioButton.setTag(new Integer(position));
        radioButtons.add(holder.radioButton);
        holder.checkBox.setVisibility(View.GONE);






        holder.radioButton.setText(itemList.get(holder.getAdapterPosition()).getGovernorate_en()+", "+itemList.get(holder.getAdapterPosition()).getCity_en()+", "+itemList.get(holder.getAdapterPosition()).getAddress());

        holder.radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                setRadioButtonsOff(holder.radioButton);
                selectedItem=itemList.get(holder.getAdapterPosition());
                prevSelectedItem=selectedItem;
            }

        });

        if (itemList.get(holder.getAdapterPosition()).getId()==prevSelectedItem.getId()){
            holder.radioButton.setChecked(true);
        }




    }


    public AddressInfo getSelectedItem() {
        return selectedItem;
    }

    @Override
    public int getItemCount() {

        return itemList.size();

    }


    private void setRadioButtonsOff(RadioButton radioButtonid){

        for (RadioButton radioButton:radioButtons){

            if ((int)radioButton.getTag()!=(int)radioButtonid.getTag())
                radioButton.setChecked(false);
        }

    }





    //////////////////////////////////////////////////////////
    public class Item_holder extends RecyclerView.ViewHolder{
       RadioButton radioButton;
       CheckBox checkBox;


        public Item_holder(View itemView) {
            super(itemView);
          radioButton=itemView.findViewById(R.id.order_item);
          checkBox=itemView.findViewById(R.id.filter_item);

        }
    }







}
