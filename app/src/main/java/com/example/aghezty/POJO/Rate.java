package com.example.aghezty.POJO;

import com.google.gson.annotations.SerializedName;

public class Rate {

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
}
