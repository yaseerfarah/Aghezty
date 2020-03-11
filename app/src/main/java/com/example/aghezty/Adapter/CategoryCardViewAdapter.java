package com.example.aghezty.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.aghezty.POJO.CategoryInfo;
import com.example.aghezty.POJO.FilterInfo;
import com.example.aghezty.R;
import com.example.aghezty.View.Filter;
import com.example.aghezty.ViewModel.ProductViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.aghezty.ViewModel.ProductViewModel.ALL;


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

        holder.progressBar.setVisibility(View.VISIBLE);

        Glide.with(context).load(categoryInfoList.get(holder.getAdapterPosition()).getImageUrl())
                .apply(RequestOptions.timeoutOf(60*1000))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e(getClass().getName(),e.getMessage());
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.INVISIBLE);
                        return false;
                    }
                })
                .into(holder.imageView);


        holder.textView.setText(categoryInfoList.get(holder.getAdapterPosition()).getName());


        holder.cardView.setOnClickListener(v -> {
            if (categoryInfoList.get(holder.getAdapterPosition()).getType()==FilterInfo.CATEGORY) {

                List<FilterInfo> categoryID = new ArrayList<>();
                categoryID.add(new FilterInfo(categoryInfoList.get(holder.getAdapterPosition()).getId(), categoryInfoList.get(holder.getAdapterPosition()).getName_en(),categoryInfoList.get(holder.getAdapterPosition()).getName_ar(), null, FilterInfo.CATEGORY));
                productViewModel.setFilter(categoryID, null, ALL,ALL, false);
                navController.navigate(R.id.action_global_productList);

            }else {

                List<FilterInfo> brandID = new ArrayList<>();
                brandID.add(new FilterInfo(categoryInfoList.get(holder.getAdapterPosition()).getId(), categoryInfoList.get(holder.getAdapterPosition()).getName_en(),categoryInfoList.get(holder.getAdapterPosition()).getName_ar(), null, FilterInfo.BRAND));
                productViewModel.setFilter(null, brandID, ALL,ALL ,false);
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
        ProgressBar progressBar;


        public Cat_holder(View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.cat_card);
          imageView=(ImageView) itemView.findViewById(R.id.category_image);
          textView=(TextView) itemView.findViewById(R.id.category_name);
          progressBar=(ProgressBar) itemView.findViewById(R.id.progress);
        }
    }







}
