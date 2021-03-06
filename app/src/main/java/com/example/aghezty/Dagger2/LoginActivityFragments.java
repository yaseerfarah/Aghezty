package com.example.aghezty.Dagger2;

import com.example.aghezty.View.Cart;
import com.example.aghezty.View.Categories;
import com.example.aghezty.View.CheckOut;
import com.example.aghezty.View.Confirm;
import com.example.aghezty.View.Filter;
import com.example.aghezty.View.Home;
import com.example.aghezty.View.Login;
import com.example.aghezty.View.Offers;
import com.example.aghezty.View.Order;
import com.example.aghezty.View.Payment;
import com.example.aghezty.View.ProductDetails;
import com.example.aghezty.View.ProductList;
import com.example.aghezty.View.Profile;
import com.example.aghezty.View.Register1;
import com.example.aghezty.View.Register2;
import com.example.aghezty.View.Search;
import com.example.aghezty.View.Shipping;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;


@Module
public abstract class LoginActivityFragments {


    @ContributesAndroidInjector()
    abstract Login contributeHomeFragment();

    @ContributesAndroidInjector()
    abstract Register1 contributeRegister1();

    @ContributesAndroidInjector()
    abstract Register2 contributeRegister2();




}
