package com.example.aghezty.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.aghezty.R;
import com.example.aghezty.ViewModel.ProductViewModel;
import com.example.aghezty.ViewModel.ViewModelFactory;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector {


    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    @Inject
    ViewModelFactory viewModelFactory;

    private ProductViewModel productViewModel;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        setContentView(R.layout.activity_main);

        productViewModel= ViewModelProviders.of(this,viewModelFactory).get(ProductViewModel.class);


        bottomNavigationView=findViewById(R.id.bottom_nav);


        NavController navController= Navigation.findNavController(this,R.id.fragment);

        NavigationUI.setupWithNavController(bottomNavigationView,navController);





    }


    @Override
    protected void onStart() {
        super.onStart();
        productViewModel.getBrandCategories();
        productViewModel.getParentCategories();

    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }


}
