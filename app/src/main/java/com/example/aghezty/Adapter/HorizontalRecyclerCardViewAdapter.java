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
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


import com.example.aghezty.POJO.HomeRecylerData;
import com.example.aghezty.R;

import java.util.List;


public class HorizontalRecyclerCardViewAdapter extends RecyclerView.Adapter<HorizontalRecyclerCardViewAdapter.Recycler_holder>  {

    private Context context;
    private List<HomeRecylerData> homeRecylerDataList;
    private NavController navController;
    private Resources resources;


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
        holder.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        holder.recyclerView.setAdapter(new ProductCardViewAdapter(context,homeRecylerDataList.get(holder.getAdapterPosition()).getProductInfos(),ProductCardViewAdapter.HOME,navController));

    }



    @Override
    public int getItemCount() {
        return homeRecylerDataList.size();
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
