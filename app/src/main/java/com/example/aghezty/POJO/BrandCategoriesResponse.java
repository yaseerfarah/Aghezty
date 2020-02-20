package com.example.aghezty.POJO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BrandCategoriesResponse {

    @SerializedName("status")
    private String status;
    @SerializedName("data")
    private List<BrandInfo> brandInfoList;
    @SerializedName("message")
    private String message;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<BrandInfo> getBrandInfoList() {
        return brandInfoList;
    }

    public void setBrandInfoList(List<BrandInfo> brandInfoList) {
        this.brandInfoList = brandInfoList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
