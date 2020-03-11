package com.example.aghezty.POJO;

import com.example.aghezty.View.AppController;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Locale;

import static com.example.aghezty.Constants.localeLanguage;

public class BrandInfo {

    @SerializedName("id")
    private int id;
    @SerializedName("title_en")
    private String title_en;
    @SerializedName("title_ar")
    private String title_ar;
    @SerializedName("image")
    private String image;
    @SerializedName("category")
    private List<CategoryInfo> subCategoryInfoList;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle_en() {
        return title_en;
    }

    public void setTitle_en(String title_en) {
        this.title_en = title_en;
    }

    public String getTitle_ar() {
        return title_ar;
    }

    public void setTitle_ar(String title_ar) {
        this.title_ar = title_ar;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<CategoryInfo> getSubCategoryInfoList() {
        return subCategoryInfoList;
    }

    public void setSubCategoryInfoList(List<CategoryInfo> subCategoryInfoList) {
        this.subCategoryInfoList = subCategoryInfoList;
    }


    public String getTitle(){
        if (localeLanguage.getDisplayLanguage().matches(Locale.ENGLISH.getDisplayLanguage())){
            return title_en;
        }
        return title_ar;
    }


}
