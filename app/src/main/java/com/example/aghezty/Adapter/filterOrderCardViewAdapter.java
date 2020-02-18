package com.example.aghezty.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.aghezty.POJO.CategoryInfo;
import com.example.aghezty.R;

import java.util.List;


public class filterOrderCardViewAdapter extends RecyclerView.Adapter<filterOrderCardViewAdapter.Item_holder>  {

    static public int FILTER=1;
    static public int ORDER=2;

    private Context context;
    private List<String> itemList;
    private List<String> itemListSelected;
    private int type;


    public filterOrderCardViewAdapter(Context context, List<String> itemList,int type) {
        this.context = context;
        this.itemList = itemList;
        this.type=type;
    }



    @NonNull
    @Override
    public Item_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=null;


            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.best_category_cardview, parent, false);
            return new Item_holder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull Item_holder holder, int position) {

        if (type==FILTER){

            holder.checkBox.setText(itemList.get(holder.getAdapterPosition()));
            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if (isChecked){

                    itemListSelected.add(itemList.get(holder.getAdapterPosition()));

                }else {
                    itemListSelected.remove(itemList.get(holder.getAdapterPosition()));
                }


            });


        }

    }


    public List<String> getItemListSelected() {
        return itemListSelected;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
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
