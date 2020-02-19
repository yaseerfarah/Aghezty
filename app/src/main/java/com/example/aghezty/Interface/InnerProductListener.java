package com.example.aghezty.Interface;

import com.example.aghezty.POJO.ProductInfo;

public interface InnerProductListener {

    void onSuccess(ProductInfo productInfo);
    void onFailure(Exception e);


}
