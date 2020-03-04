package com.example.aghezty.POJO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import butterknife.BindView;

public class CheckOutInfo {

    @SerializedName("payment")
    private int paymentId;
    private String paymentMethod;
    @SerializedName("address_id")
    private int addressId;
    @SerializedName("carts")
    private List<CartInfo> cartInfoList;



    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getAddressId() {
        return addressId;
    }


    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public List<CartInfo> getCartInfoList() {
        return cartInfoList;
    }

    public void setCartInfoList(List<CartInfo> cartInfoList) {
        this.cartInfoList = cartInfoList;
    }
}
