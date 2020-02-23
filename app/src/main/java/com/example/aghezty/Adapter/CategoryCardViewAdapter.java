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
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.aghezty.POJO.CategoryInfo;
import com.example.aghezty.POJO.FilterInfo;
import com.example.aghezty.R;
import com.example.aghezty.View.Filter;
import com.example.aghezty.ViewModel.ProductViewModel;

import java.util.ArrayList;
import java.util.List;


public class CategoryCardViewAdapter extends RecyclerView.Adapter<CategoryCardViewAdapter.Cat_holder>  {

    private Context context;
    private List<FilterInfo> categoryInfoList;
    static public   final int BEST_CATEGORIES=1;
    static public   final int CATEGORIES=2;
    private NavController navController;
    ProductViewModel productViewModel;
    private int type;


    public CategoryCardViewAdapter(Context context, List<FilterInfo> category, NavController navController, ProductViewModel productViewModel, int type) {
        this.context = context;
        this.categoryInfoList=category;
        this.type=type;
        this.productViewModel=productViewModel;
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


        Glide.with(context).load(categoryInfoList.get(holder.getAdapterPosition()).getImageUrl()).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                holder.imageView.setImageDrawable(resource);

            }
        });


        holder.textView.setText(categoryInfoList.get(holder.getAdapterPosition()).getName());


        holder.cardView.setOnClickListener(v -> {
            if (categoryInfoList.get(holder.getAdapterPosition()).getType()==FilterInfo.CATEGORY) {

                List<FilterInfo> categoryID = new ArrayList<>();
                categoryID.add(new FilterInfo(categoryInfoList.get(holder.getAdapterPosition()).getId(), categoryInfoList.get(holder.getAdapterPosition()).getName(), null, FilterInfo.CATEGORY));
                productViewModel.setFilter(categoryID, null, ProductViewModel.ALL, false);
                navController.navigate(R.id.action_global_productList);

            }else {

                List<FilterInfo> brandID = new ArrayList<>();
                brandID.add(new FilterInfo(categoryInfoList.get(holder.getAdapterPosition()).getId(), categoryInfoList.get(holder.getAdapterPosition()).getName(), null, FilterInfo.BRAND));
                productViewModel.setFilter(null, brandID, ProductViewModel.ALL, false);
                navController.navigate(R.id.action_global_productList);
            }
        });


    }






    @Override
    public int getItemCount() {
        return categoryInfoList.size();
    }







    //////////////////////////////////////////////////////////
    public class Cat_holder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView imageView;
        TextView textView;


        public Cat_holder(View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.cat_card);
          imageView=(ImageView) itemView.findViewById(R.id.category_image);
          textView=(TextView) itemView.findViewById(R.id.category_name);
        }
    }







}
