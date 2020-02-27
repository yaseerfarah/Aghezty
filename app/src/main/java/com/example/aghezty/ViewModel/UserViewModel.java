package com.example.aghezty.ViewModel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.aghezty.Data.AgheztyApi;
import com.example.aghezty.Data.CartInfoRoomMethod;
import com.example.aghezty.Data.CartRoomDatabase;
import com.example.aghezty.Data.SharedPreferencesMethod;
import com.example.aghezty.Interface.CompletableListener;
import com.example.aghezty.Interface.InnerProductListener;
import com.example.aghezty.Interface.RoomCartInfoListener;
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
import com.example.aghezty.View.Login;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

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
import retrofit2.Response;

import static com.example.aghezty.Constants.IS_LOGIN;
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



    private List<GovernorateInfo> governorateInfoList;
    private List<CityInfo> cityInfoList;
    private List<CartInfo> cartInfolist;


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

        this.governorateInfoList=new ArrayList<>();
        this.cityInfoList=new ArrayList<>();
        this.cartInfolist=new ArrayList<>();


        this.isLogin=checkIsLogin();
        if (isLogin){
            currentUserInfo=sharedPreferencesMethod.getUserInfo();
            getAllCart();
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
            public void onFailure(Throwable e) {
                Log.e(TAG,e.getMessage());
                completableListener.onFailure(e);
            }
        });

    }


    public void deleteCartInfo(CartInfo cartInfo){

        cartInfoRoomMethod.deleteStickerPack(cartInfo, new CompletableListener() {
            @Override
            public void onSuccess() {
                deleteCartFromList(cartInfo);
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e(TAG,e.getMessage());
            }
        });

    }


    public void registerUser(UserInfo userInfo){

        disposables.add(agheztyApi.registerNewUser(userInfo)
        .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stringResponse->{

                    if (stringResponse.isSuccessful()){
                       JSONObject jsonObject=new JSONObject(stringResponse.body().string());
                        userInfo.setToken(jsonObject.getJSONObject("success").getString("token"));
                        currentUserInfo=userInfo;
                        currentUserInfoMediatorLiveData.postValue(currentUserInfo);
                        setIsLogin(true);
                        saveUserInfo(currentUserInfo);

                    }else if(stringResponse.code()==401) {
                        Gson gson=new Gson();
                        ErrorResponse errorResponse=gson.fromJson(stringResponse.errorBody().string(),ErrorResponse.class);
                       for (String message:errorResponse.getMessages()){
                           Log.e("Register Response",message);
                           Toasty.error(context,message,Toast.LENGTH_SHORT).show();
                       }

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

    private void getAllCart(){

        cartInfoRoomMethod.getAllCarts(new RoomCartInfoListener() {
            @Override
            public void onSuccess(List<CartInfo> cartInfos) {
                cartInfolist.clear();
                cartInfolist.addAll(cartInfos);
                cartListMediatorLiveData.postValue(cartInfolist);
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e(TAG,e.getMessage());
            }
        });

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
