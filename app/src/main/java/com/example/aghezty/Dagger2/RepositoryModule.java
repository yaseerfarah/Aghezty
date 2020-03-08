package com.example.aghezty.Dagger2;


import android.content.Context;
import android.content.SharedPreferences;

import androidx.room.Room;

import com.example.aghezty.Data.AgheztyApi;
import com.example.aghezty.Data.CartInfoRoomMethod;
import com.example.aghezty.Data.CartRoomDatabase;

import java.io.IOException;
import java.nio.channels.NoConnectionPendingException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.aghezty.Constants.BASE_URL;
import static com.example.aghezty.Constants.DB_CART_NAME;
import static com.example.aghezty.Constants.USER_FILE_NAME;
import static com.example.aghezty.Constants.USER_TOKEN;


@Module
public class RepositoryModule {






    @Provides
    @Singleton
    public CartInfoRoomMethod cartInfoRoomMethod(Context context, CartRoomDatabase cartRoomDatabase){

        return new CartInfoRoomMethod(context,cartRoomDatabase);

    }



    @Provides
    @Singleton
    public CartRoomDatabase cartRoomDatabase(Context context){

        return Room.databaseBuilder(context, CartRoomDatabase.class, DB_CART_NAME)
                .build();

    }


    @Provides
    @Singleton
    public SharedPreferences sharedPreferences(Context context){

        return context.getSharedPreferences(USER_FILE_NAME,Context.MODE_PRIVATE);
    }


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
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request.Builder header = request.newBuilder().header("Authorization", "Bearer "+USER_TOKEN).header("Accept", "application/json").header("Content-Type", "application/json");


                        return chain.proceed(header.build());
                    }
                })
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
