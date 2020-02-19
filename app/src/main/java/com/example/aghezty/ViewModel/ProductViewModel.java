package com.example.aghezty.ViewModel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.aghezty.Data.AgheztyApi;
import com.example.aghezty.Interface.InnerProductListener;
import com.example.aghezty.POJO.HomeData;
import com.example.aghezty.POJO.HomeResponse;
import com.example.aghezty.POJO.InnerProductResponse;
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
    private ProductFilterData productFilterData;
    private List<ProductInfo> innerProductList;
    private HashMap<String,Object> filter;


    @Inject
    public ProductViewModel(Context context,AgheztyApi agheztyApi) {
        this.context = context;
        this.agheztyApi=agheztyApi;
        this.homeDataMediatorLiveData=new MediatorLiveData<>();
        this.offerProductLiveData=new MediatorLiveData<>();
        this.filter=new HashMap<>();
        this.innerProductList=new ArrayList<>();

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
        filter.clear();
        filter.put("offer","offer");

        if (productFilterData==null) {
            disposables.add(agheztyApi.getSpecificProduct(filter,1)
                    .subscribeOn(Schedulers.io())
                    .map(retrofit2.Response::body)
                    .map(productFilterResponse -> {

                        ProductFilterData productFilterData=productFilterResponse.getProductFilterData();

                        if (productFilterData.getNext_url() != null) {
                            productFilterData.setHasNext(true);
                        } else {
                            productFilterData.setHasNext(false);
                        }


                        productFilterData.setPage(1);
                        return productFilterData;

                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onOfferProductNext, this::onError, this::onProductFilterComplete)

            );

        }
    }



    public void getNextProductFilter(){

        int page =productFilterData.getPage()+1;


        disposables.add(agheztyApi.getSpecificProduct(filter,page)
                .subscribeOn(Schedulers.io())
                .map(retrofit2.Response::body)
                .map(productFilterResponse -> {

                    ProductFilterData productFilterData=productFilterResponse.getProductFilterData();

                    if (productFilterData.getNext_url() != null) {
                        productFilterData.setHasNext(true);
                    } else {
                        productFilterData.setHasNext(false);
                    }


                    productFilterData.setPage(page);
                    return productFilterData;

                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onProductFilterNext, this::onError, this::onProductFilterComplete)

        );

    }




    public void getInnerProductByID(ProductInfo productIdInfo, InnerProductListener innerProductListener){

        ProductInfo productInfo;

        for(ProductInfo info:innerProductList){
            if (info.getId()==productIdInfo.getId()){
                productInfo=info;
                if (innerProductListener!=null)
                innerProductListener.onSuccess(productInfo);
                return;
            }
        }

        disposables.add(agheztyApi.getInnerProductById(productIdInfo.getId())
                .subscribeOn(Schedulers.io())
                .map(retrofit2.Response::body)
                .map(InnerProductResponse::getProductInfo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(productInfo1 -> {

                    if (productIdInfo.getStars()>0){
                        productIdInfo.setRates(productInfo1.getRates());
                    }
                    innerProductList.add(productIdInfo);
                    if (innerProductListener!=null)
                    innerProductListener.onSuccess(productIdInfo);

                },this::onError)

        );





    }




    private void onOfferProductNext(ProductFilterData productFilterData) {

        this.productFilterData=productFilterData;
    }

    private void onProductFilterNext(ProductFilterData productFilterData) {

        this.productFilterData.addAll(productFilterData.getProductList());
        this.productFilterData.setHasNext(productFilterData.isHasNext());
        this.productFilterData.setPage(productFilterData.getPage());
        this.productFilterData.setNext_url(productFilterData.getNext_url());
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
    private void onProductFilterComplete(){

        offerProductLiveData.postValue(productFilterData);


    }



    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
