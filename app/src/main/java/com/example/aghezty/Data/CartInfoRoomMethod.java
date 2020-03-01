package com.example.aghezty.Data;

import android.content.Context;

import com.example.aghezty.Interface.CompletableListener;
import com.example.aghezty.Interface.RoomCartInfoListener;
import com.example.aghezty.POJO.CartInfo;

import java.util.List;
import java.util.function.Consumer;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CartInfoRoomMethod {

    private Context context;
    private CartRoomDatabase cartRoomDatabase;


    @Inject
    public CartInfoRoomMethod(Context context, CartRoomDatabase cartRoomDatabase) {
        this.context = context;
        this.cartRoomDatabase = cartRoomDatabase;
    }




    public void insertSticker(CartInfo cartInfo, CompletableListener completableListener){

        Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                try {

                   long i= cartRoomDatabase.daoCartInfoRoom().insertCartInfo(cartInfo);
                   // Toast.makeText(context,i+"/",Toast.LENGTH_LONG).show();

                    if (!emitter.isDisposed()) {
                        emitter.onComplete();
                    }

                }catch (Exception e){
                    if (!emitter.isDisposed()) {
                        emitter.onError(e);
                    }
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        completableListener.onSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        completableListener.onFailure(e.getMessage());
                    }
                });


    }



    public void getAllCarts(RoomCartInfoListener roomCartInfoListener){

        Single.create(new SingleOnSubscribe<List<CartInfo>>() {
            @Override
            public void subscribe(SingleEmitter<List<CartInfo>> emitter) throws Exception {
                try {

                    List<CartInfo> cartInfoList= cartRoomDatabase.daoCartInfoRoom().fetchAllCart();
                    if (!emitter.isDisposed()) {
                        emitter.onSuccess(cartInfoList);
                    }

                }catch (Exception e){
                    if (!emitter.isDisposed()) {
                        emitter.onError(e);
                    }
                }
            }
        }
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<CartInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<CartInfo> cartInfos) {
                      roomCartInfoListener.onSuccess(cartInfos);
                    }

                    @Override
                    public void onError(Throwable e) {
                        roomCartInfoListener.onFailure(e);
                    }
                });

    }




    public void deleteStickerPack(CartInfo cartInfo, CompletableListener completableListener){

        Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                try {

                    cartRoomDatabase.daoCartInfoRoom().deleteCartInfo(cartInfo);
                    if (!emitter.isDisposed()) {
                        emitter.onComplete();
                    }

                }catch (Exception e){
                    if (!emitter.isDisposed()) {
                        emitter.onError(e);
                    }
                }
            }
        }).subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                       completableListener.onSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        completableListener.onFailure(e.getMessage());
                    }
                });

    }



}
