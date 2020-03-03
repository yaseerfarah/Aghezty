package com.example.aghezty.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aghezty.Interface.CompletableListener;
import com.example.aghezty.POJO.AddressInfo;
import com.example.aghezty.POJO.CartInfo;
import com.example.aghezty.R;
import com.example.aghezty.Util.AddressDiffUtil;
import com.example.aghezty.Util.CartDiffUtil;
import com.example.aghezty.ViewModel.UserViewModel;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

import static com.example.aghezty.Util.AddressDiffUtil.DIFF_ADDRESS;
import static com.example.aghezty.Util.AddressDiffUtil.DIFF_CITY;
import static com.example.aghezty.Util.AddressDiffUtil.DIFF_GOVERNORATE;
import static com.example.aghezty.Util.CartDiffUtil.DIFF_PRICE;
import static com.example.aghezty.Util.CartDiffUtil.DIFF_QUANTITY;
import static com.example.aghezty.View.AddNewAddress.UPDATE_ADDRESS;

/**
 * Created by DELL on 1/17/2019.
 */

public class AddressCardViewAdapter extends RecyclerView.Adapter<AddressCardViewAdapter.Pro_holder> {

    private Context context;
    private List<AddressInfo> addressInfoList;
   private NavController navController;
   private UserViewModel userViewModel;


    public AddressCardViewAdapter(Context context, List<AddressInfo> addressInfoList, NavController navController, UserViewModel userViewModel) {
        this.context = context;
        this.addressInfoList = addressInfoList;
        this.navController = navController;
        this.userViewModel = userViewModel;
    }

    @NonNull
    @Override
    public Pro_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

             view = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_cardview, parent, false);

        return new Pro_holder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull Pro_holder holder, int position, @NonNull List<Object> payloads) {

        if (payloads.size()>0){
            Bundle bundle=(Bundle)payloads.get(0);

            for (String key:bundle.keySet()){
                if (key.trim().matches(DIFF_ADDRESS)){
                    holder.address.setText(bundle.getString(DIFF_ADDRESS));
                }else if (key.trim().matches(DIFF_CITY)){
                    holder.city.setText(bundle.getString(DIFF_CITY));
                }
                else if (key.trim().matches(DIFF_GOVERNORATE)){
                   holder.governorate.setText(bundle.getString(DIFF_GOVERNORATE));
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
                userViewModel.deleteAddress(addressInfoList.get(holder.getAdapterPosition()), holder.getAdapterPosition(), new CompletableListener() {
                    @Override
                    public void onSuccess() {
                        Toasty.success(context,"Successful Delete Address", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
            }
        });


        holder.address.setText(addressInfoList.get(holder.getAdapterPosition()).getAddress());
        holder.city.setText(addressInfoList.get(holder.getAdapterPosition()).getCity_en());
        holder.governorate.setText(addressInfoList.get(holder.getAdapterPosition()).getGovernorate_en());
        holder.address_num.setText(String.valueOf(holder.getAdapterPosition()+1));
        holder.cardView.setOnClickListener(v -> {
            Bundle bundle=new Bundle();
            bundle.putParcelable(UPDATE_ADDRESS,addressInfoList.get(holder.getAdapterPosition()));
            navController.navigate(R.id.action_myAddresses_to_addNewAddress,bundle);

        });


    }

    @Override
    public int getItemCount() {
        return addressInfoList.size();
    }




    public void updateAddressList(List<AddressInfo> addressInfos){
        AddressDiffUtil addressDiffUtil=new AddressDiffUtil(context,this.addressInfoList,addressInfos);
        DiffUtil.DiffResult diffResult=DiffUtil.calculateDiff(addressDiffUtil);

        this.addressInfoList.clear();
        this.addressInfoList.addAll(addressInfos);
        diffResult.dispatchUpdatesTo(this);

    }




    //////////////////////////////////////////////////////////
    public class Pro_holder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView address,city,governorate,address_num;
        ImageButton close;

        public Pro_holder(View itemView) {
            super(itemView);
            cardView=(CardView) itemView.findViewById(R.id.card_view);
            close=(ImageButton) itemView.findViewById(R.id.card_close);
           address=(TextView) itemView.findViewById(R.id.card_an_address);
            city=(TextView) itemView.findViewById(R.id.card_an_city);
            governorate=(TextView) itemView.findViewById(R.id.card_an_governorate);
            address_num=(TextView) itemView.findViewById(R.id.card_an_address_num);

        }
    }


}
