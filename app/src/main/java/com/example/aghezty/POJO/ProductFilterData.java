package com.example.aghezty.POJO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductFilterData {

    @SerializedName("data")
    private List<ProductInfo> ProductList;
    @SerializedName("next_url")
    private String next_url;

    private int Page;

    boolean isHasNext;


    public boolean isHasNext() {
        return isHasNext;
    }

    public void setHasNext(boolean hasNext) {
        isHasNext = hasNext;
    }

    public int getPage() {
        return Page;
    }

    public void setPage(int page) {
        Page = page;
    }

    public List<ProductInfo> getProductList() {
        return ProductList;
    }

    public void setProductList(List<ProductInfo> productList) {
        ProductList = productList;
    }

    public String getNext_url() {
        return next_url;
    }

    public void setNext_url(String next_url) {
        this.next_url = next_url;
    }


    public void addAll(List<ProductInfo> productInfos){

        this.ProductList.addAll(productInfos);

    }

}
