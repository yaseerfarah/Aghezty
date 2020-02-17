package com.example.aghezty.ViewModel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.aghezty.Data.AgheztyApi;
import com.example.aghezty.POJO.HomeData;
import com.example.aghezty.POJO.HomeResponse;
import com.example.aghezty.View.Home;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ProductViewModel extends ViewModel {

    private MediatorLiveData<HomeData> homeDataMediatorLiveData;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private AgheztyApi agheztyApi;
    private Context context;
    private HomeData homeData;


    @Inject
    public ProductViewModel(Context context,AgheztyApi agheztyApi) {
        this.context = context;
        this.agheztyApi=agheztyApi;
        this.homeDataMediatorLiveData=new MediatorLiveData<>();

    }


    public LiveData<HomeData> getHomeDataLiveData(){
        return homeDataMediatorLiveData;
    }

    public void getHomeData(){


        disposables.add(agheztyApi.getHome()
        .subscribeOn(Schedulers.io())
        .map(retrofit2.Response::body)
         .map(HomeResponse::getHomeData)
                        .observeOn(AndroidSchedulers.mainThread())
         .subscribe(this::onNext,this::onError,this::onComplete)

        );




    }



    private void onNext(HomeData homeData){

        this.homeData=homeData;

    }

    private void onError(Throwable throwable){

        Log.e(getClass().getName(),throwable.getMessage());
        Toast.makeText(context,throwable.getMessage(),Toast.LENGTH_SHORT).show();

    }

    private void onComplete(){

        homeDataMediatorLiveData.postValue(homeData);


    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
