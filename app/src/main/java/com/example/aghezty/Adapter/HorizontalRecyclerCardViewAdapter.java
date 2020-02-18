package com.example.aghezty.Adapter;

import android.content.Context;
import android.content.res.Resources;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


import com.example.aghezty.POJO.HomeRecylerData;
import com.example.aghezty.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class HorizontalRecyclerCardViewAdapter extends RecyclerView.Adapter<HorizontalRecyclerCardViewAdapter.Recycler_holder>  {

    private Context context;
    private List<HomeRecylerData> homeRecylerDataList;
    private NavController navController;
    private Resources resources;
    private List<Timer> timers=new ArrayList<>();


    public HorizontalRecyclerCardViewAdapter(Context context, List<HomeRecylerData> homeRecylerDataList , NavController navController,Resources resources) {
        this.context = context;
        this.homeRecylerDataList=homeRecylerDataList;
        this.navController=navController;
        this.resources=resources;


    }

    @NonNull
    @Override
    public Recycler_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=null;


        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_recycler_card, parent, false);
        return new Recycler_holder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull Recycler_holder holder, int position) {


        holder.icon.setBackground(resources.getDrawable(homeRecylerDataList.get(holder.getAdapterPosition()).getIconId()));
        holder.title.setText(homeRecylerDataList.get(holder.getAdapterPosition()).getName());

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.recyclerView.setLayoutManager(linearLayoutManager);

        final ProductCardViewAdapter productCardViewAdapter=new ProductCardViewAdapter(context,homeRecylerDataList.get(holder.getAdapterPosition()).getProductInfos(),ProductCardViewAdapter.HOME,navController);
        holder.recyclerView.setAdapter(productCardViewAdapter);


        final int time = 4000; // it's the delay time for sliding between items in recyclerview

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {

                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() < (productCardViewAdapter.getItemCount() - 1 )&& holder.recyclerView!=null) {

                    linearLayoutManager.smoothScrollToPosition(holder.recyclerView, new RecyclerView.State(), linearLayoutManager.findLastCompletelyVisibleItemPosition() + 1);
                }

                else if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == (productCardViewAdapter.getItemCount() - 1)&& holder.recyclerView!=null) {

                    linearLayoutManager.smoothScrollToPosition(holder.recyclerView, new RecyclerView.State(), 0);
                }
            }
        }, 0, time);

        timers.add(timer);



    }



    @Override
    public int getItemCount() {
        return homeRecylerDataList.size();
    }


    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);


        for (Timer timer:timers){
            timer.cancel();
        }

    }

    //////////////////////////////////////////////////////////
    public class Recycler_holder extends RecyclerView.ViewHolder{
        ImageButton icon;
        TextView title;
        RecyclerView recyclerView;

        public Recycler_holder(View itemView) {
            super(itemView);
          icon=(ImageButton) itemView.findViewById(R.id.logo);
          title=(TextView) itemView.findViewById(R.id.title);
          recyclerView=(RecyclerView) itemView.findViewById(R.id.recycler);
        }
    }







}
