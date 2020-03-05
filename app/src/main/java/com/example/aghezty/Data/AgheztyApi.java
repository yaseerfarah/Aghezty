package com.example.aghezty.Data;

import com.example.aghezty.POJO.AddressInfo;
import com.example.aghezty.POJO.AddressResponse;
import com.example.aghezty.POJO.BrandCategoriesResponse;
import com.example.aghezty.POJO.CheckOutInfo;
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
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
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





    @GET("client")
    Single<Response<ResponseBody>> getUserInfo( @Header("Accept") String accept,
                                                  @Header("Authorization") String authorization);


    @GET("addresses")
    Single<Response<AddressResponse>> getUserAddresses(@Header("Accept") String accept,
                                                       @Header("Authorization") String authorization);


    @GET("delete_address/{id}")
    Single<Response<ResponseBody>> deleteAddress(
            @Header("Accept") String accept,
            @Header("Authorization") String authorization
    ,@Path("id") int id);

    @Multipart
    @POST("updated_address/{id}")
    Single<Response<ResponseBody>> updateAddress(
            @Header("Accept") String accept,
            @Header("Authorization") String authorization
            ,@Path("id") int id
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
    Single<Response<ResponseBody>> updateUserInfo(
            @Header("Accept") String accept,
            @Header("Authorization") String authorization,
            @Body UserInfo userInfo);

    @Multipart
    @POST("edit_profile")
    Single<Response<ResponseBody>> updateUserPicture(

            @Header("Accept") String accept,
            @Header("Authorization") String authorization,
            @PartMap Map<String, RequestBody> requestBodyMap
            ,@Part MultipartBody.Part userImage

    );



    @Multipart
    @POST("updated_password")
    Single<Response<ResponseBody>> updatePassword(
            @Header("Accept") String accept,
            @Header("Authorization") String authorization,
            @PartMap Map<String,RequestBody> passwordMap);


    @POST("add_address")
    Single<Response<ResponseBody>> addNewAddress(
            @Header("Accept") String accept,
            @Header("Authorization") String authorization,
            @Body AddressInfo addressInfo);





    @Multipart
    @POST("check_coupon")
    Single<Response<ResponseBody>> getCouponDiscount(
            @Header("Accept") String accept,
            @Header("Authorization") String authorization,
            @Part("coupon") RequestBody coupon);



    @POST("check_out")
    Single<Response<ResponseBody>> checkOut( @Header("Accept") String accept,
                                                    @Header("Authorization") String authorization,
                                                    @Header("Content-Type") String contentType,
                                                    @Body CheckOutInfo checkOutInfo);





}
