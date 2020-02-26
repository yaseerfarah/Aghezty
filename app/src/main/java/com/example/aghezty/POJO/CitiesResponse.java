package com.example.aghezty.POJO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CitiesResponse {


    @SerializedName("data")
    private List<CityInfo> cityInfoList;
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private String status;


    public List<CityInfo> getCityInfoList() {
        return cityInfoList;
    }

    public void setCityInfoList(List<CityInfo> cityInfoList) {
        this.cityInfoList = cityInfoList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
