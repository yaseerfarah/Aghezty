package com.example.aghezty.View;


import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.aghezty.Adapter.CategoryCardViewAdapter;
import com.example.aghezty.Adapter.HorizontalRecyclerCardViewAdapter;
import com.example.aghezty.Adapter.SliderAdapter;
import com.example.aghezty.BroadcastReceiver.NetworkReceiver;
import com.example.aghezty.Interface.InternetStatus;
import com.example.aghezty.POJO.CategoryInfo;
import com.example.aghezty.POJO.FilterInfo;
import com.example.aghezty.POJO.HomeData;
import com.example.aghezty.POJO.HomeRecylerData;
import com.example.aghezty.POJO.ProductInfo;
import com.example.aghezty.POJO.SliderInfo;
import com.example.aghezty.R;
import com.example.aghezty.Util.GridSpacingItemDecoration;
import com.example.aghezty.ViewModel.ProductViewModel;
import com.example.aghezty.ViewModel.ViewModelFactory;
import com.gturedi.views.CustomStateOptions;
import com.gturedi.views.StatefulLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.support.AndroidSupportInjection;
import es.dmoral.toasty.Toasty;

import static com.example.aghezty.Adapter.SliderAdapter.HOME;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment implements InternetStatus {

    @Inject
    ViewModelFactory viewModelFactory;
    private ProductViewModel productViewModel;
    private Observer homeObserver;

    @BindView(R.id.view_pager)
     ViewPager viewPager;
    @BindView(R.id.best_categories)
     RecyclerView bestcategories;
    @BindView(R.id.horizontal_recycler)
     RecyclerView horizontalRecycler;
    @BindView(R.id.stateful)
     StatefulLayout statefulLayout;

    private List<HomeRecylerData> homeRecylerDataList=new ArrayList<>();
    private List<FilterInfo> categoryInfoList=new ArrayList<>();
    private List<String> sliderInfoList=new ArrayList<>();

    private CustomStateOptions networkCustom=new CustomStateOptions().image(R.drawable.ic_signal_wifi_off_black_24dp);

    private NavController navController;
    private SliderAdapter sliderAdapter;
    private HorizontalRecyclerCardViewAdapter horizontalRecyclerCardViewAdapter;
    private CategoryCardViewAdapter categoryCardViewAdapter;
    private TextView[] dots;
    private LinearLayout linearLayout;
    private int page=0;
    private Timer timer;
    private boolean isStart=false;
    private NetworkReceiver networkReceiver;


    public Home() {
        // Required empty public constructor

        homeObserver=new Observer<HomeData>() {
            @Override
            public void onChanged(HomeData homeData) {
                homeRecylerDataList.clear();
                homeRecylerDataList.add(new HomeRecylerData("New Arrivals",R.drawable.ic_new,homeData.getRecently_added()));
                homeRecylerDataList.add(new HomeRecylerData("Just For You",R.drawable.ic_gift,homeData.getSelected_for_you()));

                categoryInfoList.clear();
                for (CategoryInfo categoryInfo:homeData.getHomepage_cat()){

                    categoryInfoList.add(new FilterInfo(categoryInfo.getId(),categoryInfo.getTitle_en(),categoryInfo.getImage(),FilterInfo.CATEGORY));
                }



                sliderInfoList.clear();
                sliderInfoList.addAll(convertToStringList(homeData.getSlides()));


                sliderAdapter.notifyDataSetChanged();
                horizontalRecyclerCardViewAdapter.notifyDataSetChanged();
                categoryCardViewAdapter.notifyDataSetChanged();

               statefulLayout.showContent();

                add_Dots(getContext(),0);

                viewpager_timer(5);

            }
        };

    }


    @Override
    public void onStart() {
        super.onStart();

        IntentFilter netFilter=new IntentFilter();
        netFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        getActivity().registerReceiver(networkReceiver,netFilter);

        horizontalRecycler.setAdapter(horizontalRecyclerCardViewAdapter);

        productViewModel.getHomeDataLiveData().observe(this,homeObserver);

    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(networkReceiver);
        horizontalRecycler.setAdapter(null);
        cancel_timer();
        productViewModel.getHomeDataLiveData().removeObservers(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSupportInjection.inject(this);
        productViewModel=ViewModelProviders.of(this,viewModelFactory).get(ProductViewModel.class);
        networkReceiver=new NetworkReceiver(this);

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
        ButterKnife.bind(this,view);
        linearLayout=view.findViewById(R.id.linear);

        navController= Navigation.findNavController(view);




        sliderAdapter=new SliderAdapter(getContext(),sliderInfoList,getActivity(),HOME);
        categoryCardViewAdapter=new CategoryCardViewAdapter(getContext(),categoryInfoList,navController,productViewModel,CategoryCardViewAdapter.BEST_CATEGORIES);
        horizontalRecyclerCardViewAdapter=new HorizontalRecyclerCardViewAdapter(getContext(),homeRecylerDataList,navController,getResources());


        viewPager.setAdapter(sliderAdapter);
        bestcategories.setAdapter(categoryCardViewAdapter);



        horizontalRecycler.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int displayWidth = displayMetrics.widthPixels;


        bestcategories.setLayoutManager(new GridLayoutManager(getContext(),2));
        bestcategories.addItemDecoration(new GridSpacingItemDecoration(2,GridSpacingItemDecoration.dpToPx(10,getResources()),GridSpacingItemDecoration.Category,displayWidth,(int)getResources().getDimension(R.dimen.best_category_card_size)));


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                page=viewPager.getCurrentItem();
                add_Dots(getContext(),position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        // Finch App if Back Button Pressed

        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getActivity().finish();
            }
        });


    }



    private void add_Dots(Context context, int i){
        dots=new TextView[sliderAdapter.getCount()];
        linearLayout.removeAllViews();
        for (int b=0;b<dots.length;b++){
            dots[b]=new TextView(context);
            dots[b].setText(Html.fromHtml("&#8226"));
            dots[b].setTextSize(35);
            dots[b].setTextColor(getResources().getColor(R.color.grey));
            linearLayout.addView(dots[b]);
        }

        dots[i].setTextColor(getResources().getColor(R.color.orange));

    }





    private List<String> convertToStringList(List<SliderInfo> sliderInfos){
        List<String> stringList=new ArrayList<>();


        for (SliderInfo sliderInfo:sliderInfos){

            stringList.add(sliderInfo.getImage());

        }

        return stringList;

    }


    /// timer for viewpager///////////

    private void viewpager_timer(int second){

        // page=viewPager.getCurrentItem();
        // Toast.makeText(getActivity(),"timer",Toast.LENGTH_LONG).show();
        if (!isStart) {
            isStart=true;
            timer = new Timer();
            timer.schedule(new Timer_task(), 5 * 1000, second * 1000);
        }



    }


    ////////// cancel Timer//////////////////

    public void cancel_timer(){
        if (timer!=null) {
            timer.cancel();
            isStart = false;
        }

    }

    @Override
    public void Connect() {

        if (sliderInfoList.isEmpty()){
            productViewModel.getHomeData();
            statefulLayout.showLoading(" ");
        }else {
            statefulLayout.showContent();
        }


    }

    @Override
    public void notConnect() {

        statefulLayout.showCustom(networkCustom.message("Oooopss...  Check your Connection"));

    }


    //////////////////////////////// inner Class for Timer Task//////////////////////////////////////////////


    class Timer_task extends TimerTask {





        @Override
        public void run() {

            if(getActivity()!=null){

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if(page>sliderAdapter.getCount()){
                            page=0;
                        }
                        else {
                            page++;
                        }
                        viewPager.setCurrentItem(page);
                    }
                });
            }



        }
    }





}
