package com.example.aghezty.ViewModel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.aghezty.Data.AgheztyApi;
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
import com.example.aghezty.View.Filter;
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

    static public final int LESS_FROM_1000=1000;
    static public final int FROM_1000_TO_3000=3000;
    static public final int FROM_3000_TO_6000=6000;
    static public final int FROM_6000_TO_10000=10000;
    static public final int MORE_THAN_10000=20000;
    static public final int ALL=0;
    static public final int Low_To_High_Price=2;
    static public final int High_To_Low_Price=1;

    private MediatorLiveData<HomeData> homeDataMediatorLiveData;
    private MediatorLiveData<ProductFilterData> productFilterDataLiveData;
    private MediatorLiveData<List<CategoryInfo>> parentCategoriesLiveData;
    private MediatorLiveData<List<BrandInfo>> brandCategoriesLiveData;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private AgheztyApi agheztyApi;
    private Context context;
    private HomeData homeData=null;
    private ProductFilterData productFilterData;
    private List<ProductInfo> innerProductList;
    private List<CategoryInfo> parentCategoriesInfoList;
    private List<BrandInfo> brandCategoriesInfoList;
    private HashMap<String,Object> filter;
    private FilterOption filterOption;
    private boolean isFilter=false;


    @Inject
    public ProductViewModel(Context context,AgheztyApi agheztyApi) {
        this.context = context;
        this.agheztyApi=agheztyApi;
        this.homeDataMediatorLiveData=new MediatorLiveData<>();
        this.productFilterDataLiveData=new MediatorLiveData<>();
        this.parentCategoriesLiveData=new MediatorLiveData<>();
        this.brandCategoriesLiveData=new MediatorLiveData<>();
        this.filter=new HashMap<>();
        this.innerProductList=new ArrayList<>();
        this.parentCategoriesInfoList=new ArrayList<>();
        this.brandCategoriesInfoList=new ArrayList<>();



    }


    public LiveData<HomeData> getHomeDataLiveData(){
        return homeDataMediatorLiveData;
    }
    public LiveData<ProductFilterData> getProductFilterLiveData(){
        return productFilterDataLiveData;
    }

    public LiveData<List<BrandInfo>> getBrandCategoriesLiveData() {
        return brandCategoriesLiveData;
    }

    public LiveData<List<CategoryInfo>> getParentCategoriesLiveData() {
        return parentCategoriesLiveData;
    }

    public FilterOption getFilterOption() {
        return filterOption;
    }

    public boolean isFilter() {
        return isFilter;
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




    public void getProductFilter(){

      if (isFilter) {

          disposables.add(agheztyApi.getSpecificProduct(filter, 1)
                  .subscribeOn(Schedulers.io())
                  .doOnError(throwable -> {

                  })
                  .map(retrofit2.Response::body)
                  .map(productFilterResponse -> {

                      ProductFilterData productFilterData = productFilterResponse.getProductFilterData();

                      if (productFilterData.getNext_url() != null) {
                          productFilterData.setHasNext(true);
                      } else {
                          productFilterData.setHasNext(false);
                      }


                      productFilterData.setPage(1);
                      return productFilterData;

                  })
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(productFilterData1 -> {

                      this.productFilterData = productFilterData1;
                      productFilterDataLiveData.postValue(productFilterData);
                      isFilter = false;


                  }, this::onError)

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
                .subscribe(this::onProductFilterNext, this::onError)

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




    public void getParentCategories(){


        if (parentCategoriesInfoList.isEmpty()) {
            disposables.add(agheztyApi.getParentCategories()
                    .subscribeOn(Schedulers.io())
                    .map(retrofit2.Response::body)
                    .map(ParentCategoriesResponse::getParentCategories)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(categoryInfos -> {

                        parentCategoriesInfoList.clear();
                        parentCategoriesInfoList.addAll(categoryInfos);
                        parentCategoriesLiveData.postValue(parentCategoriesInfoList);

                    }, this::onError)

            );

        }else {
            parentCategoriesLiveData.postValue(parentCategoriesInfoList);
        }

    }



    public void getBrandCategories(){


        if (brandCategoriesInfoList.isEmpty()) {
            disposables.add(agheztyApi.getBrandCategories()
                    .subscribeOn(Schedulers.io())
                    .map(retrofit2.Response::body)
                    .map(BrandCategoriesResponse::getBrandInfoList)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(brandInfos -> {

                        brandCategoriesInfoList.addAll(brandInfos);
                        brandCategoriesLiveData.postValue(brandCategoriesInfoList);

                    }, this::onError)

            );

        }else {
            brandCategoriesLiveData.postValue(brandCategoriesInfoList);
        }

    }




    public void setFilter(List<FilterInfo> categoryID, List<FilterInfo> brandID, int priceRange,int orderBy, boolean offers){
        clearFilter();
        filterOption.setOffer(offers);
        filterOption.setPriceRange(priceRange);
        filterOption.setOrderBy(orderBy);




        if (categoryID!=null){
            String ids="";
            filterOption.setCategoriesID(categoryID);
            for (FilterInfo filterInfo:categoryID){
                ids+=String.valueOf(filterInfo.getId())+",";
            }
            filter.put("category_id",ids);
        }else {
            filterOption.setCategoriesID(new ArrayList<>());
        }

        if (brandID!=null){
            String ids="";
            filterOption.setBrandID(brandID);
            for (FilterInfo filterInfo:brandID){
                ids+=String.valueOf(filterInfo.getId())+",";

            }
            filter.put("brand_id",ids);
        }
        else {
            filterOption.setBrandID(new ArrayList<>());
        }

        if (priceRange!=ALL){

            switch (priceRange){

                case LESS_FROM_1000:
                    filter.put("from",0);
                    filter.put("to",1000);
                    break;

                case FROM_1000_TO_3000:
                    filter.put("from",1000);
                    filter.put("to",3000);
                    break;

                case FROM_3000_TO_6000:
                    filter.put("from",3000);
                    filter.put("to",6000);
                    break;

                case FROM_6000_TO_10000:
                    filter.put("from",6000);
                    filter.put("to",10000);
                    break;

                case MORE_THAN_10000:
                    filter.put("from",10000);
                    filter.put("to",1000000);
                    break;

            }


        }

        if (orderBy!=ALL){

            switch (orderBy){

                case Low_To_High_Price:
                    filter.put("sorted","price,asc");
                    break;

                case High_To_Low_Price:
                    filter.put("sorted","price,desc");
                    break;

            }


        }

        if (offers){
            filter.put("offer","offer");

        }

        isFilter=true;

    }

    private void clearFilter(){
        filterOption=new FilterOption();
        filter.clear();
        productFilterDataLiveData.postValue(null);

    }


    public void clearApiCall(){
        disposables.clear();
        //Toast.makeText(context,"Clear",Toast.LENGTH_SHORT).show();
    }

    private void onProductFilterNext(ProductFilterData productFilterData1) {

        this.productFilterData.addAll(productFilterData1.getProductList());
        this.productFilterData.setHasNext(productFilterData1.isHasNext());
        this.productFilterData.setPage(productFilterData1.getPage());
        this.productFilterData.setNext_url(productFilterData1.getNext_url());

        productFilterDataLiveData.postValue(this.productFilterData);

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







    @Override
    protected void onCleared() {
        super.onCleared();
        //Toast.makeText(context,"Clear",Toast.LENGTH_SHORT).show();
       // disposables.clear();
    }
}
