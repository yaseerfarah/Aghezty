package com.example.aghezty.POJO;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class AddressInfo implements Parcelable {

    @SerializedName("id")
    private int id;
    @SerializedName("city_id")
    private int city_id;
    @SerializedName("address")
    private String address;
    @SerializedName("city_en")
    private String city_en;
    @SerializedName("city_ar")
    private String city_ar;
    @SerializedName("governorate_en")
    private String governorate_en;
    @SerializedName("governorate_ar")
    private String governorate_ar;


    public AddressInfo() {
        //Empty Constructor
    }

    public AddressInfo(int city_id, String address, String city_en, String city_ar, String governorate_en, String governorate_ar) {
        this.city_id = city_id;
        this.address = address;
        this.city_en = city_en;
        this.city_ar = city_ar;
        this.governorate_en = governorate_en;
        this.governorate_ar = governorate_ar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity_en() {
        return city_en;
    }

    public void setCity_en(String city_en) {
        this.city_en = city_en;
    }

    public String getCity_ar() {
        return city_ar;
    }

    public void setCity_ar(String city_ar) {
        this.city_ar = city_ar;
    }

    public String getGovernorate_en() {
        return governorate_en;
    }

    public void setGovernorate_en(String governorate_en) {
        this.governorate_en = governorate_en;
    }

    public String getGovernorate_ar() {
        return governorate_ar;
    }

    public void setGovernorate_ar(String governorate_ar) {
        this.governorate_ar = governorate_ar;
    }


    public boolean compare(AddressInfo addressInfo){

        if (addressInfo.getAddress().matches(getAddress())&&addressInfo.getCity_ar().matches(getCity_ar())&&addressInfo.getCity_en().matches(getCity_en())&&addressInfo.getGovernorate_ar().matches(getGovernorate_ar())&&addressInfo.getGovernorate_en().matches(getGovernorate_en()))
        {
            return true;
        }
        return false;
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private AddressInfo(Parcel in) {

        id=in.readInt();
        city_id=in.readInt();
        city_ar=in.readString();
        city_en=in.readString();
        governorate_ar=in.readString();
        governorate_en=in.readString();
        address=in.readString();


    }

    public static final Creator<AddressInfo> CREATOR = new Creator<AddressInfo>() {

        @Override
        public AddressInfo createFromParcel(Parcel source) {
            return new AddressInfo(source) ;
        }

        @Override
        public AddressInfo[] newArray(int size) {
            return new AddressInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeInt(city_id);
        dest.writeString(city_ar);
        dest.writeString(city_en);
        dest.writeString(governorate_ar);
        dest.writeString(governorate_en);
        dest.writeString(address);


    }
}
