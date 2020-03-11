package com.example.aghezty.POJO;

import com.example.aghezty.View.AppController;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

import static com.example.aghezty.Constants.localeLanguage;

public class GovernorateInfo {
    @SerializedName("id")
    private int id;
    @SerializedName("governorate_en")
    private String governorate_en;
    @SerializedName("governorate_ar")
    private String governorate_ar;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGovernorate_en() {
        return governorate_en;
    }

    public void setGovernorate_en(String governorate_en) {
        this.governorate_en = governorate_en;
    }

    public String getGovernorate_ar() {
        return governorate_ar;
    }

    public void setGovernorate_ar(String governorate_ar) {
        this.governorate_ar = governorate_ar;
    }


    public String getGovernorate(){
        if (localeLanguage.getDisplayLanguage().matches(Locale.ENGLISH.getDisplayLanguage())){
            return governorate_en;
        }
        return governorate_ar;
    }

}
