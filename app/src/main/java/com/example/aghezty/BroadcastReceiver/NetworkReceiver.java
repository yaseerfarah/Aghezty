package com.example.aghezty.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

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

            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){

                NetworkCapabilities networkCapabilities=cm.getNetworkCapabilities(cm.getActiveNetwork());

                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)&&networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
                {
                    this.networkConnection.Connect();
                }else {
                    this.networkConnection.notConnect();
                }

            }else {
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

                if (activeNetwork==null){
                    this.networkConnection.notConnect();
                }else {
                    this.networkConnection.Connect();
                }
            }

        }


    }
}
