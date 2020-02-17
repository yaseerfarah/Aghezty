package com.example.aghezty.POJO;

import com.google.gson.annotations.SerializedName;

public class HomeResponse {

    @SerializedName("status")
    private String status;
    @SerializedName("data")
    private HomeData homeData;
    @SerializedName("message")
    private String message;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public HomeData getHomeData() {
        return homeData;
    }

    public void setHomeData(HomeData homeData) {
        this.homeData = homeData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
