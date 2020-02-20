package com.example.aghezty.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aghezty.POJO.FilterInfo;
import com.example.aghezty.R;
import com.example.aghezty.View.Filter;

import java.util.ArrayList;
import java.util.List;


public class FilterOrderCardViewAdapter extends RecyclerView.Adapter<FilterOrderCardViewAdapter.Item_holder>  {

    static public int FILTER=1;
    static public int ORDER=2;

    private Context context;
    private List<FilterInfo> itemList;
    private List<FilterInfo> itemListSelected;
    private List<FilterInfo> itemListPrevSelected;
    private List<RadioButton> radioButtons;
    private int type;


    public FilterOrderCardViewAdapter(Context context, List<FilterInfo> itemList,List<FilterInfo> itemListPrevSelected, int type) {
        this.context = context;
        this.itemList = itemList;
        this.type=type;
        this.itemListSelected=new ArrayList<>();
        this.radioButtons=new ArrayList<>();
        this.itemListPrevSelected=itemListPrevSelected;
        this.itemListSelected.addAll(itemListPrevSelected);
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

        if (type==FILTER){

            for (FilterInfo filterInfo:itemListPrevSelected){

                if (itemList.get(holder.getAdapterPosition()).getName().matches(filterInfo.getName())){
                    holder.checkBox.setChecked(true);
                }

            }


            holder.radioButton.setVisibility(View.GONE);
            holder.checkBox.setText(itemList.get(holder.getAdapterPosition()).getName());
            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if (isChecked){

                    itemListSelected.add(itemList.get(holder.getAdapterPosition()));

                    //Toast.makeText(context,String.valueOf(itemListSelected.size()),Toast.LENGTH_SHORT).show();
                }else {

                    removeitem(itemList.get(holder.getAdapterPosition()));
                    //itemListSelected.remove(itemList.get(holder.getAdapterPosition()));
                    ///Toast.makeText(context,String.valueOf(itemListSelected.size()),Toast.LENGTH_SHORT).show();
                }


            });


        }else {


            holder.radioButton.setTag(new Integer(position));
            radioButtons.add(holder.radioButton);
            holder.checkBox.setVisibility(View.GONE);

            for (FilterInfo filterInfo:itemListPrevSelected){

                if (itemList.get(holder.getAdapterPosition()).getName().matches(filterInfo.getName())){
                    holder.radioButton.setChecked(true);
                }
            }

            holder.radioButton.setText(itemList.get(holder.getAdapterPosition()).getName());

            holder.radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked){
                    setRadioButtonsOff(holder.radioButton);
                    itemListSelected.clear();
                    itemListSelected.add(itemList.get(holder.getAdapterPosition()));
                }

            });

        }

    }


    public List<FilterInfo> getItemListSelected() {
        return itemListSelected;
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


    private void removeitem(FilterInfo filterInfo){

        for(int i=0;i<itemListSelected.size();i++){
            if (itemListSelected.get(i).getId()==filterInfo.getId()){
                itemListSelected.remove(i);
            }

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
