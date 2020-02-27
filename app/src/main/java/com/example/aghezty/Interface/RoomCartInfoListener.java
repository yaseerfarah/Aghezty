package com.example.aghezty.Interface;

import com.example.aghezty.POJO.CartInfo;

import java.util.List;

public interface RoomCartInfoListener {

    void onSuccess(List<CartInfo> cartInfos);
    void onFailure(Throwable e);

}
