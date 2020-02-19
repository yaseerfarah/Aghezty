package com.example.aghezty.Data;

import com.example.aghezty.POJO.HomeResponse;
import com.example.aghezty.POJO.ProductFilterResponse;
import com.example.aghezty.POJO.InnerProductResponse;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface AgheztyApi {


    @GET("home")
    Observable<Response<HomeResponse>> getHome();


    @GET("products")
    Observable<Response<ProductFilterResponse>> getSpecificProduct(
            @QueryMap Map<String, Object> options
    ,@Query("page") int page );


    @GET("inner_product/{id}")
    Single<Response<InnerProductResponse>> getInnerProductById(@Path("id") int id);



}
