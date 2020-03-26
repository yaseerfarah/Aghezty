package com.example.aghezty.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.aghezty.POJO.CartInfo;
import com.example.aghezty.POJO.FilterInfo;
import com.example.aghezty.POJO.OrderInfo;
import com.example.aghezty.R;
import com.example.aghezty.Util.CartDiffUtil;
import com.example.aghezty.Util.OrderDiffUtil;
import com.example.aghezty.ViewModel.ProductViewModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.aghezty.Util.CartDiffUtil.DIFF_PRICE;
import static com.example.aghezty.Util.CartDiffUtil.DIFF_QUANTITY;
import static com.example.aghezty.Util.OrderDiffUtil.STATUS;
import static com.example.aghezty.ViewModel.ProductViewModel.ALL;


public class OrderCardViewAdapter extends RecyclerView.Adapter<OrderCardViewAdapter.Order_holder>  {

    private Context context;
    private List<OrderInfo> orderInfoList;


    public OrderCardViewAdapter(Context context, List<OrderInfo> orderInfoList) {
        this.context = context;
        this.orderInfoList = orderInfoList;
    }

    @NonNull
    @Override
    public Order_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_cardview, parent, false);
        return new Order_holder(view);


    }




    @Override
    public void onBindViewHolder(@NonNull Order_holder holder, int position, @NonNull List<Object> payloads) {

        if (payloads.size()>0){
            Bundle bundle=(Bundle)payloads.get(0);

            for (String key:bundle.keySet()){
                if (key.trim().matches(STATUS)){
                    holder.orderStatus.setText(bundle.getString(key));
                }else if (key.trim().matches(DIFF_QUANTITY)){
                    holder.totalPrice.setText(NumberFormat.getInstance(Locale.US).format(bundle.getInt(key)  +" "+context.getResources().getString(R.string.egp)));
                }
            }



        }else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull Order_holder holder, int position) {

        holder.orderID.setText(orderInfoList.get(holder.getAdapterPosition()).getOrderID());
        holder.paymentMethod.setText(orderInfoList.get(holder.getAdapterPosition()).getPaymentMethod());
        holder.orderStatus.setText(orderInfoList.get(holder.getAdapterPosition()).getOrderStatus());
        holder.orderTime.setText(orderInfoList.get(holder.getAdapterPosition()).getOrderTime());
        holder.shippingAmount.setText(orderInfoList.get(holder.getAdapterPosition()).getShippingAmount() +" "+context.getResources().getString(R.string.egp));
        holder.totalPrice.setText(NumberFormat.getInstance(Locale.US).format(orderInfoList.get(holder.getAdapterPosition()).getOrderTotalPrice()) +" "+context.getResources().getString(R.string.egp));

        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));
        holder.recyclerView.setAdapter(new OrderProductCardViewAdapter(context,orderInfoList.get(holder.getAdapterPosition()).getOrderProductList()));


    }






    @Override
    public int getItemCount() {
        return orderInfoList.size();
    }




    public void updateOrderList(List<OrderInfo> orderInfos){
        OrderDiffUtil orderDiffUtil=new OrderDiffUtil(context,this.orderInfoList,orderInfos);
        DiffUtil.DiffResult diffResult=DiffUtil.calculateDiff(orderDiffUtil);

        this.orderInfoList.clear();
        this.orderInfoList.addAll(orderInfos);
        diffResult.dispatchUpdatesTo(this);

    }




    //////////////////////////////////////////////////////////
    public class Order_holder extends RecyclerView.ViewHolder{
       TextView orderID,orderStatus,paymentMethod,orderTime,shippingAmount,totalPrice;
       RecyclerView recyclerView;



        public Order_holder(View itemView) {
            super(itemView);
          orderID=(TextView) itemView.findViewById(R.id.order_an_id);
          orderStatus=(TextView) itemView.findViewById(R.id.order_an_status);
          orderTime=(TextView) itemView.findViewById(R.id.order_an_time);
          paymentMethod=(TextView) itemView.findViewById(R.id.order_an_method);
          shippingAmount=(TextView) itemView.findViewById(R.id.shipping_price);
          totalPrice=(TextView) itemView.findViewById(R.id.order_an_total_price);
          recyclerView=(RecyclerView) itemView.findViewById(R.id.order_product_list);

        }
    }







}
