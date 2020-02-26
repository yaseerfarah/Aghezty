package com.example.aghezty.POJO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GovernorateResponse {

    @SerializedName("data")
    private List<GovernorateInfo> governorateInfoList;
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private String status;


    public List<GovernorateInfo> getGovernorateInfoList() {
        return governorateInfoList;
    }

    public void setGovernorateInfoList(List<GovernorateInfo> governorateInfoList) {
        this.governorateInfoList = governorateInfoList;
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
