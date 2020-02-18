package com.example.aghezty.POJO;

import com.google.gson.annotations.SerializedName;

public class ProductFilterResponse {

    @SerializedName("status")
    private String status;
    @SerializedName("data")
    private ProductFilterData productFilterData;
    @SerializedName("message")
    private String message;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ProductFilterData getProductFilterData() {
        return productFilterData;
    }

    public void setProductFilterData(ProductFilterData productFilterData) {
        this.productFilterData = productFilterData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
