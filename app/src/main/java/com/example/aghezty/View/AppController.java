package com.example.aghezty.View;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import androidx.core.os.ConfigurationCompat;
import androidx.lifecycle.ViewModelProviders;

import com.example.aghezty.Dagger2.Component.DaggerAppComponent;
import com.example.aghezty.Data.SharedPreferencesMethod;
import com.example.aghezty.ViewModel.UserViewModel;
import com.example.aghezty.ViewModel.ViewModelFactory;

import java.util.Locale;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

import static com.example.aghezty.Constants.LOCALE_LANGUAGE;
import static com.example.aghezty.Constants.localeLanguage;


public class AppController extends Application implements HasActivityInjector
{

   // public static Locale localeLanguage;

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;
    @Inject
    SharedPreferencesMethod sharedPreferencesMethod;










    @Override
    public void onCreate() {
        super.onCreate();

        DaggerAppComponent.builder()
                .application(this)
                .appContext(getApplicationContext())
                .build()
                .inject(this);


       // localeLanguage= ConfigurationCompat.getLocales(Resources.getSystem().getConfiguration()).get(0);

        if (sharedPreferencesMethod.getString(LOCALE_LANGUAGE).length()>0) {
            localeLanguage = new Locale(sharedPreferencesMethod.getString(LOCALE_LANGUAGE));
        }else {
            localeLanguage = Locale.getDefault();
        }

        Toast.makeText(AppController.this,localeLanguage.getLanguage(),Toast.LENGTH_LONG).show();






    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }





}
