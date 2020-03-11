package com.example.aghezty.POJO;

import com.example.aghezty.View.AppController;

import java.util.Locale;

import static com.example.aghezty.Constants.localeLanguage;

public class FilterInfo {

    static public final int CATEGORY=0;
    static public final int BRAND=1;

    int id;
    String name_en;
    String name_ar;
    String imageUrl;
    int type;

    public FilterInfo(int id, String name_en, String name_ar, String imageUrl, int type) {
        this.id = id;
        this.name_en = name_en;
        this.name_ar = name_ar;
        this.imageUrl = imageUrl;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getName_en() {
        return name_en;
    }

    public String getName_ar() {
        return name_ar;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }



    public String getName(){
        if (localeLanguage.getDisplayLanguage().matches(Locale.ENGLISH.getDisplayLanguage())){
            return name_en;
        }
        return name_ar;
    }


}



