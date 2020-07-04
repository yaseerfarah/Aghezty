package com.example.aghezty.Interface;

import java.util.List;

public interface ListCompletableListener<T> {
    void onSuccess(List<T> completeList);
    void onFailure(String message);
}
