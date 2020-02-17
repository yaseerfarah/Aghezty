package com.example.aghezty.POJO;

public class SliderInfo {

    private int id;

    private String image;

    private String ads_url;

    private String created_at;

    private String updated_at;

    private String type;

    private int active;

    private int order;

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setImage(String image){
        this.image = image;
    }
    public String getImage(){
        return this.image;
    }
    public void setAds_url(String ads_url){
        this.ads_url = ads_url;
    }
    public String getAds_url(){
        return this.ads_url;
    }
    public void setCreated_at(String created_at){
        this.created_at = created_at;
    }
    public String getCreated_at(){
        return this.created_at;
    }
    public void setUpdated_at(String updated_at){
        this.updated_at = updated_at;
    }
    public String getUpdated_at(){
        return this.updated_at;
    }
    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return this.type;
    }
    public void setActive(int active){
        this.active = active;
    }
    public int getActive(){
        return this.active;
    }
    public void setOrder(int order){
        this.order = order;
    }
    public int getOrder(){
        return this.order;
    }

}
