package com.example.aghezty.Dagger2;

import com.example.aghezty.View.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;



@Module
public abstract class ActivityBuilder {


    @ContributesAndroidInjector(modules = MainActivityFragments.class)
    abstract MainActivity contributeMainActivity();






}
