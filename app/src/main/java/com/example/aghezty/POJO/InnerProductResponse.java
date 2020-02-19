package com.example.aghezty.POJO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InnerProductResponse {

    @SerializedName("status")
    private String status;
    @SerializedName("data")
    private ProductInfo productInfo;
    @SerializedName("message")
    private String message;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ProductInfo getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(ProductInfo productInfo) {
        this.productInfo = productInfo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
