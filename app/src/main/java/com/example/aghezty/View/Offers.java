package com.example.aghezty.View;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aghezty.Adapter.FilterOrderCardViewAdapter;
import com.example.aghezty.Adapter.ProductCardViewAdapter;
import com.example.aghezty.POJO.FilterInfo;
import com.example.aghezty.POJO.ProductFilterData;
import com.example.aghezty.POJO.ProductInfo;
import com.example.aghezty.R;
import com.example.aghezty.Util.GridSpacingItemDecoration;
import com.example.aghezty.ViewModel.ProductViewModel;
import com.example.aghezty.ViewModel.ViewModelFactory;
import com.gturedi.views.StatefulLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

import static com.example.aghezty.Adapter.FilterOrderCardViewAdapter.ORDER;
import static com.example.aghezty.Adapter.ProductCardViewAdapter.LIST;

/**
 * A simple {@link Fragment} subclass.
 */
public class Offers extends Fragment {


    @Inject
    ViewModelFactory viewModelFactory;
    private ProductViewModel productViewModel;
    private Observer listObserver;
    private NavController navController;

    private final String TAG=getClass().getName();

    private boolean isLoading=false;
    private RecyclerView listRecycler;
    private RelativeLayout orderBy,filterBy;
    private StatefulLayout statefulLayout;
    private RelativeLayout root;
    private NestedScrollView nestedScrollView;
    private ProductFilterData productFilterData;

    private ProductCardViewAdapter productCardViewAdapter;

    private List<ProductInfo> productInfoList=new ArrayList<>();
    private ProgressBar progressBar;


    public Offers() {

        listObserver=new Observer<ProductFilterData>() {
            @Override
            public void onChanged(ProductFilterData productFilterData1) {

                progressBar.setVisibility(View.INVISIBLE);

                if (productFilterData1!=null) {
                    productFilterData = productFilterData1;
                    // productInfoList.clear();
                    //productInfoList.addAll(productFilterData1.getProductList());
                    //productCardViewAdapter.notifyDataSetChanged();

                    productCardViewAdapter.updateProductList(productFilterData1.getProductList());


                    isLoading = false;
                    // Toast.makeText(getContext(),"onchange",Toast.LENGTH_SHORT).show();
                    root.setVisibility(View.VISIBLE);

                    if (productFilterData.getProductList().size()>0){
                        statefulLayout.showContent();
                    }else {
                        statefulLayout.showEmpty("No Match");
                    }





                }else {

                    statefulLayout.showEmpty("No Data");
                }




            }
        };


    }


    @Override
    public void onStart() {
        super.onStart();
        //productViewModel.setFilter(null,null,ProductViewModel.ALL,true);
        productViewModel.getProductFilter();
        productViewModel.getProductFilterLiveData().observe(this,listObserver);
        //Toast.makeText(getContext(),"OnStart",Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onResume() {
        super.onResume();

        // Toast.makeText(getContext(),String.valueOf(productCardViewAdapter.getItemCount()),Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onStop() {
        super.onStop();
        productViewModel.getProductFilterLiveData().removeObservers(this);
        listRecycler.setAdapter(null);

    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSupportInjection.inject(this);
        productViewModel= ViewModelProviders.of(this,viewModelFactory).get(ProductViewModel.class);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_offers, container, false);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Toast.makeText(getContext(),"onViewCreated",Toast.LENGTH_SHORT).show();
        orderBy=view.findViewById(R.id.order_by);
        filterBy=view.findViewById(R.id.filter_by);
        statefulLayout=view.findViewById(R.id.stateful);
        progressBar=view.findViewById(R.id.prog);
        listRecycler=view.findViewById(R.id.list_recyclerview);
        nestedScrollView=view.findViewById(R.id.scroll);
        root=view.findViewById(R.id.root);
        statefulLayout.showLoading(" ");
        navController= Navigation.findNavController(view);

        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getContext(), R.color.orange), PorterDuff.Mode.SRC_IN);




        productCardViewAdapter=new ProductCardViewAdapter(getContext(),productInfoList,LIST,navController);


       /* if (productCardViewAdapter.getItemCount()==0)
            progressBar.setVisibility(View.VISIBLE);*/

        listRecycler.setAdapter(productCardViewAdapter);
        //listRecycler.setItemViewCacheSize(0);

        listRecycler.getRecycledViewPool().setMaxRecycledViews(0,1000);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int displayWidth = displayMetrics.widthPixels;


        listRecycler.setLayoutManager(new GridLayoutManager(getContext(),2));
        listRecycler.addItemDecoration(new GridSpacingItemDecoration(2,GridSpacingItemDecoration.dpToPx(5,getResources()),GridSpacingItemDecoration.ListLayout,displayWidth,(int)getResources().getDimension(R.dimen.list_card_width)));






        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY > oldScrollY) {
                    Log.i(TAG, "Scroll DOWN");
                }
                if (scrollY < oldScrollY) {
                    Log.i(TAG, "Scroll UP");
                }

                if (scrollY == 0) {
                    Log.i(TAG, "TOP SCROLL");
                }

                if ((v.getChildAt(v.getChildCount()-1).getBottom()-(v.getHeight()+v.getScrollY()))==0){
                    //Log.e("Scroll",(v.getChildAt(v.getChildCount()-1).getBottom())+"-"+v.getHeight()+"+"+v.getScrollY());
                    // Toast.makeText(getContext(),"bottom",Toast.LENGTH_SHORT).show();
                    onScroll();
                }

            }
        });


        filterBy.setOnClickListener(v -> {
            Bundle bundle=new Bundle();
            bundle.putBoolean(Filter.IS_OFFERS,true);
            navController.navigate(R.id.action_offers_to_filter,bundle);

        });


        orderBy.setOnClickListener(v -> {

            orderByDialog();

        });


    }




    private void onScroll(){


        if (productFilterData.isHasNext()&&!isLoading){

            isLoading=true;
            progressBar.setVisibility(View.VISIBLE);
            productViewModel.getNextProductFilter();

        }

    }



    private void orderByDialog(){


        final Dialog dialog=new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_filter);

        TextView title=dialog.findViewById(R.id.title);
        Button done=dialog.findViewById(R.id.done);
        RecyclerView recyclerView=dialog.findViewById(R.id.filter_recycler);
        title.setText("Order By");

        List<FilterInfo> items=new ArrayList<>();
        List<FilterInfo> selected=new ArrayList<>();

        items.add(new FilterInfo(0,"All",null,FilterInfo.CATEGORY));
        items.add(new FilterInfo(1,"High To Low Price",null,FilterInfo.CATEGORY));
        items.add(new FilterInfo(2,"Low To High Price",null,FilterInfo.CATEGORY));

        selected.add(items.get(0));

        FilterOrderCardViewAdapter filterOrderCardViewAdapter=new FilterOrderCardViewAdapter(getContext(),items,selected,ORDER);

        recyclerView.setAdapter(filterOrderCardViewAdapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));

        done.setOnClickListener(v -> {


            dialog.dismiss();

        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();



    }




}
