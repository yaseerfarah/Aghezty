package com.example.aghezty.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavGraph;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.aghezty.POJO.CartInfo;
import com.example.aghezty.R;
import com.example.aghezty.ViewModel.ProductViewModel;
import com.example.aghezty.ViewModel.UserViewModel;
import com.example.aghezty.ViewModel.ViewModelFactory;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import q.rorbin.badgeview.QBadgeView;

import static com.example.aghezty.Constants.localeLanguage;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector {


    private static final int REQUEST_PERMISSION=200;



    private final Observer cartObserver;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    @Inject
    ViewModelFactory viewModelFactory;

    private ProductViewModel productViewModel;
    private UserViewModel userViewModel;

    private BottomNavigationView bottomNavigationView;

    private QBadgeView cartBadge;


    public MainActivity() {
        cartObserver=new Observer<List<CartInfo>>() {
            @Override
            public void onChanged(List<CartInfo> cartInfos) {
                if (!cartInfos.isEmpty()) {
                   addBadge(cartInfos.size());
                }else {
                    removeBadge();
                }
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);

        setContentView(R.layout.activity_main);

        productViewModel= ViewModelProviders.of(this,viewModelFactory).get(ProductViewModel.class);
        userViewModel= ViewModelProviders.of(this,viewModelFactory).get(UserViewModel.class);


        bottomNavigationView=findViewById(R.id.bottom_nav);



        NavController navController= Navigation.findNavController(this,R.id.fragment);

        //NavigationUI.setupWithNavController(bottomNavigationView,navController);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                NavOptions.Builder builder = new NavOptions.Builder()
                        .setLaunchSingleTop(false)
                        .setEnterAnim(R.anim.nav_default_enter_anim)
                        .setExitAnim(R.anim.nav_default_exit_anim)
                        .setPopEnterAnim(R.anim.nav_default_pop_enter_anim)
                        .setPopExitAnim(R.anim.nav_default_pop_exit_anim);
                if ((menuItem.getOrder() & Menu.CATEGORY_SECONDARY) == 0) {
                    builder.setPopUpTo(findStartDestination(navController.getGraph()).getId(), false);
                }

                NavOptions options = builder.build();
                try {
                    //TODO provide proper API instead of using Exceptions as Control-Flow.


                    if (navController.getCurrentDestination().getId()!=menuItem.getItemId()) {

                        if (menuItem.getItemId()==R.id.cart||menuItem.getItemId()==R.id.profile){

                            if (userViewModel.isLogin()) {
                               // Log.e("Password",userViewModel.getCurrentUserInfo().getPassword());
                                productViewModel.clearApiCall();
                                navController.navigate(menuItem.getItemId(), null, options);
                            }
                            else {
                                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                                return false;
                            }

                        }else {

                            productViewModel.clearApiCall();
                            productViewModel.setFilter(null,null,ProductViewModel.ALL,ProductViewModel.ALL,true);
                            navController.navigate(menuItem.getItemId(), null, options);
                        }


                    }


                    return true;
                } catch (IllegalArgumentException e) {
                    return false;
                }
            }
        });



        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId()==R.id.home){

                    bottomNavigationView.getMenu().getItem(0).setChecked(true);

                }
            }
        });


        cartBadge=(QBadgeView) new QBadgeView(this)
                .setBadgeTextSize(getResources().getDimension(R.dimen.cart_qbadge),true)
                .setGravityOffset(12, 2, true)
                .bindTarget(bottomNavigationView.findViewById(R.id.cart));






        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED  ) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        }



    }


    @Override
    protected void onStart() {
        super.onStart();
        userViewModel.getAllCart();
        productViewModel.getBrandCategories();
        productViewModel.getParentCategories();
        userViewModel.getCartListMediatorLiveData().observe(this,cartObserver);

    }


    @Override
    protected void onStop() {
        super.onStop();
        userViewModel.getCartListMediatorLiveData().removeObservers(this);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }







    private void addBadge(int number) {
        // add badge
        cartBadge.setVisibility(View.VISIBLE);
        cartBadge.setBadgeNumber(number);
    }

    private void removeBadge() {
        cartBadge.hide(true);
        cartBadge.setVisibility(View.INVISIBLE);
    }

    static NavDestination findStartDestination(@NonNull NavGraph graph) {
        NavDestination startDestination = graph;
        while (startDestination instanceof NavGraph) {
            NavGraph parent = (NavGraph) startDestination;
            startDestination = parent.findNode(parent.getStartDestination());
        }
        return startDestination;
    }











    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(updateBaseContextLocale(base));

    }

    private Context updateBaseContextLocale(Context context) {

        Locale.setDefault(localeLanguage);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResourcesLocale(context, localeLanguage);
        }

        return updateResourcesLocaleLegacy(context, localeLanguage);
    }


    private Context updateResourcesLocale(Context context, Locale locale) {
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);
        return context.createConfigurationContext(configuration);
    }

    @SuppressWarnings("deprecation")
    private Context updateResourcesLocaleLegacy(Context context, Locale locale) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        configuration.setLayoutDirection(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return context;
    }








}
