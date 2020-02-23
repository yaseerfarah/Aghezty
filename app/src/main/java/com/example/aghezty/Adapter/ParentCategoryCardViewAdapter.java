package com.example.aghezty.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.aghezty.Interface.OnParentCategoryClick;
import com.example.aghezty.POJO.CategoryInfo;
import com.example.aghezty.POJO.FilterInfo;
import com.example.aghezty.R;
import com.example.aghezty.View.Filter;
import com.example.aghezty.ViewModel.ProductViewModel;

import java.util.ArrayList;
import java.util.List;


public class ParentCategoryCardViewAdapter extends RecyclerView.Adapter<ParentCategoryCardViewAdapter.Cat_holder>  {

    private Context context;
    private List<FilterInfo> parentCategoryInfoList;
    private NavController navController;
    private OnParentCategoryClick onParentCategoryClick;

    private TextView lastPressed;


    public ParentCategoryCardViewAdapter(Context context, List<FilterInfo> parentCategoryInfoList, NavController navController, OnParentCategoryClick onParentCategoryClick) {
        this.context = context;
        this.parentCategoryInfoList = parentCategoryInfoList;
        this.navController = navController;
        this.onParentCategoryClick=onParentCategoryClick;
    }

    @NonNull
    @Override
    public Cat_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=null;

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent_category_card, parent, false);
            return new Cat_holder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull Cat_holder holder, int position) {

        if (position==0){

            holder.textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

            onParentCategoryClick.onClick(parentCategoryInfoList.get(holder.getAdapterPosition()).getId(),parentCategoryInfoList.get(holder.getAdapterPosition()).getType());
            lastPressed=holder.textView;

        }


        holder.textView.setText(parentCategoryInfoList.get(holder.getAdapterPosition()).getName());

        holder.relativeLayout.setOnClickListener(v -> {

            lastPressed.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            holder.textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

            lastPressed=holder.textView;

            onParentCategoryClick.onClick(parentCategoryInfoList.get(holder.getAdapterPosition()).getId(),parentCategoryInfoList.get(holder.getAdapterPosition()).getType());

        });


    }






    @Override
    public int getItemCount() {
        return parentCategoryInfoList.size();
    }







    //////////////////////////////////////////////////////////
    public class Cat_holder extends RecyclerView.ViewHolder{
        RelativeLayout relativeLayout;
        TextView textView;


        public Cat_holder(View itemView) {
            super(itemView);
            relativeLayout=itemView.findViewById(R.id.parent_category);
          textView=(TextView) itemView.findViewById(R.id.category_name);
        }
    }







}
