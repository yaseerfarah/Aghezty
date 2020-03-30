package com.example.aghezty.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.aghezty.Interface.OnParentCategoryClick;
import com.example.aghezty.POJO.FilterInfo;
import com.example.aghezty.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;


public class FilterParentCategoryCardViewAdapter extends RecyclerView.Adapter<FilterParentCategoryCardViewAdapter.Cat_holder>  {

    private Context context;
    private List<FilterInfo> filterList;
    private OnParentCategoryClick onParentCategoryClick;




    public FilterParentCategoryCardViewAdapter(Context context, List<FilterInfo> filterList, OnParentCategoryClick onParentCategoryClick) {
        this.context = context;
        this.filterList=filterList;
        this.onParentCategoryClick=onParentCategoryClick;
    }

    @NonNull
    @Override
    public Cat_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=null;

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_card_view, parent, false);
            return new Cat_holder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull Cat_holder holder, int position) {



        holder.textView.setText(filterList.get(holder.getAdapterPosition()).getName());

        holder.relativeLayout.setOnClickListener(v -> {

            onParentCategoryClick.onClick(filterList.get(holder.getAdapterPosition()));

        });


    }






    @Override
    public int getItemCount() {
        return filterList.size();
    }







    //////////////////////////////////////////////////////////
    public class Cat_holder extends RecyclerView.ViewHolder{
        RelativeLayout relativeLayout;
        TextView textView;


        public Cat_holder(View itemView) {
            super(itemView);
            relativeLayout=itemView.findViewById(R.id.sub_cat);
          textView=(TextView) itemView.findViewById(R.id.sub_cat_name);
        }
    }







}
