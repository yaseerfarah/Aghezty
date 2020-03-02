package com.example.aghezty.Data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.aghezty.POJO.CartInfo;

import java.util.List;


@Dao
public interface DaoCartInfoRoom {


    @Insert
    Long insertCartInfo(CartInfo cartInfo);

    @Query("SELECT * FROM CartInfo")
    List<CartInfo> fetchAllCart();

    @Delete
    void deleteCartInfo(CartInfo cartInfo);

    @Query("DELETE FROM CartInfo")
    void deleteAllCartInfo();

}
