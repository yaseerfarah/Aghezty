package com.example.aghezty.POJO;

import com.google.gson.annotations.SerializedName;

public class UserInfo {

    private String token;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("image")
    private String imgUrl;
    @SerializedName("phone")
    private String phoneNumber;
    @SerializedName("city_id")
    private int cityId;
    private String city;
    private int governoratePosition;
    private String governorate;
    @SerializedName("address")
    private String address;
    @SerializedName("password")
    private String password;
    @SerializedName("c_password")
    private String confirm_password;









    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        setConfirm_password(password);
    }


    public String getConfirm_password() {
        return confirm_password;
    }

    public void setConfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
    }


    public String getGovernorate() {
        return governorate;
    }

    public void setGovernorate(String governorate) {
        this.governorate = governorate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }



    public int getGovernoratePosition() {
        return governoratePosition;
    }

    public void setGovernoratePosition(int governoratePosition) {
        this.governoratePosition = governoratePosition;
    }


    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
