package com.example.aghezty.POJO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeData {

    @SerializedName("slides")
    private List<SliderInfo> slides;
    @SerializedName("ads")
    private List<AdsInfo> ads;
    @SerializedName("recently_added")
    private List<ProductInfo> recently_added;
    @SerializedName("selected_for_you")
    private List<ProductInfo> selected_for_you;
    @SerializedName("homepage_cat")
    private List<CategoryInfo> homepage_cat;


    public List<SliderInfo> getSlides() {
        return slides;
    }

    public void setSlides(List<SliderInfo> slides) {
        this.slides = slides;
    }

    public List<AdsInfo> getAds() {
        return ads;
    }

    public void setAds(List<AdsInfo> ads) {
        this.ads = ads;
    }

    public List<ProductInfo> getRecently_added() {
        return recently_added;
    }

    public void setRecently_added(List<ProductInfo> recently_added) {
        this.recently_added = recently_added;
    }

    public List<ProductInfo> getSelected_for_you() {
        return selected_for_you;
    }

    public void setSelected_for_you(List<ProductInfo> selected_for_you) {
        this.selected_for_you = selected_for_you;
    }

    public List<CategoryInfo> getHomepage_cat() {
        return homepage_cat;
    }

    public void setHomepage_cat(List<CategoryInfo> homepage_cat) {
        this.homepage_cat = homepage_cat;
    }
}
