package com.example.aghezty.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.aghezty.POJO.CategoryInfo;
import com.example.aghezty.R;

import java.util.List;


public class CategoryCardViewAdapter extends RecyclerView.Adapter<CategoryCardViewAdapter.Cat_holder>  {

    private Context context;
    private List<CategoryInfo> categoryInfoList;
    static public   final int BEST_CATEGORIES=1;
    static public   final int CATEGORIES=2;
    private NavController navController;
    private int type;


    public CategoryCardViewAdapter(Context context, List<CategoryInfo> category, NavController navController,int type) {
        this.context = context;
        this.categoryInfoList=category;
        this.type=type;
        this.navController=navController;


    }

    @NonNull
    @Override
    public Cat_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=null;
        if (type==BEST_CATEGORIES){

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.best_category_cardview, parent, false);
            return new Cat_holder(view);
        }
        else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card, parent, false);
            return new Cat_holder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull Cat_holder holder, int position) {


        Glide.with(context).load(categoryInfoList.get(holder.getAdapterPosition()).getImage()).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                holder.imageView.setImageDrawable(resource);

            }
        });


        holder.textView.setText(categoryInfoList.get(holder.getAdapterPosition()).getTitle_en());

    }






    @Override
    public int getItemCount() {
        return categoryInfoList.size();
    }







    //////////////////////////////////////////////////////////
    public class Cat_holder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;


        public Cat_holder(View itemView) {
            super(itemView);
          imageView=(ImageView) itemView.findViewById(R.id.category_image);
          textView=(TextView) itemView.findViewById(R.id.category_name);
        }
    }







}
