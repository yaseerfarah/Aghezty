package com.example.aghezty.Interface;

import com.example.aghezty.POJO.ErrorResponse;

public interface CompletableListener {

     void onSuccess();
     void onFailure(Throwable e);
}
