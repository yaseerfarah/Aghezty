package com.example.aghezty.Dagger2.Component;

import android.app.Application;
import android.content.Context;

import com.example.aghezty.Dagger2.ActivityBuilder;
import com.example.aghezty.Dagger2.RepositoryModule;
import com.example.aghezty.Dagger2.ViewModelModule;
import com.example.aghezty.View.AppController;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;


@Singleton
@Component(modules = {ViewModelModule.class, ActivityBuilder.class, AndroidSupportInjectionModule.class, RepositoryModule.class})
public interface AppComponent {

    @Component.Builder
    interface Builder{

        @BindsInstance
        Builder application(Application application);

        @BindsInstance
        Builder appContext(Context context);

        AppComponent build();

    }



    void inject(AppController appController);

}
