package com.example.aghezty.Dagger2;

import android.widget.EditText;

import com.example.aghezty.View.AddNewAddress;
import com.example.aghezty.View.Cart;
import com.example.aghezty.View.Categories;
import com.example.aghezty.View.ChangePassword;
import com.example.aghezty.View.CheckOut;
import com.example.aghezty.View.Confirm;
import com.example.aghezty.View.EditProfile;
import com.example.aghezty.View.Filter;
import com.example.aghezty.View.Home;
import com.example.aghezty.View.MyAddresses;
import com.example.aghezty.View.Offers;
import com.example.aghezty.View.Order;
import com.example.aghezty.View.Payment;
import com.example.aghezty.View.ProductDetails;
import com.example.aghezty.View.ProductList;
import com.example.aghezty.View.Profile;
import com.example.aghezty.View.Search;
import com.example.aghezty.View.Shipping;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;


@Module
public abstract class MainActivityFragments {


    @ContributesAndroidInjector()
    abstract Home contributeHomeFragment();

    @ContributesAndroidInjector()
    abstract ProductList contributeProductListFragment();

    @ContributesAndroidInjector()
    abstract Cart contributeCartFragment();

    @ContributesAndroidInjector()
    abstract Profile contributeProfileFragment();

    @ContributesAndroidInjector()
    abstract EditProfile contributeEditProfileFragment();

    @ContributesAndroidInjector()
    abstract ChangePassword contributeChangePasswordFragment();

    @ContributesAndroidInjector()
    abstract AddNewAddress contributeAddNewAddressFragment();

    @ContributesAndroidInjector()
    abstract MyAddresses contributeMyAddressesFragment();

    @ContributesAndroidInjector()
    abstract Categories contributeCategoriesFragment();

    @ContributesAndroidInjector()
    abstract ProductDetails contributeProductDetailsFragment();

    @ContributesAndroidInjector()
    abstract CheckOut contributeCheckoutFragment();

    @ContributesAndroidInjector()
    abstract Confirm contributeConfirmFragment();


    @ContributesAndroidInjector()
    abstract Shipping contributeShippingFragment();


    @ContributesAndroidInjector()
    abstract Payment contributePaymentFragment();


    @ContributesAndroidInjector()
    abstract Order contributeOrderFragment();



    @ContributesAndroidInjector()
    abstract Filter contributeFilterFragment();



    @ContributesAndroidInjector()
    abstract Search contributeSearchFragment();



    @ContributesAndroidInjector()
    abstract Offers contributeOffersFragment();


}
