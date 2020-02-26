package com.example.aghezty.POJO;

import com.google.gson.annotations.SerializedName;

public class CityInfo {

    @SerializedName("id")
    private int id;
    @SerializedName("city_en")
    private String city_en;
    @SerializedName("city_ar")
    private String city_ar;
    @SerializedName("governorate_id")
    private int governorate_id;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity_en() {
        return city_en;
    }

    public void setCity_en(String city_en) {
        this.city_en = city_en;
    }

    public String getCity_ar() {
        return city_ar;
    }

    public void setCity_ar(String city_ar) {
        this.city_ar = city_ar;
    }

    public int getGovernorate_id() {
        return governorate_id;
    }

    public void setGovernorate_id(int governorate_id) {
        this.governorate_id = governorate_id;
    }

}
