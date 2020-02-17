package com.example.aghezty.Adapter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.aghezty.POJO.ProductInfo;
import com.example.aghezty.R;
import com.example.aghezty.View.Home;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by DELL on 1/17/2019.
 */

public class ProductCardViewAdapter extends RecyclerView.Adapter<ProductCardViewAdapter.Pro_holder> {


    static public   final int HOME=1;
    static public   final int LIST=2;
    private Context context;
    private List<ProductInfo> products;
    int type;
    private NavController navController;


    public ProductCardViewAdapter(Context context, List<ProductInfo> products, int type, NavController navController) {
        this.context = context;
        this.products = products;
        this.type = type;
        this.navController = navController;
    }

    @NonNull
    @Override
    public Pro_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if(type== HOME) {
             view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_cardview, parent, false);

        }else {
             view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cardview, parent, false);


        }

        return new Pro_holder(view);
    }





    @Override
    public void onBindViewHolder(@NonNull final Pro_holder holder, final int position) {



      /*  if (Integer.valueOf(products.get(holder.getAdapterPosition()).getStock())==0){
            holder.sold_layout.setVisibility(View.VISIBLE);
            holder.soldOut.setVisibility(View.VISIBLE);
        }*/


        Glide.with(context).load(products.get(holder.getAdapterPosition()).getMain_image())
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        holder.progressBar.setVisibility(View.INVISIBLE);
                        holder.pro_image.setImageDrawable(resource);

                    }
                });


        if(type==HOME){
            if(products.get(holder.getAdapterPosition()).getTitle_en().trim().toCharArray().length>10){

                String title="";

                for(int i=0;i<10;i++){
                    title+=products.get(holder.getAdapterPosition()).getTitle_en().toString().charAt(i);
                }
                holder.name.setText(title+"...");
            }
            else {
                holder.name.setText(products.get(holder.getAdapterPosition()).getTitle_en());
            }
        }
        else {

            holder.name.setText(products.get(holder.getAdapterPosition()).getTitle_en());
        }






        if(products.get(holder.getAdapterPosition()).getDiscount()!=null){

            holder.ex_price.setText(String.valueOf(products.get(holder.getAdapterPosition()).getPrice()));
            holder.price.setText(String.valueOf(products.get(holder.getAdapterPosition()).getPrice_after_discount()));
            holder.discount.setText(String.valueOf((int)((100*Integer.valueOf(products.get(holder.getAdapterPosition()).getDiscount()))/products.get(holder.getAdapterPosition()).getPrice()))+"%");
        }else {
            holder.discountLayout.setVisibility(View.GONE);
            holder.price.setText(String.valueOf(products.get(holder.getAdapterPosition()).getPrice()));

        }


        if(products.get(holder.getAdapterPosition()).getStars()>0){
            holder.evaluation.setText(String.valueOf(products.get(holder.getAdapterPosition()).getStars()));
        }else {
            holder.evaluationLayout.setVisibility(View.GONE);
        }




        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // Go To Product Details

            }
        });



    }



    @Override
    public int getItemCount() {
        return products.size();
    }











    //////////////////////////////////////////////////////////
    public class Pro_holder extends RecyclerView.ViewHolder{

        ImageView pro_image;
        TextView name,price,ex_price,discount,evaluation;

        RelativeLayout discountLayout,evaluationLayout;
        CardView cardView;
        ProgressBar progressBar;



        public Pro_holder(View itemView) {

            super(itemView);
            cardView=(CardView) itemView.findViewById(R.id.card_view);
            pro_image=(ImageView)itemView.findViewById(R.id.pro_image);
            name=(TextView)itemView.findViewById(R.id.pro_name);
            ex_price=(TextView)itemView.findViewById(R.id.pro_exprice);
            discount=(TextView)itemView.findViewById(R.id.pro_discount);
            price=(TextView)itemView.findViewById(R.id.pro_price);
            evaluation=(TextView)itemView.findViewById(R.id.pro_evaluation);
            discountLayout=(RelativeLayout)itemView.findViewById(R.id.ex_dis);
            evaluationLayout=(RelativeLayout)itemView.findViewById(R.id.evaluation);
            progressBar=(ProgressBar)itemView.findViewById(R.id.progress);



        }
    }


}
