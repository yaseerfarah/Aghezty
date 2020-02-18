package com.example.aghezty.Dagger2;


import android.content.Context;

import androidx.room.Room;

import com.example.aghezty.Data.AgheztyApi;

import java.nio.channels.NoConnectionPendingException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.aghezty.Constants.BASE_URL;


@Module
public class RepositoryModule {









    @Provides
    @Singleton
    public AgheztyApi agheztyApi(Retrofit retrofit){

        return retrofit.create(AgheztyApi.class);

    }


    @Singleton
    @Provides
    public Retrofit retrofit(){

        OkHttpClient client=new OkHttpClient()
                .newBuilder()
                .readTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(1,TimeUnit.MINUTES)
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }







}
