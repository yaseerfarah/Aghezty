package com.example.aghezty.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavGraph;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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

                    if (menuItem.getItemId()==R.id.offers){
                        productViewModel.setFilter(null,null,ProductViewModel.ALL,true);

                    }

                    if (navController.getCurrentDestination().getId()!=menuItem.getItemId()) {
                        productViewModel.clearApiCall();
                        navController.navigate(menuItem.getItemId(), null, options);
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



    static NavDestination findStartDestination(@NonNull NavGraph graph) {
        NavDestination startDestination = graph;
        while (startDestination instanceof NavGraph) {
            NavGraph parent = (NavGraph) startDestination;
            startDestination = parent.findNode(parent.getStartDestination());
        }
        return startDestination;
    }


}
