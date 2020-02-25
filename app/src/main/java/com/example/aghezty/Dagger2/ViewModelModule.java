package com.example.aghezty.Dagger2;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.example.aghezty.Data.AgheztyApi;
import com.example.aghezty.Data.SharedPreferencesMethod;
import com.example.aghezty.ViewModel.ProductViewModel;
import com.example.aghezty.ViewModel.UserViewModel;
import com.example.aghezty.ViewModel.ViewModelFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

import javax.inject.Provider;
import javax.inject.Singleton;

import dagger.MapKey;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

@Module
public class ViewModelModule {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @MapKey
    @interface ViewModelKey {
        Class<? extends ViewModel> value();
    }



    @Singleton
    @Provides
    ViewModelFactory viewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> providerMap) {

        return new ViewModelFactory(providerMap);
    }

    @Provides
    @Singleton
    @IntoMap
    @ViewModelKey(ProductViewModel.class)
    ViewModel productViewModel(Context context, AgheztyApi agheztyApi) {
        return new ProductViewModel(context,agheztyApi);
    }



    @Provides
    @Singleton
    @IntoMap
    @ViewModelKey(UserViewModel.class)
    ViewModel userViewModel(Context context, SharedPreferencesMethod sharedPreferencesMethod) {
        return new UserViewModel(context,sharedPreferencesMethod);
    }







}
