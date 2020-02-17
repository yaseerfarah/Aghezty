package com.example.aghezty.POJO;

import java.util.List;

public class HomeRecylerData {

    private String name;
    private int iconId;
    private List<ProductInfo> productInfos;


    public HomeRecylerData(String name, int iconId, List<ProductInfo> productInfos) {
        this.name = name;
        this.iconId = iconId;
        this.productInfos = productInfos;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public List<ProductInfo> getProductInfos() {
        return productInfos;
    }

    public void setProductInfos(List<ProductInfo> productInfos) {
        this.productInfos = productInfos;
    }
}
