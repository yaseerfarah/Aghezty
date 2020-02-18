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
import com.example.aghezty.POJO.ProductFilterData;
import com.example.aghezty.POJO.ProductInfo;
import com.example.aghezty.View.Home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ProductViewModel extends ViewModel {

    private MediatorLiveData<HomeData> homeDataMediatorLiveData;
    private MediatorLiveData<ProductFilterData> offerProductLiveData;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private AgheztyApi agheztyApi;
    private Context context;
    private HomeData homeData=null;
    private ProductFilterData offerProducts;


    @Inject
    public ProductViewModel(Context context,AgheztyApi agheztyApi) {
        this.context = context;
        this.agheztyApi=agheztyApi;
        this.homeDataMediatorLiveData=new MediatorLiveData<>();
        this.offerProductLiveData=new MediatorLiveData<>();



    }


    public LiveData<HomeData> getHomeDataLiveData(){
        return homeDataMediatorLiveData;
    }
    public LiveData<ProductFilterData> getOfferProductsLiveData(){
        return offerProductLiveData;
    }

    public void getHomeData(){


        if (homeData==null) {
            disposables.add(agheztyApi.getHome()
                    .subscribeOn(Schedulers.io())
                    .map(retrofit2.Response::body)
                    .map(HomeResponse::getHomeData)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onHomeDataNext, this::onError, this::onHomeDataComplete)

            );

        }



    }




    public void getOfferProducts(){

        HashMap<String,Object> filter=new HashMap<>();

        filter.put("offer","offer");

        if (offerProducts==null) {
            disposables.add(agheztyApi.getSpecificProduct(filter,1)
                    .subscribeOn(Schedulers.io())
                    .map(retrofit2.Response::body)
                    .map(productFilterResponse -> {

                        ProductFilterData productFilterData=productFilterResponse.getProductFilterData();
                        productFilterData.setPage(1);
                        return productFilterData;

                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onOfferProductNext, this::onError, this::onOfferProductComplete)

            );

        }
    }

    private void onOfferProductNext(ProductFilterData productFilterData) {

        this.offerProducts=productFilterData;
    }


    private void onHomeDataNext(HomeData homeData){

        this.homeData=homeData;

    }

    private void onError(Throwable throwable){

        Log.e(getClass().getName(),throwable.getMessage());
        Toast.makeText(context,throwable.getMessage(),Toast.LENGTH_SHORT).show();

    }

    private void onHomeDataComplete(){

        homeDataMediatorLiveData.postValue(homeData);


    }
    private void onOfferProductComplete(){

        offerProductLiveData.postValue(offerProducts);


    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
