package com.example.aghezty.POJO;

public class PayPalPaymentDetails {

    String paymentID;
    String paymentState;
    String PaymentAmount;

    public PayPalPaymentDetails(){

    }

    public PayPalPaymentDetails(String paymentID, String paymentState, String paymentAmount) {
        this.paymentID = paymentID;
        this.paymentState = paymentState;
        PaymentAmount = paymentAmount;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public String getPaymentState() {
        return paymentState;
    }

    public void setPaymentState(String paymentState) {
        this.paymentState = paymentState;
    }

    public String getPaymentAmount() {
        return PaymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        PaymentAmount = paymentAmount;
    }
}
