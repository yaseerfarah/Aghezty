package com.example.aghezty.POJO;

import java.util.List;

public class OrderInfo {

    String orderID;
    String orderTime;
    String orderStatus;
    String paymentMethod;
    String shippingAmount;
    int orderTotalPrice;
    List<CartInfo> orderProductList;

    public String getShippingAmount() {
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
}


