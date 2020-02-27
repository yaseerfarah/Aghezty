package com.example.aghezty.Data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.aghezty.POJO.CartInfo;



@Database(entities = {CartInfo.class}, version = 1, exportSchema = false)
public abstract class CartRoomDatabase extends RoomDatabase {

    public abstract DaoCartInfoRoom daoCartInfoRoom();

}
