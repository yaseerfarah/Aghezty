package com.example.aghezty.Data;

import android.content.SharedPreferences;

import com.example.aghezty.POJO.UserInfo;
import com.google.gson.Gson;

import javax.inject.Inject;

import static com.example.aghezty.Constants.USER_INFO;


/**
 * Created by DELL on 7/31/2019.
 */

public class SharedPreferencesMethod {


    private SharedPreferences sharedPreferences;

    @Inject
    public SharedPreferencesMethod(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }


    public void setBoolean(String name, boolean b){

        sharedPreferences.edit().putBoolean(name,b).apply();

    }


    public boolean getBoolean(String name){

        return sharedPreferences.getBoolean(name,false);
    }



    public void setString(String name, String value){

        sharedPreferences.edit().putString(name,value).apply();

    }



    public String getString(String name){

        return sharedPreferences.getString(name,"");
    }



    public void setObject(String name, Object object){

        Gson gson=new Gson();

        String json=gson.toJson(object);

        sharedPreferences.edit().putString(name,json).apply();

    }


    public Object getObject(String name, Class className){

        Gson gson=new Gson();

        String json=sharedPreferences.getString(name,"");

        return gson.fromJson(json,className);

    }


    public UserInfo getUserInfo(){

        Gson gson=new Gson();

        String json=sharedPreferences.getString(USER_INFO,"");

        return gson.fromJson(json,UserInfo.class);

    }


}
