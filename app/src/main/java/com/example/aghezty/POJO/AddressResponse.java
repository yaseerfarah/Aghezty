package com.example.aghezty.POJO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddressResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private List<AddressInfo> addressInfoList;

    @SerializedName("message")
    private String message;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AddressInfo> getAddressInfoList() {
        return addressInfoList;
    }

    public void setAddressInfoList(List<AddressInfo> addressInfoList) {
        this.addressInfoList = addressInfoList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
