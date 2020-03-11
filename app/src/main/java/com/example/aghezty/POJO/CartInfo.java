package com.example.aghezty.POJO;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.aghezty.View.AppController;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

import static com.example.aghezty.Constants.localeLanguage;

@Entity
public class CartInfo {

    @PrimaryKey
    @SerializedName("product_id")
    private int product_id;
    @SerializedName("quantity")
    private int quantity;
    @SerializedName("price")
    private int pro_price;
    @SerializedName("total_price")
    private int pro_totalPrice;

    private String pro_name_en,pro_name_ar,pro_imageUrl;


    public CartInfo(int product_id, int quantity, int pro_price, int pro_totalPrice, String pro_name_en, String pro_name_ar, String pro_imageUrl) {
        this.product_id = product_id;
        this.quantity = quantity;
        this.pro_price = pro_price;
        this.pro_totalPrice = pro_totalPrice;
        this.pro_name_en = pro_name_en;
        this.pro_name_ar = pro_name_ar;
        this.pro_imageUrl = pro_imageUrl;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPro_price() {
        return pro_price;
    }

    public void setPro_price(int pro_price) {
        this.pro_price = pro_price;
    }

    public String getPro_name_en() {
        return pro_name_en;
    }

    public void setPro_name_en(String pro_name_en) {
        this.pro_name_en = pro_name_en;
    }

    public String getPro_name_ar() {
        return pro_name_ar;
    }

    public void setPro_name_ar(String pro_name_ar) {
        this.pro_name_ar = pro_name_ar;
    }

    public String getPro_imageUrl() {
        return pro_imageUrl;
    }

    public void setPro_imageUrl(String pro_imageUrl) {
        this.pro_imageUrl = pro_imageUrl;
    }


    public int getPro_totalPrice() {
        return pro_totalPrice;
    }

    public void setPro_totalPrice(int pro_totalPrice) {
        this.pro_totalPrice = pro_totalPrice;
    }



    public String getProName(){
        if (localeLanguage.getDisplayLanguage().matches(Locale.ENGLISH.getDisplayLanguage())){
            return pro_name_en;
        }
        return pro_name_ar;
    }



    public boolean compare(CartInfo cartInfo){

        if (cartInfo.pro_price==getPro_price()&&cartInfo.quantity==getQuantity()){
            return true;
        }
        return false;
    }



}
