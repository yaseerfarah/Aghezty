package com.example.aghezty.POJO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderInfo {

    @SerializedName("id")
   private String orderID;
    @SerializedName("created_at")
    private String orderTime;
    @SerializedName("status")
    private String orderStatus;
    @SerializedName("payment")
    private String paymentMethod;
    @SerializedName("shipping_amount")
    private int shippingAmount;
    @SerializedName("total_price")
    private int orderTotalPrice;

    @SerializedName("products")
    List<CartInfo> orderProductList;

    public int getShippingAmount() {
        return shippingAmount;
    }

    public String getOrderID() {
        return orderID;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public int getOrderTotalPrice() {
        return orderTotalPrice;
    }

    public List<CartInfo> getOrderProductList() {
        return orderProductList;
    }


    public boolean compare(OrderInfo orderInfo){

        if (orderInfo.getOrderStatus().matches(orderStatus)&&orderInfo.getShippingAmount()==shippingAmount){
            return true;
        }
        return false;
    }


}


