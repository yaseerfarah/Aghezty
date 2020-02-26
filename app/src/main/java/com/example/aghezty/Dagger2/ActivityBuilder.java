package com.example.aghezty.Dagger2;

import com.example.aghezty.View.LoginActivity;
import com.example.aghezty.View.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;



@Module
public abstract class ActivityBuilder {


    @ContributesAndroidInjector(modules = MainActivityFragments.class)
    abstract MainActivity contributeMainActivity();


    @ContributesAndroidInjector(modules = LoginActivityFragments.class)
    abstract LoginActivity contributeLoginActivity();





}
