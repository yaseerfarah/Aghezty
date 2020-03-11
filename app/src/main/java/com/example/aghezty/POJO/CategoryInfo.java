package com.example.aghezty.POJO;

import com.example.aghezty.View.AppController;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Locale;

import static com.example.aghezty.Constants.localeLanguage;

public class CategoryInfo {

    @SerializedName("id")
    private int id;
    @SerializedName("title_en")
    private String title_en;
    @SerializedName("title_ar")
    private String title_ar;
    @SerializedName("coding")
    private String coding;
    @SerializedName("parent_id")
    private int parent_id;
    @SerializedName("image")
    private String image;
    @SerializedName("sub_cats")
    private List<CategoryInfo> sub_cats;


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

    public String getCoding() {
        return coding;
    }

    public void setCoding(String coding) {
        this.coding = coding;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<CategoryInfo> getSub_cats() {
        return sub_cats;
    }

    public void setSub_cats(List<CategoryInfo> sub_cats) {
        this.sub_cats = sub_cats;
    }


    public String getTitle(){
        if (localeLanguage.getDisplayLanguage().matches(Locale.ENGLISH.getDisplayLanguage())){
            return title_en;
        }
        return title_ar;
    }


}
