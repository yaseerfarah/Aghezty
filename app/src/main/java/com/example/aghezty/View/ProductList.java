package com.example.aghezty.View;


import android.app.Dialog;
import android.app.MediaRouteButton;
import android.app.assist.AssistStructure;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
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
import com.example.aghezty.BroadcastReceiver.NetworkReceiver;
import com.example.aghezty.Interface.InternetStatus;
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
import com.gturedi.views.CustomStateOptions;
import com.gturedi.views.StatefulLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

import static com.example.aghezty.Adapter.FilterOrderCardViewAdapter.ORDER;
import static com.example.aghezty.Adapter.ProductCardViewAdapter.LIST;
import static com.example.aghezty.ViewModel.ProductViewModel.ALL;
import static com.example.aghezty.ViewModel.ProductViewModel.High_To_Low_Price;
import static com.example.aghezty.ViewModel.ProductViewModel.Low_To_High_Price;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductList extends Fragment implements InternetStatus {



    @Inject
    ViewModelFactory viewModelFactory;
    private ProductViewModel productViewModel;
    private Observer listObserver;
    private NavController navController;

    private final String TAG=getClass().getName();

    private boolean isLoading=false;

    @BindView(R.id.list_recyclerview)
    RecyclerView listRecycler;
    @BindView(R.id.order_by)
    RelativeLayout orderBy;
    @BindView(R.id.filter_by)
    RelativeLayout filterBy;
    @BindView(R.id.stateful)
    StatefulLayout statefulLayout;
    @BindView(R.id.root)
    RelativeLayout root;
    @BindView(R.id.prog)
    ProgressBar progressBar;

    private ProductFilterData productFilterData;

    private ProductCardViewAdapter productCardViewAdapter;

    private List<ProductInfo> productInfoList=new ArrayList<>();

    private boolean isScrolling;
    private GridLayoutManager productLayout;
    private boolean isOnline=false;
    private NetworkReceiver networkReceiver;
    private CustomStateOptions networkCustom=new CustomStateOptions().image(R.drawable.ic_signal_wifi_off_black_24dp);


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
                        statefulLayout.showEmpty(getResources().getString(R.string.no_match));
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

        IntentFilter netFilter=new IntentFilter();
        netFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        getActivity().registerReceiver(networkReceiver,netFilter);



        productViewModel.getProductFilterLiveData().observe(this,listObserver);

    }




    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(networkReceiver);
        productViewModel.getProductFilterLiveData().removeObservers(this);
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

        return inflater.inflate(R.layout.fragment_product_list, container, false);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

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

        items.add(new FilterInfo(ALL,getResources().getString(R.string.all),null,FilterInfo.CATEGORY));
        items.add(new FilterInfo(High_To_Low_Price,getResources().getString(R.string.h_to_l),null,FilterInfo.CATEGORY));
        items.add(new FilterInfo(Low_To_High_Price,getResources().getString(R.string.l_to_h),null,FilterInfo.CATEGORY));


        selected.add(items.get(filterOption.getOrderBy()));


        FilterOrderCardViewAdapter filterOrderCardViewAdapter=new FilterOrderCardViewAdapter(getContext(),items,selected,ORDER);

        recyclerView.setAdapter(filterOrderCardViewAdapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));

        done.setOnClickListener(v -> {
            if (isOnline) {
                productViewModel.setFilter(filterOption.getCategoriesID(), filterOption.getBrandID(), filterOption.getPriceRange(), filterOrderCardViewAdapter.getItemListSelected().get(0).getId(), filterOption.isOffer());
                productViewModel.getProductFilter();
                statefulLayout.showLoading(" ");
            }
            dialog.dismiss();

        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();



    }

    @Override
    public void Connect() {
        isOnline=true;
        productViewModel.getProductFilter();
        statefulLayout.showLoading(" ");

    }

    @Override
    public void notConnect() {
        isOnline=false;
        statefulLayout.showCustom(networkCustom.message(getResources().getString(R.string.check_connection)));
        progressBar.setVisibility(View.GONE);
        isLoading = false;


    }
}
