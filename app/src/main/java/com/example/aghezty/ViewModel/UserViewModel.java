package com.example.aghezty.ViewModel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.aghezty.Data.AgheztyApi;
import com.example.aghezty.Data.SharedPreferencesMethod;
import com.example.aghezty.Interface.InnerProductListener;
import com.example.aghezty.POJO.BrandCategoriesResponse;
import com.example.aghezty.POJO.BrandInfo;
import com.example.aghezty.POJO.CategoryInfo;
import com.example.aghezty.POJO.CitiesResponse;
import com.example.aghezty.POJO.CityInfo;
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


    private Context context;
    private SharedPreferencesMethod sharedPreferencesMethod;
    private AgheztyApi agheztyApi;

    private MediatorLiveData<UserInfo> currentUserInfoMediatorLiveData;
    private MediatorLiveData<List<GovernorateInfo>> governorateListMediatorLiveData;
    private MediatorLiveData<List<CityInfo>> citiesListMediatorLiveData;


    private List<GovernorateInfo> governorateInfoList;
    private List<CityInfo> cityInfoList;


    private UserInfo currentUserInfo;
    private final CompositeDisposable disposables = new CompositeDisposable();

    private boolean isLogin;

    @Inject
    public UserViewModel(Context context, SharedPreferencesMethod sharedPreferencesMethod,AgheztyApi agheztyApi) {
        this.context = context;
        this.sharedPreferencesMethod = sharedPreferencesMethod;
        this.agheztyApi=agheztyApi;

        this.currentUserInfoMediatorLiveData=new MediatorLiveData<>();
        this.governorateListMediatorLiveData=new MediatorLiveData<>();
        this.citiesListMediatorLiveData=new MediatorLiveData<>();

        this.governorateInfoList=new ArrayList<>();
        this.cityInfoList=new ArrayList<>();


        this.isLogin=checkIsLogin();
        if (isLogin){
            currentUserInfo=sharedPreferencesMethod.getUserInfo();
        }

    }





    public boolean isLogin() {
        return isLogin;
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

    public void setCurrentUserInfo(UserInfo userInfo){
        currentUserInfo=userInfo;
    }

    public UserInfo getCurrentUserInfo() {
        return currentUserInfo;
    }

    public void registerUser(UserInfo userInfo){

        disposables.add(agheztyApi.registerNewUser(userInfo)
        .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stringResponse->{

                    if (stringResponse.isSuccessful()){
                        userInfo.setToken(stringResponse.body().getRegisterResponse().getToken());
                        currentUserInfo=userInfo;
                        currentUserInfoMediatorLiveData.postValue(currentUserInfo);
                        setIsLogin(true);
                        saveUserInfo(currentUserInfo);

                    }else if(stringResponse.code()==401) {

                        JSONObject jsonObject=new JSONObject(stringResponse.errorBody().string());
                        String errors=jsonObject.getString("error");
                        String[]messages= errors.split(",");
                        List<String> errorMessage=new ArrayList<>();
                        for (String message:messages){
                            errorMessage.add(message.trim().replaceAll("\\.*\\W\\W\\W*",""));
                            Log.e("Register Response",message.trim().replaceAll("\\.*\\W\\W\\W*",""));
                            Toasty.error(context,message.trim().replaceAll("\\.*\\W\\W\\W*",""),Toast.LENGTH_SHORT).show();

                        }

                        //Toast.makeText(context,errorMessage.get(0),Toast.LENGTH_LONG).show();
                    }



                },this::onError)
        );

    }


    public void getAllGovernorate(){

        disposables.add(agheztyApi.getAllGovernorate()
        .subscribeOn(Schedulers.io())
                .map(Response::body)
                .map(GovernorateResponse::getGovernorateInfoList)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(governorateInfos -> {
                    governorateInfoList.clear();
                    governorateInfoList.addAll(governorateInfos);
                    governorateListMediatorLiveData.postValue(governorateInfoList);

                },this::onError)

        );

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
