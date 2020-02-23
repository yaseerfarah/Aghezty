package com.example.aghezty.POJO;

public class FilterInfo {

    static public final int CATEGORY=0;
    static public final int BRAND=1;

    int id;
    String name;
    String imageUrl;
    int type;

    public FilterInfo(int id, String name,String imageUrl, int type) {
        this.id = id;
        this.name = name;
        this.imageUrl=imageUrl;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
