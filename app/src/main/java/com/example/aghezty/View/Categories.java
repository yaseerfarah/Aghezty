package com.example.aghezty.View;


import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aghezty.Adapter.CategoryCardViewAdapter;
import com.example.aghezty.Adapter.ParentCategoryCardViewAdapter;
import com.example.aghezty.BroadcastReceiver.NetworkReceiver;
import com.example.aghezty.Interface.InternetStatus;
import com.example.aghezty.Interface.OnParentCategoryClick;
import com.example.aghezty.POJO.BrandInfo;
import com.example.aghezty.POJO.CategoryInfo;
import com.example.aghezty.POJO.FilterInfo;
import com.example.aghezty.R;
import com.example.aghezty.Util.GridSpacingItemDecoration;
import com.example.aghezty.ViewModel.ProductViewModel;
import com.example.aghezty.ViewModel.ViewModelFactory;
import com.gturedi.views.CustomStateOptions;
import com.gturedi.views.StatefulLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

import static com.example.aghezty.Adapter.CategoryCardViewAdapter.CATEGORIES;

/**
 * A simple {@link Fragment} subclass.
 */
public class Categories extends Fragment implements OnParentCategoryClick , InternetStatus {


    @Inject
    ViewModelFactory viewModelFactory;
    private ProductViewModel productViewModel;
    private Observer parentCategoriesObserver;
    private Observer brandCategoriesObserver;
    private NavController navController;

    boolean isparentCatDone=false;
    boolean isBrandCatDone=false;

    private List<FilterInfo> parentCategoriesList=new ArrayList<>();
    private List<FilterInfo> categoriesList=new ArrayList<>();


    private List<CategoryInfo> parentSubCategoryList;
    private List<BrandInfo> brandSubCategoryList;

    private ParentCategoryCardViewAdapter parentCategoryCardViewAdapter;
    private CategoryCardViewAdapter categoryCardViewAdapter;

    private NetworkReceiver networkReceiver;
    private CustomStateOptions networkCustom=new CustomStateOptions().image(R.drawable.ic_signal_wifi_off_black_24dp);


    @BindView(R.id.parent_categories_recycler)
    RecyclerView parentCategoryRecycler;
    @BindView(R.id.categories_recycler)
    RecyclerView categoryRecycler;

    @BindView(R.id.stateful)
    StatefulLayout statefulLayout;



    public Categories() {
        // Required empty public constructor

        parentCategoriesObserver=new Observer<List<CategoryInfo>>() {
            @Override
            public void onChanged(List<CategoryInfo> categoryInfos) {
                if (categoryInfos!=null&&!isparentCatDone){
                    parentSubCategoryList=categoryInfos;
                  for (CategoryInfo categoryInfo:categoryInfos){

                      parentCategoriesList.add(new FilterInfo(categoryInfo.getId(),categoryInfo.getTitle_en(),categoryInfo.getTitle_ar(),null,FilterInfo.CATEGORY));

                  }

                  parentCategoryCardViewAdapter.notifyDataSetChanged();
                  isparentCatDone=true;


                }
                progress();
            }

        };

        brandCategoriesObserver=new Observer<List<BrandInfo>>() {
            @Override
            public void onChanged(List<BrandInfo> brandInfos) {
                if (brandInfos != null && !isBrandCatDone) {
                    brandSubCategoryList = brandInfos;
                    parentCategoriesList.add(new FilterInfo(0, getResources().getString(R.string.brand), getResources().getString(R.string.brand),null, FilterInfo.BRAND));

                    parentCategoryCardViewAdapter.notifyDataSetChanged();
                    isBrandCatDone=true;

                }
                progress();
            }

        };


    }



    @Override
    public void onStart() {
        super.onStart();

        IntentFilter netFilter=new IntentFilter();
        netFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        getActivity().registerReceiver(networkReceiver,netFilter);


        productViewModel.getParentCategoriesLiveData().observe(this,parentCategoriesObserver);
        productViewModel.getBrandCategoriesLiveData().observe(this,brandCategoriesObserver);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(networkReceiver);
        productViewModel.getParentCategoriesLiveData().removeObservers(this);
        productViewModel.getBrandCategoriesLiveData().removeObservers(this);

    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSupportInjection.inject(this);
        productViewModel= ViewModelProviders.of(this,viewModelFactory).get(ProductViewModel.class);
        networkReceiver=new NetworkReceiver(this);

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this,view);


        navController= Navigation.findNavController(view);
        parentCategoryCardViewAdapter=new ParentCategoryCardViewAdapter(getContext(),parentCategoriesList,navController,this);
        categoryCardViewAdapter=new CategoryCardViewAdapter(getContext(),categoriesList,navController,productViewModel,CATEGORIES);

        parentCategoryRecycler.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        parentCategoryRecycler.setAdapter(parentCategoryCardViewAdapter);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int displayWidth = displayMetrics.widthPixels;
        categoryRecycler.setLayoutManager(new GridLayoutManager(getContext(),2));
        categoryRecycler.addItemDecoration(new GridSpacingItemDecoration(2,GridSpacingItemDecoration.dpToPx(10,getResources()),GridSpacingItemDecoration.HomeLayout,displayWidth,(int)getResources().getDimension(R.dimen.best_category_card_size)));


        categoryRecycler.setAdapter(categoryCardViewAdapter);



    }


    @Override
    public void onClick(int id,int type) {

        if (type==FilterInfo.CATEGORY){
            getSubCategory(id);

        }else {
            getSubBrand();
        }

    }

    private void getSubCategory(int id){

        categoriesList.clear();
        for (CategoryInfo categoryInfo:parentSubCategoryList){

            if (categoryInfo.getId()==id){

              for (CategoryInfo categoryInfo1:categoryInfo.getSub_cats()){

                  categoriesList.add(new FilterInfo(categoryInfo1.getId(),categoryInfo1.getTitle_en(),categoryInfo1.getTitle_ar(),categoryInfo1.getImage(),FilterInfo.CATEGORY));

              }
              categoryCardViewAdapter.notifyDataSetChanged();

            }

        }

    }


    private void getSubBrand(){

        categoriesList.clear();
        for (BrandInfo brandInfo:brandSubCategoryList){

            categoriesList.add(new FilterInfo(brandInfo.getId(),brandInfo.getTitle_en(),brandInfo.getTitle_ar(),brandInfo.getImage(),FilterInfo.BRAND));

        }
        categoryCardViewAdapter.notifyDataSetChanged();
    }


    private void progress(){

        if (isBrandCatDone&&isparentCatDone){

            statefulLayout.showContent();

        }
    }


    @Override
    public void Connect() {

        productViewModel.getBrandCategories();
        productViewModel.getParentCategories();
        progress();

    }

    @Override
    public void notConnect() {
        statefulLayout.showCustom(networkCustom.message(getResources().getString(R.string.check_connection)));
    }
}
