package com.example.aghezty.POJO;

import com.google.gson.annotations.SerializedName;

public class AddressInfo {

    @SerializedName("id")
    private int id;
    @SerializedName("city_id")
    private int city_id;
    @SerializedName("address")
    private String address;
    @SerializedName("city_en")
    private String city_en;
    @SerializedName("city_ar")
    private String city_ar;
    @SerializedName("governorate_en")
    private String governorate_en;
    @SerializedName("governorate_ar")
    private String governorate_ar;


    public AddressInfo() {
        //Empty Constructor
    }

    public AddressInfo(int city_id, String address, String city_en, String city_ar, String governorate_en, String governorate_ar) {
        this.city_id = city_id;
        this.address = address;
        this.city_en = city_en;
        this.city_ar = city_ar;
        this.governorate_en = governorate_en;
        this.governorate_ar = governorate_ar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
}
