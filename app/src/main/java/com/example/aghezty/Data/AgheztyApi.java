package com.example.aghezty.Data;

import com.example.aghezty.POJO.HomeResponse;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;

public interface AgheztyApi {


    @GET("home")
    Observable<Response<HomeResponse>> getHome();




}
