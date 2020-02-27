package com.example.aghezty.Data;

import com.example.aghezty.POJO.BrandCategoriesResponse;
import com.example.aghezty.POJO.CitiesResponse;
import com.example.aghezty.POJO.GovernorateResponse;
import com.example.aghezty.POJO.HomeResponse;
import com.example.aghezty.POJO.ParentCategoriesResponse;
import com.example.aghezty.POJO.ProductFilterResponse;
import com.example.aghezty.POJO.InnerProductResponse;
import com.example.aghezty.POJO.RegisterParentResponse;
import com.example.aghezty.POJO.RegisterResponse;
import com.example.aghezty.POJO.UserInfo;

import org.json.JSONObject;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface AgheztyApi {


    @GET("home")
    Observable<Response<HomeResponse>> getHome();

    @GET("categorys")
    Single<Response<ParentCategoriesResponse>> getParentCategories();

    @GET("brands")
    Single<Response<BrandCategoriesResponse>> getBrandCategories();

    @GET("products")
    Single<Response<ProductFilterResponse>> getSpecificProduct(
            @QueryMap Map<String, Object> options
    ,@Query("page") int page );


    @GET("inner_product/{id}")
    Single<Response<InnerProductResponse>> getInnerProductById(@Path("id") int id);


    @GET("governorate")
    Single<Response<GovernorateResponse>> getAllGovernorate();


    @GET("city")
    Single<Response<CitiesResponse>> getCitiesByGovernorateId(@Query("governorate_id") int id);


    @POST("register")
    Single<Response<ResponseBody>> registerNewUser(@Body UserInfo userInfo);



}
