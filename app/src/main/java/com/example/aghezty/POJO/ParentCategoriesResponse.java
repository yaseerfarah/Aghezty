package com.example.aghezty.POJO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ParentCategoriesResponse {

    @SerializedName("status")
    private String status;
    @SerializedName("data")
    private List<CategoryInfo> ParentCategories;
    @SerializedName("message")
    private String message;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<CategoryInfo> getParentCategories() {
        return ParentCategories;
    }

    public void setParentCategories(List<CategoryInfo> parentCategories) {
        ParentCategories = parentCategories;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
