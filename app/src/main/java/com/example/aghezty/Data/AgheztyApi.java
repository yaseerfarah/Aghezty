package com.example.aghezty.Data;

import com.example.aghezty.POJO.AddressInfo;
import com.example.aghezty.POJO.ArrayBaseResponse;
import com.example.aghezty.POJO.BrandInfo;
import com.example.aghezty.POJO.CategoryInfo;
import com.example.aghezty.POJO.CheckOutInfo;
import com.example.aghezty.POJO.CityInfo;
import com.example.aghezty.POJO.GovernorateInfo;
import com.example.aghezty.POJO.HomeData;
import com.example.aghezty.POJO.ObjectBaseResponse;
import com.example.aghezty.POJO.OrderInfo;
import com.example.aghezty.POJO.ProductFilterData;
import com.example.aghezty.POJO.ProductInfo;
import com.example.aghezty.POJO.UserInfo;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface AgheztyApi {


    @GET("home")
    Observable<Response<ObjectBaseResponse<HomeData>>> getHome();

    @GET("categorys")
    Single<Response<ArrayBaseResponse<CategoryInfo>>> getParentCategories();

    @GET("brands")
    Single<Response<ArrayBaseResponse<BrandInfo>>> getBrandCategories();

    @GET("products")
    Single<Response<ObjectBaseResponse<ProductFilterData>>> getSpecificProduct(
            @QueryMap Map<String, Object> options
    ,@Query("page") int page );


    @GET("inner_product/{id}")
    Single<Response<ObjectBaseResponse<ProductInfo>>> getInnerProductById(@Path("id") int id);


    @GET("governorate")
    Single<Response<ArrayBaseResponse<GovernorateInfo>>> getAllGovernorate();


    @GET("city")
    Single<Response<ArrayBaseResponse<CityInfo>>> getCitiesByGovernorateId(@Query("governorate_id") int id);



    @GET("order")
    Single<Response<ArrayBaseResponse<OrderInfo>>> getUserOrders();


    @GET("client")
    Single<Response<ResponseBody>> getUserInfo();


    @GET("addresses")
    Single<Response<ArrayBaseResponse<AddressInfo>>> getUserAddresses();


    @GET("delete_address/{id}")
    Single<Response<ResponseBody>> deleteAddress( @Path("id") int id);

    @Multipart
    @POST("updated_address/{id}")
    Single<Response<ResponseBody>> updateAddress(
            @Path("id") int id
            ,@Part("city_id") RequestBody city_id
            ,@Part("address") RequestBody address);


    @POST("register")
    Single<Response<ResponseBody>> registerNewUser(@Body UserInfo userInfo);

    @Multipart
    @POST("login")
    Single<Response<ResponseBody>> userLogin(
            @Part("email") RequestBody email
            ,@Part("password") RequestBody password
    );


    @POST("edit_profile")
    Single<Response<ResponseBody>> updateUserInfo( @Body UserInfo userInfo);

    @Multipart
    @POST("edit_profile")
    Single<Response<ResponseBody>> updateUserPicture(@PartMap Map<String, RequestBody> requestBodyMap ,@Part MultipartBody.Part userImage );



    @Multipart
    @POST("updated_password")
    Single<Response<ResponseBody>> updatePassword( @PartMap Map<String,RequestBody> passwordMap);


    @POST("add_address")
    Single<Response<ResponseBody>> addNewAddress( @Body AddressInfo addressInfo);





    @Multipart
    @POST("check_coupon")
    Single<Response<ResponseBody>> getCouponDiscount(@Part("coupon") RequestBody coupon);



    @POST("check_out")
    Single<Response<ResponseBody>> checkOut(@Body CheckOutInfo checkOutInfo);





}
