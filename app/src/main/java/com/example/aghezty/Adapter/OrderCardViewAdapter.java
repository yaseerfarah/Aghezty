package com.example.aghezty.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.aghezty.POJO.FilterInfo;
import com.example.aghezty.POJO.OrderInfo;
import com.example.aghezty.R;
import com.example.aghezty.ViewModel.ProductViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.best_category_cardview, parent, false);
        return new Order_holder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull Order_holder holder, int position) {

        holder.orderID.setText(orderInfoList.get(holder.getAdapterPosition()).getOrderID());
        holder.paymentMethod.setText(orderInfoList.get(holder.getAdapterPosition()).getPaymentMethod());
        holder.orderStatus.setText(orderInfoList.get(holder.getAdapterPosition()).getOrderStatus());
        holder.orderTime.setText(orderInfoList.get(holder.getAdapterPosition()).getOrderTime());
        holder.shippingAmount.setText(orderInfoList.get(holder.getAdapterPosition()).getShippingAmount());
        holder.totalPrice.setText(orderInfoList.get(holder.getAdapterPosition()).getOrderTotalPrice());

        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));
        holder.recyclerView.setAdapter(new OrderProductCardViewAdapter(context,orderInfoList.get(holder.getAdapterPosition()).getOrderProductList()));


    }






    @Override
    public int getItemCount() {
        return orderInfoList.size();
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

        }
    }







}
