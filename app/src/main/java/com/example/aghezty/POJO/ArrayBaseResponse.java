package com.example.aghezty.POJO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArrayBaseResponse<T> {

    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private List<T> responseList;

    @SerializedName("message")
    private String message;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<T> getResponseList() {
        return responseList;
    }

    public void setResponseList(List<T> responseList) {
        this.responseList = responseList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
