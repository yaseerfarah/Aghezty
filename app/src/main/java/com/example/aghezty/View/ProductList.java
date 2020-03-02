package com.example.aghezty.View;


import android.app.Dialog;
import android.app.MediaRouteButton;
import android.app.assist.AssistStructure;
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
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aghezty.Adapter.FilterOrderCardViewAdapter;
import com.example.aghezty.Adapter.ProductCardViewAdapter;
import com.example.aghezty.POJO.FilterInfo;
import com.example.aghezty.POJO.FilterOption;
import com.example.aghezty.POJO.HomeData;
import com.example.aghezty.POJO.HomeRecylerData;
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
import static com.example.aghezty.ViewModel.ProductViewModel.ALL;
import static com.example.aghezty.ViewModel.ProductViewModel.High_To_Low_Price;
import static com.example.aghezty.ViewModel.ProductViewModel.Low_To_High_Price;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductList extends Fragment {



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
    private ProductFilterData productFilterData;

    private ProductCardViewAdapter productCardViewAdapter;

    private List<ProductInfo> productInfoList=new ArrayList<>();
    private ProgressBar progressBar;
    private boolean isScrolling;
    private GridLayoutManager productLayout;


    public ProductList() {

        listObserver=new Observer<ProductFilterData>() {
            @Override
            public void onChanged(ProductFilterData productFilterData1) {
                if (productFilterData1!=null) {
                    productFilterData = productFilterData1;

                    productCardViewAdapter.updateProductList(productFilterData1.getProductList());
                    productInfoList.clear();
                    productInfoList.addAll(productFilterData1.getProductList());
                    isLoading = false;

                    root.setVisibility(View.VISIBLE);

                    if (productFilterData.getProductList().size()>0){
                        statefulLayout.showContent();
                    }else {
                        statefulLayout.showEmpty("No Match");
                    }
                }
                progressBar.post(() -> {
                    progressBar.setVisibility(View.INVISIBLE);
                });



            }
        };


    }


    @Override
    public void onStart() {
        super.onStart();
        productViewModel.getProductFilter();
        productViewModel.getProductFilterLiveData().observe(this,listObserver);

    }


    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onStop() {
        super.onStop();
        productViewModel.getProductFilterLiveData().removeObservers(this);
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

        return inflater.inflate(R.layout.fragment_product_list, container, false);

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
        root=view.findViewById(R.id.root);
        statefulLayout.showLoading(" ");
        navController= Navigation.findNavController(view);

        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getContext(), R.color.orange), PorterDuff.Mode.SRC_IN);
        productLayout=new GridLayoutManager(getContext(),2);



        productCardViewAdapter=new ProductCardViewAdapter(getContext(),productInfoList,LIST,navController);




        listRecycler.setAdapter(productCardViewAdapter);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int displayWidth = displayMetrics.widthPixels;


        listRecycler.setLayoutManager(productLayout);
        listRecycler.addItemDecoration(new GridSpacingItemDecoration(2,GridSpacingItemDecoration.dpToPx(5,getResources()),GridSpacingItemDecoration.ListLayout,displayWidth,(int)getResources().getDimension(R.dimen.list_card_width)));




        listRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){

                    isScrolling=true;

                }


            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int childCount=productLayout.getChildCount();
                int pastVisibleItem;
                int totalItem=productLayout.getItemCount();

                pastVisibleItem =productLayout.findFirstVisibleItemPosition();

                if (isScrolling&&((childCount+pastVisibleItem)>=totalItem)){

                    isScrolling=false;
                    onScroll();

                }

            }
        });


        filterBy.setOnClickListener(v -> {
            Bundle bundle=new Bundle();
            bundle.putBoolean(Filter.IS_OFFERS,false);
            navController.navigate(R.id.action_productList_to_filter,bundle);

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

        FilterOption filterOption=productViewModel.getFilterOption();
        TextView title=dialog.findViewById(R.id.title);
        Button done=dialog.findViewById(R.id.done);
        RecyclerView recyclerView=dialog.findViewById(R.id.filter_recycler);
        title.setText("Order By");

        List<FilterInfo> items=new ArrayList<>();
        List<FilterInfo> selected=new ArrayList<>();

        items.add(new FilterInfo(ALL,"All",null,FilterInfo.CATEGORY));
        items.add(new FilterInfo(High_To_Low_Price,"High To Low Price",null,FilterInfo.CATEGORY));
        items.add(new FilterInfo(Low_To_High_Price,"Low To High Price",null,FilterInfo.CATEGORY));


        selected.add(items.get(filterOption.getOrderBy()));


        FilterOrderCardViewAdapter filterOrderCardViewAdapter=new FilterOrderCardViewAdapter(getContext(),items,selected,ORDER);

        recyclerView.setAdapter(filterOrderCardViewAdapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));

        done.setOnClickListener(v -> {
            productViewModel.setFilter(filterOption.getCategoriesID(),filterOption.getBrandID(),filterOption.getPriceRange(),filterOrderCardViewAdapter.getItemListSelected().get(0).getId(),filterOption.isOffer());
            productViewModel.getProductFilter();
            statefulLayout.showLoading(" ");
            dialog.dismiss();

        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();



    }



}
