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
import com.example.aghezty.POJO.FilterInfo;
import com.example.aghezty.POJO.FilterOption;
import com.example.aghezty.POJO.HomeData;
import com.example.aghezty.POJO.HomeResponse;
import com.example.aghezty.POJO.InnerProductResponse;
import com.example.aghezty.POJO.ParentCategoriesResponse;
import com.example.aghezty.POJO.ProductFilterData;
import com.example.aghezty.POJO.ProductInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.aghezty.Constants.IS_LOGIN;

public class UserViewModel extends ViewModel {


    private Context context;
    private SharedPreferencesMethod sharedPreferencesMethod;

    private boolean isLogin;

    @Inject
    public UserViewModel(Context context, SharedPreferencesMethod sharedPreferencesMethod) {
        this.context = context;
        this.sharedPreferencesMethod = sharedPreferencesMethod;

        this.isLogin=checkIsLogin();

    }





    public boolean isLogin() {
        return isLogin;
    }

    private boolean checkIsLogin(){

        return sharedPreferencesMethod.getBoolean(IS_LOGIN);
    }


}
