package com.example.aghezty.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.aghezty.Interface.InternetStatus;


public class NetworkReceiver extends BroadcastReceiver {


    InternetStatus networkConnection;


    public NetworkReceiver(InternetStatus networkConnection) {
        this.networkConnection = networkConnection;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

         if(intent.getAction().trim().matches("android.net.conn.CONNECTIVITY_CHANGE")){

            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

            if (activeNetwork==null){
                this.networkConnection.notConnect();


            }else {
                this.networkConnection.Connect();
            }



        }


    }
}
