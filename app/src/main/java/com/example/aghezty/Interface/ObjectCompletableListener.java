package com.example.aghezty.Interface;

public interface ObjectCompletableListener<T> {
    void onSuccess(T completeObject);
    void onFailure(String message);
}
