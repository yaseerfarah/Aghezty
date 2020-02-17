package com.example.aghezty.View;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aghezty.Adapter.CategoryCardViewAdapter;
import com.example.aghezty.Adapter.HorizontalRecyclerCardViewAdapter;
import com.example.aghezty.Adapter.SliderAdapter;
import com.example.aghezty.POJO.CategoryInfo;
import com.example.aghezty.POJO.HomeData;
import com.example.aghezty.POJO.HomeRecylerData;
import com.example.aghezty.POJO.ProductInfo;
import com.example.aghezty.POJO.SliderInfo;
import com.example.aghezty.R;
import com.example.aghezty.Util.GridSpacingItemDecoration;
import com.example.aghezty.ViewModel.ProductViewModel;
import com.example.aghezty.ViewModel.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.support.AndroidSupportInjection;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {

    @Inject
    ViewModelFactory viewModelFactory;
    private ProductViewModel productViewModel;
    private Observer homeObserver;
    private ViewPager viewPager;
    private RecyclerView bestcategories,horizontalRecycler;

    private List<HomeRecylerData> homeRecylerDataList=new ArrayList<>();
    private List<CategoryInfo> categoryInfoList=new ArrayList<>();
    private List<SliderInfo> sliderInfoList=new ArrayList<>();

    private NavController navController;
    private SliderAdapter sliderAdapter;
    private HorizontalRecyclerCardViewAdapter horizontalRecyclerCardViewAdapter;
    private CategoryCardViewAdapter categoryCardViewAdapter;


    public Home() {
        // Required empty public constructor

        homeObserver=new Observer<HomeData>() {
            @Override
            public void onChanged(HomeData homeData) {
                homeRecylerDataList.clear();
                homeRecylerDataList.add(new HomeRecylerData("New Arrivals",R.drawable.ic_new,homeData.getRecently_added()));
                homeRecylerDataList.add(new HomeRecylerData("Just For You",R.drawable.ic_gift,homeData.getSelected_for_you()));

                categoryInfoList.clear();
                categoryInfoList.addAll(homeData.getHomepage_cat());

                sliderInfoList.clear();
                sliderInfoList.addAll(homeData.getSlides());


                sliderAdapter.notifyDataSetChanged();
                horizontalRecyclerCardViewAdapter.notifyDataSetChanged();
                categoryCardViewAdapter.notifyDataSetChanged();


            }
        };

    }


    @Override
    public void onStart() {
        super.onStart();
        productViewModel.getHomeData();
        productViewModel.getHomeDataLiveData().observe(this,homeObserver);

    }

    @Override
    public void onStop() {
        super.onStop();
        productViewModel.getHomeDataLiveData().removeObservers(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSupportInjection.inject(this);
        productViewModel=ViewModelProviders.of(this,viewModelFactory).get(ProductViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager=view.findViewById(R.id.view_pager);
        horizontalRecycler=view.findViewById(R.id.horizontal_recycler);
        bestcategories=view.findViewById(R.id.best_categories);
        navController= Navigation.findNavController(view);

        sliderAdapter=new SliderAdapter(getContext(),sliderInfoList);
        categoryCardViewAdapter=new CategoryCardViewAdapter(getContext(),categoryInfoList,navController,CategoryCardViewAdapter.BEST_CATEGORIES);
        horizontalRecyclerCardViewAdapter=new HorizontalRecyclerCardViewAdapter(getContext(),homeRecylerDataList,navController,getResources());

        viewPager.setAdapter(sliderAdapter);
        bestcategories.setAdapter(categoryCardViewAdapter);
        horizontalRecycler.setAdapter(horizontalRecyclerCardViewAdapter);


        horizontalRecycler.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        bestcategories.setLayoutManager(new GridLayoutManager(getContext(),2));
        bestcategories.addItemDecoration(new GridSpacingItemDecoration(2,GridSpacingItemDecoration.dpToPx(5,getResources()),GridSpacingItemDecoration.ListLayout));


    }
}
