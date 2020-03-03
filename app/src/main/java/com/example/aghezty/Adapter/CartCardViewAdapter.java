package com.example.aghezty.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aghezty.POJO.CartInfo;
import com.example.aghezty.R;
import com.example.aghezty.Util.CartDiffUtil;
import com.example.aghezty.ViewModel.UserViewModel;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import static com.example.aghezty.Util.CartDiffUtil.DIFF_PRICE;
import static com.example.aghezty.Util.CartDiffUtil.DIFF_QUANTITY;

/**
 * Created by DELL on 1/17/2019.
 */

public class CartCardViewAdapter extends RecyclerView.Adapter<CartCardViewAdapter.Pro_holder> {

    private Context context;
    private List<CartInfo> cartsInfoList;
    private UserViewModel userViewModel;


    public CartCardViewAdapter(Context context, List<CartInfo> cartsInfoList, UserViewModel userViewModel) {
        this.context = context;
        this.cartsInfoList = cartsInfoList;
        this.userViewModel = userViewModel;
    }

    @NonNull
    @Override
    public Pro_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

             view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_cardview, parent, false);

        return new Pro_holder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull Pro_holder holder, int position, @NonNull List<Object> payloads) {

        if (payloads.size()>0){
            Bundle bundle=(Bundle)payloads.get(0);

            for (String key:bundle.keySet()){
                if (key.trim().matches(DIFF_PRICE)){
                    holder.price.setText(String.valueOf(NumberFormat.getInstance(Locale.US).format(cartsInfoList.get(holder.getAdapterPosition()).getPro_price())));
                }else if (key.trim().matches(DIFF_QUANTITY)){
                    holder.quantity.setText(String.valueOf(cartsInfoList.get(holder.getAdapterPosition()).getQuantity()));
                }
            }



        }else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final Pro_holder holder, final int position) {

        holder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userViewModel.deleteCartInfo(cartsInfoList.get(holder.getAdapterPosition()));
            }
        });

        Glide.with(context).load(cartsInfoList.get(holder.getAdapterPosition()).getPro_imageUrl()).into(holder.imageView);

        holder.title.setText(cartsInfoList.get(holder.getAdapterPosition()).getPro_name());
        holder.price.setText(String.valueOf(NumberFormat.getInstance(Locale.US).format(cartsInfoList.get(holder.getAdapterPosition()).getPro_price())));
        holder.quantity.setText(String.valueOf(cartsInfoList.get(holder.getAdapterPosition()).getQuantity()));


        holder.increase.setOnClickListener(v -> {

            CartInfo cartInfo=cartsInfoList.get(holder.getAdapterPosition());
            cartInfo.setQuantity(cartsInfoList.get(holder.getAdapterPosition()).getQuantity()+1);
            userViewModel.updateCartInfo(cartInfo,holder.getAdapterPosition());


            holder.quantity.setText(String.valueOf(cartsInfoList.get(holder.getAdapterPosition()).getQuantity()));

        });

        holder.decrease.setOnClickListener(v -> {

            if (cartsInfoList.get(holder.getAdapterPosition()).getQuantity()>1){

                CartInfo cartInfo=cartsInfoList.get(holder.getAdapterPosition());
                cartInfo.setQuantity(cartsInfoList.get(holder.getAdapterPosition()).getQuantity()-1);
                userViewModel.updateCartInfo(cartInfo,holder.getAdapterPosition());

                holder.quantity.setText(String.valueOf(cartsInfoList.get(holder.getAdapterPosition()).getQuantity()));
            }

        });

    }

    @Override
    public int getItemCount() {
        return cartsInfoList.size();
    }




    public void updateCartList(List<CartInfo> cartInfos){
        CartDiffUtil cartDiffUtil=new CartDiffUtil(context,this.cartsInfoList,cartInfos);
        DiffUtil.DiffResult diffResult=DiffUtil.calculateDiff(cartDiffUtil);

        this.cartsInfoList.clear();
        this.cartsInfoList.addAll(cartInfos);
        diffResult.dispatchUpdatesTo(this);

    }




    //////////////////////////////////////////////////////////
    public class Pro_holder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView title,quantity,price;
        ImageButton close,increase,decrease;

        public Pro_holder(View itemView) {
            super(itemView);
            close=(ImageButton) itemView.findViewById(R.id.card_close);
           imageView =(ImageView) itemView.findViewById(R.id.card_image);
           increase=(ImageButton) itemView.findViewById(R.id.increase);
           decrease=(ImageButton) itemView.findViewById(R.id.decrease);
           title=(TextView) itemView.findViewById(R.id.card_title);
            quantity=(TextView) itemView.findViewById(R.id.q_text);
            price=(TextView) itemView.findViewById(R.id.card_price);

        }
    }


}
