package com.example.aghezty.ViewModel;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.aghezty.Data.AgheztyApi;
import com.example.aghezty.Data.CartInfoRoomMethod;
import com.example.aghezty.Data.CartRoomDatabase;
import com.example.aghezty.Data.SharedPreferencesMethod;
import com.example.aghezty.Interface.CheckCoupon;
import com.example.aghezty.Interface.CompletableListener;
import com.example.aghezty.Interface.InnerProductListener;
import com.example.aghezty.Interface.RoomCartInfoListener;
import com.example.aghezty.POJO.AddressInfo;
import com.example.aghezty.POJO.BrandCategoriesResponse;
import com.example.aghezty.POJO.BrandInfo;
import com.example.aghezty.POJO.CartInfo;
import com.example.aghezty.POJO.CategoryInfo;
import com.example.aghezty.POJO.CitiesResponse;
import com.example.aghezty.POJO.CityInfo;
import com.example.aghezty.POJO.ErrorResponse;
import com.example.aghezty.POJO.FilterInfo;
import com.example.aghezty.POJO.FilterOption;
import com.example.aghezty.POJO.GovernorateInfo;
import com.example.aghezty.POJO.GovernorateResponse;
import com.example.aghezty.POJO.HomeData;
import com.example.aghezty.POJO.HomeResponse;
import com.example.aghezty.POJO.InnerProductResponse;
import com.example.aghezty.POJO.ParentCategoriesResponse;
import com.example.aghezty.POJO.ProductFilterData;
import com.example.aghezty.POJO.ProductInfo;
import com.example.aghezty.POJO.UserInfo;
import com.example.aghezty.Util.FileUtils;
import com.example.aghezty.View.Login;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.inject.Inject;

import es.dmoral.toasty.Toasty;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

import static com.example.aghezty.Constants.IS_LOGIN;
import static com.example.aghezty.Constants.USER_COUPON_DISCOUNT;
import static com.example.aghezty.Constants.USER_INFO;

public class UserViewModel extends ViewModel {

    private final String TAG=getClass().getName();

    private Context context;
    private SharedPreferencesMethod sharedPreferencesMethod;
    private CartInfoRoomMethod cartInfoRoomMethod;
    private AgheztyApi agheztyApi;

    private MediatorLiveData<UserInfo> currentUserInfoMediatorLiveData;
    private MediatorLiveData<List<GovernorateInfo>> governorateListMediatorLiveData;
    private MediatorLiveData<List<CityInfo>> citiesListMediatorLiveData;
    private MediatorLiveData<List<CartInfo>> cartListMediatorLiveData;
    private MediatorLiveData<List<AddressInfo>> addressListMediatorLiveData;



    private List<GovernorateInfo> governorateInfoList;
    private List<CityInfo> cityInfoList;
    private List<CartInfo> cartInfolist;
    private List<AddressInfo> addressInfoList;

    private int couponDiscount;


    private UserInfo currentUserInfo;
    private final CompositeDisposable disposables = new CompositeDisposable();

    private boolean isLogin;

    @Inject
    public UserViewModel(Context context, SharedPreferencesMethod sharedPreferencesMethod,CartInfoRoomMethod cartInfoRoomMethod,AgheztyApi agheztyApi) {
        this.context = context;
        this.sharedPreferencesMethod = sharedPreferencesMethod;
        this.agheztyApi=agheztyApi;
        this.cartInfoRoomMethod=cartInfoRoomMethod;

        this.currentUserInfoMediatorLiveData=new MediatorLiveData<>();
        this.governorateListMediatorLiveData=new MediatorLiveData<>();
        this.citiesListMediatorLiveData=new MediatorLiveData<>();
        this.cartListMediatorLiveData=new MediatorLiveData<>();
        this.addressListMediatorLiveData=new MediatorLiveData<>();


        this.governorateInfoList=new ArrayList<>();
        this.cityInfoList=new ArrayList<>();
        this.cartInfolist=new ArrayList<>();
        this.addressInfoList=new ArrayList<>();


        this.isLogin=checkIsLogin();
        if (isLogin){
            currentUserInfo=sharedPreferencesMethod.getUserInfo();

            couponDiscount=getSavedCouponDiscount();

        }else {
            currentUserInfo=new UserInfo();
        }

    }


    public LiveData<UserInfo> getCurrentUserInfoLiveData() {
        return currentUserInfoMediatorLiveData;
    }

    public LiveData<List<GovernorateInfo>> getGovernorateListMediatorLiveData() {
        return governorateListMediatorLiveData;
    }

    public LiveData<List<CityInfo>> getCitiesListMediatorLiveData() {
        return citiesListMediatorLiveData;
    }

    public LiveData<List<CartInfo>> getCartListMediatorLiveData() {
        return cartListMediatorLiveData;
    }


    public LiveData<List<AddressInfo>> getAddressListMediatorLiveData() {
        return addressListMediatorLiveData;
    }

    public int getCouponDiscount() {
        return couponDiscount;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setCurrentUserInfo(UserInfo userInfo){
        currentUserInfo=userInfo;
    }

    public UserInfo getCurrentUserInfo() {
        return currentUserInfo;
    }




    public boolean checkInCart(ProductInfo productInfo){

        for (CartInfo cartInfo:cartInfolist){
            if (cartInfo.getProduct_id()==productInfo.getId()){
                return true;
            }
        }

        return false;
    }

    public void addCartInfo(ProductInfo productInfo,int quantity,CompletableListener completableListener){
        CartInfo cartInfo=new CartInfo(productInfo.getId(),quantity,productInfo.getDiscount()!=null?productInfo.getPrice_after_discount():productInfo.getPrice(),productInfo.getTitle_en(),productInfo.getMain_image());

        cartInfoRoomMethod.insertSticker(cartInfo, new CompletableListener() {
            @Override
            public void onSuccess() {
                cartInfolist.add(cartInfo);
                cartListMediatorLiveData.postValue(cartInfolist);
                completableListener.onSuccess();
            }

            @Override
            public void onFailure(String e) {
                Log.e(TAG,e);
                completableListener.onFailure(e);
            }
        });

    }


    public void deleteCartInfo(CartInfo cartInfo){

        cartInfoRoomMethod.deleteCartInfo(cartInfo, new CompletableListener() {
            @Override
            public void onSuccess() {
                deleteCartFromList(cartInfo);
            }

            @Override
            public void onFailure(String e) {
                Log.e(TAG,e);
            }
        });

    }


    public void registerUser(UserInfo userInfo,CompletableListener completableListener){

        disposables.add(agheztyApi.registerNewUser(userInfo)
        .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stringResponse->{

                    if (stringResponse.isSuccessful()){
                       JSONObject jsonObject=new JSONObject(stringResponse.body().string());
                        userInfo.setToken(jsonObject.getJSONObject("data").getString("token"));
                        currentUserInfo=userInfo;
                        currentUserInfoMediatorLiveData.postValue(currentUserInfo);
                        setIsLogin(true);
                        saveUserInfo(currentUserInfo);

                        completableListener.onSuccess();

                    }else if(stringResponse.code()==422) {
                        Gson gson=new Gson();
                        ErrorResponse errorResponse=gson.fromJson(stringResponse.errorBody().string(),ErrorResponse.class);
                       for (String message:errorResponse.getMessages()){
                           Log.e("Register Response",message);
                           Toasty.error(context,message,Toast.LENGTH_SHORT).show();
                       }

                       completableListener.onFailure(errorResponse.getMessages().toString());

                    }



                },this::onError)
        );

    }



    public void userLogin(String email,String password,CompletableListener completableListener){

        disposables.add(agheztyApi.userLogin(toRequestBody(email),toRequestBody(password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stringResponse->{

                    if (stringResponse.isSuccessful()){
                        JSONObject jsonObject=new JSONObject(stringResponse.body().string());
                        currentUserInfo.setToken(jsonObject.getJSONObject("data").getString("token"));
                        setIsLogin(true);
                        saveUserInfo(currentUserInfo);

                        completableListener.onSuccess();

                    }else if(stringResponse.code()==422) {
                        Gson gson=new Gson();
                        ErrorResponse errorResponse=gson.fromJson(stringResponse.errorBody().string(),ErrorResponse.class);
                        for (String message:errorResponse.getMessages()){
                            Log.e("Register Response",message);
                            Toasty.error(context,message,Toast.LENGTH_SHORT).show();
                        }

                        completableListener.onFailure(errorResponse.getMessages().toString());

                    }



                },this::onError)
        );

    }




    public void logOut(){
        setIsLogin(false);
        currentUserInfo=new UserInfo();
        saveUserInfo(currentUserInfo);
        currentUserInfoMediatorLiveData.postValue(null);
        couponDiscount=0;
        saveCouponDiscount(couponDiscount);

        cartInfoRoomMethod.deleteAllCartInfos(new CompletableListener() {
            @Override
            public void onSuccess() {
                cartInfolist.clear();
                cartListMediatorLiveData.postValue(cartInfolist);
            }

            @Override
            public void onFailure(String message) {
                Log.e(getClass().getName(),message);
            }
        });
    }


    public void updateProfile(UserInfo userInfo,Uri uri,CompletableListener completableListener){
        MultipartBody.Part body=null;
        File file = FileUtils.getFile(context,uri);
        if (file!=null) {
                // create RequestBody instance from file
                RequestBody requestFile =
                        RequestBody.create(
                                MediaType.parse(context.getContentResolver().getType(uri)),
                                file
                        );
                // MultipartBody.Part is used to send also the actual file name
                body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        }



        disposables.add(agheztyApi.updateUserPicture("application/json","Bearer "+currentUserInfo.getToken(),covertUserToMap(userInfo),body)
        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(stringResponse->{

                            if (stringResponse.isSuccessful()){
                                JSONObject jsonObject=new JSONObject(stringResponse.body().string());
                                if (!jsonObject.getJSONObject("data").getString("image").matches("null")){
                                    userInfo.setImgUrl(jsonObject.getJSONObject("data").getString("image"));
                                }

                                currentUserInfo=userInfo;
                                saveUserInfo(currentUserInfo);
                                currentUserInfoMediatorLiveData.postValue(currentUserInfo);
                                completableListener.onSuccess();

                            }else{

                                Gson gson=new Gson();
                                ErrorResponse errorResponse=gson.fromJson(stringResponse.errorBody().string(),ErrorResponse.class);
                                for (String message:errorResponse.getMessages()){
                                    Log.e("Update Response",message);
                                    Toasty.error(context,message,Toast.LENGTH_SHORT).show();
                                }
                                completableListener.onFailure(errorResponse.getMessages().toString());

                            }
                        },this::onError)
        );



    }


    public void updateUserPassword(String oldPassword,String newPassword,CompletableListener completableListener){

        Map<String,RequestBody> passwordMap=new HashMap<>();

        passwordMap.put("old_password",toRequestBody(oldPassword));
        passwordMap.put("password",toRequestBody(newPassword));
        passwordMap.put("password_confirmation",toRequestBody(newPassword));

        disposables.add(agheztyApi.updatePassword("application/json","Bearer "+currentUserInfo.getToken(),passwordMap)
        .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    if (responseBodyResponse.isSuccessful()){
                        JSONObject jsonObject=new JSONObject(responseBodyResponse.body().string());

                        currentUserInfo.setPassword(newPassword);
                        saveUserInfo(currentUserInfo);
                            completableListener.onSuccess();

                    }else {
                        Gson gson=new Gson();
                      //  Log.e("Response",responseBodyResponse.errorBody().string());
                        ErrorResponse errorResponse=gson.fromJson(responseBodyResponse.errorBody().string(),ErrorResponse.class);
                        for (String message:errorResponse.getMessages()){
                            Log.e("Update Pass Response",message);
                            Toasty.error(context,message,Toast.LENGTH_SHORT).show();
                        }
                        completableListener.onFailure(errorResponse.getMessages().toString());
                    }

                })

        );


    }


    public void addNewAddress(AddressInfo addressInfo,CompletableListener completableListener){

        disposables.add(agheztyApi.addNewAddress("application/json","Bearer "+currentUserInfo.getToken(),addressInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {

                    if (responseBodyResponse.isSuccessful()){
                        JSONObject jsonObject=new JSONObject(responseBodyResponse.body().string());
                        addressInfo.setId(jsonObject.getJSONObject("data").getInt("id"));
                        addressInfoList.add(addressInfo);
                        currentUserInfo.setCityId(addressInfo.getCity_id());
                        currentUserInfo.setCity(addressInfo.getCity_en());
                        currentUserInfo.setGovernorate(addressInfo.getGovernorate_en());
                        currentUserInfo.setAddress(addressInfo.getAddress());
                        saveUserInfo(currentUserInfo);
                        addressListMediatorLiveData.postValue(addressInfoList);
                        completableListener.onSuccess();

                    }else {
                        Gson gson=new Gson();
                        //  Log.e("Response",responseBodyResponse.errorBody().string());
                        ErrorResponse errorResponse=gson.fromJson(responseBodyResponse.errorBody().string(),ErrorResponse.class);
                        for (String message:errorResponse.getMessages()){
                            Log.e("Update Pass Response",message);
                            Toasty.error(context,message,Toast.LENGTH_SHORT).show();
                        }
                        completableListener.onFailure(errorResponse.getMessages().toString());
                    }

                },this::onError)

        );

    }


    public void getLoginUserInfo(){

        if (currentUserInfo.getName()==null&&currentUserInfo.getAddress()==null){
            disposables.add(agheztyApi.getUserInfo("application/json", "Bearer " + currentUserInfo.getToken())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(userInfoResponse -> {

                        if (userInfoResponse.isSuccessful()) {
                            JSONObject jsonObject=new JSONObject(userInfoResponse.body().string());
                            Gson gson=new Gson();
                            UserInfo userInfo = gson.fromJson(jsonObject.getString("data"),UserInfo.class);
                            currentUserInfo.setName(userInfo.getName());
                            currentUserInfo.setEmail(userInfo.getEmail());
                            currentUserInfo.setImgUrl(userInfo.getImgUrl());
                            currentUserInfo.setPhoneNumber(userInfo.getPhoneNumber());

                            // saveUserInfo(currentUserInfo);
                            // currentUserInfoMediatorLiveData.postValue(currentUserInfo);
                            getAllUserAddress();
                        } else {
                            Gson gson = new Gson();
                            //  Log.e("Response",responseBodyResponse.errorBody().string());
                            ErrorResponse errorResponse = gson.fromJson(userInfoResponse.errorBody().string(), ErrorResponse.class);
                            for (String message : errorResponse.getMessages()) {
                                Log.e("Update Pass Response", message);
                                Toasty.error(context, message, Toast.LENGTH_SHORT).show();
                            }

                        }


                    }, this::onError)
            );
        }else {
            currentUserInfoMediatorLiveData.postValue(currentUserInfo);
        }


    }



    private void getAllUserAddress(){

        disposables.add(agheztyApi.getUserAddresses("application/json","Bearer "+currentUserInfo.getToken())
        .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(addressResponseResponse -> {

                    if (addressResponseResponse.isSuccessful()){
                       List<AddressInfo> addressInfos=addressResponseResponse.body().getAddressInfoList();

                       if (!addressInfos.isEmpty()) {

                           currentUserInfo.setCityId(addressInfos.get(0).getCity_id());
                           currentUserInfo.setCity(addressInfos.get(0).getCity_en());
                           currentUserInfo.setGovernorate(addressInfos.get(0).getGovernorate_en());
                           currentUserInfo.setAddress(addressInfos.get(0).getAddress());

                           addressInfoList.addAll(addressInfos);
                           saveUserInfo(currentUserInfo);
                           addressListMediatorLiveData.postValue(addressInfoList);
                           currentUserInfoMediatorLiveData.postValue(currentUserInfo);
                       }else {
                           currentUserInfoMediatorLiveData.postValue(currentUserInfo);
                       }

                    }else {
                        Gson gson=new Gson();
                        //  Log.e("Response",responseBodyResponse.errorBody().string());
                        ErrorResponse errorResponse=gson.fromJson(addressResponseResponse.errorBody().string(),ErrorResponse.class);
                        for (String message:errorResponse.getMessages()){
                            Log.e("Update Pass Response",message);
                            Toasty.error(context,message,Toast.LENGTH_SHORT).show();
                        }

                    }


                },this::onError)


        );

    }



    public void getCouponDiscount(String coupon, CheckCoupon checkCoupon){
        disposables.add(agheztyApi.getCouponDiscount("application/json","Bearer "+currentUserInfo.getToken(),toRequestBody(coupon))
        .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(requestBodyResponse -> {

                    if (requestBodyResponse.isSuccessful()){

                        JSONObject jsonObject=new JSONObject(requestBodyResponse.body().string());
                        couponDiscount+=jsonObject.getInt("data");
                        saveCouponDiscount(couponDiscount);
                        checkCoupon.onSuccess(couponDiscount);



                    }else {
                        Gson gson=new Gson();
                          //Log.e("Response",requestBodyResponse.errorBody().string());
                        ErrorResponse errorResponse=gson.fromJson(requestBodyResponse.errorBody().string(),ErrorResponse.class);
                        checkCoupon.onFailure(errorResponse.getMessages().get(0));
                    }

                },this::onError)
        );
    }





    public void getAllGovernorate(){

        if (governorateInfoList.isEmpty()) {
            disposables.add(agheztyApi.getAllGovernorate()
                    .subscribeOn(Schedulers.io())
                    .map(Response::body)
                    .map(GovernorateResponse::getGovernorateInfoList)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(governorateInfos -> {
                        governorateInfoList.clear();
                        governorateInfoList.addAll(governorateInfos);
                        governorateListMediatorLiveData.postValue(governorateInfoList);

                    }, this::onError)

            );
        }

    }

    public void getCitiesByGovernorateId(int id){


        disposables.add(agheztyApi.getCitiesByGovernorateId(id)
                .subscribeOn(Schedulers.io())
                .map(Response::body)
                .map(CitiesResponse::getCityInfoList)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cityInfos -> {

                    cityInfoList.clear();
                    cityInfoList.addAll(cityInfos);
                    citiesListMediatorLiveData.postValue(cityInfoList);

                },this::onError)

        );

    }

    public void getAllCart(){

        if (cartListMediatorLiveData.getValue()==null) {
            cartInfoRoomMethod.getAllCarts(new RoomCartInfoListener() {
                @Override
                public void onSuccess(List<CartInfo> cartInfos) {
                    cartInfolist.clear();
                    cartInfolist.addAll(cartInfos);
                    cartListMediatorLiveData.postValue(cartInfolist);
                }

                @Override
                public void onFailure(Throwable e) {
                    Log.e(TAG, e.getMessage());
                }
            });
        }
    }


    private boolean checkIsLogin(){

        return sharedPreferencesMethod.getBoolean(IS_LOGIN);
    }
    private void saveUserInfo(UserInfo userInfo){

        sharedPreferencesMethod.setObject(USER_INFO,userInfo);

    }
    private void setIsLogin(boolean login){
        sharedPreferencesMethod.setBoolean(IS_LOGIN,login);
        isLogin=login;
    }

    private void deleteCartFromList(CartInfo cartInfo){

        for (int i=0;i<cartInfolist.size();i++){

            if (cartInfo.getProduct_id()==cartInfolist.get(i).getProduct_id()){
                cartInfolist.remove(i);
                cartListMediatorLiveData.postValue(cartInfolist);
            }

        }

    }


    private Map<String,RequestBody> covertUserToMap(UserInfo userInfo){

        Map<String,RequestBody> userMap=new HashMap<>();

        userMap.put("name",toRequestBody(userInfo.getName()));
        userMap.put("email",toRequestBody(userInfo.getEmail()));
        userMap.put("phone",toRequestBody(userInfo.getPhoneNumber()));
        return userMap;

    }



    // This method  converts String to RequestBody
    private RequestBody toRequestBody (String value) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body ;
    }


    private void saveCouponDiscount(int couponDiscount){
        sharedPreferencesMethod.setInteger(USER_COUPON_DISCOUNT,couponDiscount);
    }

    private int getSavedCouponDiscount(){
        return sharedPreferencesMethod.getInteger(USER_COUPON_DISCOUNT);
    }

    private void onError(Throwable throwable){

        Log.e(getClass().getName(),throwable.getMessage());
        Toast.makeText(context,throwable.getMessage(),Toast.LENGTH_SHORT).show();

    }



    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
