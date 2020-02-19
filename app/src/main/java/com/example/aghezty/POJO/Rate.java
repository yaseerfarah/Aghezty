package com.example.aghezty.POJO;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Rate implements Parcelable {

    @SerializedName("rate")
    private int rate;
    @SerializedName("comment")
    private String comment;
    @SerializedName("name")
    private String name;
    @SerializedName("created")
    private String created;


    public Rate(int rate, String comment, String name, String created) {
        this.rate = rate;
        this.comment = comment;
        this.name = name;
        this.created = created;
    }


    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }




    private Rate(Parcel in){

        rate=in.readInt();
        comment=in.readString();
        name=in.readString();
        created=in.readString();

    }

    public static final Creator<Rate> CREATOR = new Creator<Rate>() {
        @Override
        public Rate createFromParcel(Parcel source) {
            return new Rate(source);
        }

        @Override
        public Rate[] newArray(int size) {
            return new Rate[size];
        }
    };




    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(rate);
        dest.writeString(comment);
        dest.writeString(name);
        dest.writeString(created);


    }
}
