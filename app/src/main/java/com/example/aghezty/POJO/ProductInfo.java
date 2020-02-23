package com.example.aghezty.POJO;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductInfo implements Parcelable {

    @SerializedName("id")
    private int id;
    @SerializedName("title_en")
    private String title_en;
    @SerializedName("title_ar")
    private String title_ar;
    @SerializedName("main_image")
    private String main_image;
    @SerializedName("price")
    private int price;
    @SerializedName("discount")
    private String discount;
    @SerializedName("price_after_discount")
    private int price_after_discount;
    @SerializedName("special")
    private int special;
    @SerializedName("active")
    private int active;
    @SerializedName("description_en")
    private String description_en;
    @SerializedName("description_ar")
    private String description_ar;
    @SerializedName("short_description_en")
    private String short_description_en;
    @SerializedName("short_description_ar")
    private String short_description_ar;
    @SerializedName("category_id")
    private int category_id;
    @SerializedName("brand_id")
    private int brand_id;
    @SerializedName("stock")
    private int stock;
    @SerializedName("stars")
    private int stars;
    @SerializedName("rates")
    private List<Rate> rates;

    public List<Rate> getRates() {
        return rates;
    }




    public void setRates(List<Rate> rates) {
        this.rates = rates;
    }

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setTitle_en(String title_en){
        this.title_en = title_en;
    }
    public String getTitle_en(){
        return this.title_en;
    }
    public void setTitle_ar(String title_ar){
        this.title_ar = title_ar;
    }
    public String getTitle_ar(){
        return this.title_ar;
    }
    public void setMain_image(String main_image){
        this.main_image = main_image;
    }
    public String getMain_image(){
        return this.main_image;
    }
    public void setPrice(int price){
        this.price = price;
    }
    public int getPrice(){
        return this.price;
    }
    public void setDiscount(String discount){
        this.discount = discount;
    }
    public String getDiscount(){
        return this.discount;
    }
    public void setPrice_after_discount(int price_after_discount){
        this.price_after_discount = price_after_discount;
    }
    public int getPrice_after_discount(){
        return this.price_after_discount;
    }
    public void setSpecial(int special){
        this.special = special;
    }
    public int getSpecial(){
        return this.special;
    }
    public void setActive(int active){
        this.active = active;
    }
    public int getActive(){
        return this.active;
    }
    public void setDescription_en(String description_en){
        this.description_en = description_en;
    }
    public String getDescription_en(){
        return this.description_en;
    }
    public void setDescription_ar(String description_ar){
        this.description_ar = description_ar;
    }
    public String getDescription_ar(){
        return this.description_ar;
    }
    public void setShort_description_en(String short_description_en){
        this.short_description_en = short_description_en;
    }
    public String getShort_description_en(){
        return this.short_description_en;
    }
    public void setShort_description_ar(String short_description_ar){
        this.short_description_ar = short_description_ar;
    }
    public String getShort_description_ar(){
        return this.short_description_ar;
    }
    public void setCategory_id(int category_id){
        this.category_id = category_id;
    }
    public int getCategory_id(){
        return this.category_id;
    }
    public void setBrand_id(int brand_id){
        this.brand_id = brand_id;
    }
    public int getBrand_id(){
        return this.brand_id;
    }
    public void setStock(int stock){
        this.stock = stock;
    }
    public int getStock(){
        return this.stock;
    }
    public void setStars(int stars){
        this.stars = stars;
    }
    public int getStars(){
        return this.stars;
    }




    private ProductInfo(Parcel in){

        id=in.readInt();
        title_en=in.readString();
        title_ar=in.readString();
        main_image=in.readString();
        price=in.readInt();
        discount=in.readString();
        price_after_discount=in.readInt();
        special=in.readInt();
        active=in.readInt();
        description_en=in.readString();
        description_ar=in.readString();
        short_description_ar=in.readString();
        short_description_en=in.readString();
        category_id=in.readInt();
        brand_id=in.readInt();
        stock=in.readInt();
        stars=in.readInt();
        rates=in.createTypedArrayList(Rate.CREATOR);

    }




    public boolean compare(ProductInfo productInfo){

        if (productInfo.getTitle_en().trim().matches(getTitle_en().trim())&&productInfo.getStock()==getStock()&&productInfo.getPrice_after_discount()==getPrice_after_discount()&&productInfo.getPrice()==getPrice()){

            return true;
        }
        return false;

    }


    /////////////////////////////////////////Parcelable Implement///////////////////////////////////////


    public static final Creator<ProductInfo> CREATOR = new Creator<ProductInfo>() {

        @Override
        public ProductInfo createFromParcel(Parcel source) {
            return new ProductInfo(source);
        }

        @Override
        public ProductInfo[] newArray(int size) {
            return new ProductInfo[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title_en);
        dest.writeString(title_ar);
        dest.writeString(main_image);
        dest.writeInt(price);
        dest.writeString(discount);
        dest.writeInt(price_after_discount);
        dest.writeInt(special);
        dest.writeInt(active);
        dest.writeString(description_en);
        dest.writeString(description_ar);
        dest.writeString(short_description_ar);
        dest.writeString(short_description_en);
        dest.writeInt(category_id);
        dest.writeInt(brand_id);
        dest.writeInt(stock);
        dest.writeInt(stars);
        dest.writeTypedList(rates);


    }


}
