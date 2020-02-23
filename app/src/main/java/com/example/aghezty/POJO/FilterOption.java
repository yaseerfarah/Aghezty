package com.example.aghezty.POJO;

import com.example.aghezty.View.Filter;

import java.util.List;

public class FilterOption {

    private List<FilterInfo> categoriesID;
    private List<FilterInfo> brandID;
    private int priceRange;
    private boolean offer;


    public FilterOption(List<FilterInfo> categoriesID, List<FilterInfo> brandID, int priceRange, boolean offer) {
        this.categoriesID = categoriesID;
        this.brandID = brandID;
        this.priceRange = priceRange;
        this.offer = offer;
    }

    public FilterOption() {
    }

    public List<FilterInfo> getBrandID() {
        return brandID;
    }

    public void setBrandID(List<FilterInfo> brandID) {
        this.brandID = brandID;
    }

    public List<FilterInfo> getCategoriesID() {
        return categoriesID;
    }

    public void setCategoriesID(List<FilterInfo> categoriesID) {
        this.categoriesID = categoriesID;
    }


    public int getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(int priceRange) {
        this.priceRange = priceRange;
    }

    public boolean isOffer() {
        return offer;
    }

    public void setOffer(boolean offer) {
        this.offer = offer;
    }
}
