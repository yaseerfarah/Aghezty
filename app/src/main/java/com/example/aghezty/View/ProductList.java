package com.example.aghezty.View;


import android.graphics.PorterDuff;
import android.os.Bundle;

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

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.aghezty.Adapter.ProductCardViewAdapter;
import com.example.aghezty.POJO.HomeData;
import com.example.aghezty.POJO.HomeRecylerData;
import com.example.aghezty.POJO.ProductFilterData;
import com.example.aghezty.POJO.ProductInfo;
import com.example.aghezty.R;
import com.example.aghezty.Util.GridSpacingItemDecoration;
import com.example.aghezty.ViewModel.ProductViewModel;
import com.example.aghezty.ViewModel.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

import static com.example.aghezty.Adapter.ProductCardViewAdapter.LIST;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductList extends Fragment {


    @Inject
    ViewModelFactory viewModelFactory;
    private ProductViewModel productViewModel;
    private Observer listObserver;
    private NavController navController;


    private RecyclerView listRecycler;
    private RelativeLayout orderBy,filterBy;
    private ProgressBar progressBar;

    private ProductCardViewAdapter productCardViewAdapter;

    private List<ProductInfo> productInfoList=new ArrayList<>();


    public ProductList() {

        listObserver=new Observer<ProductFilterData>() {
            @Override
            public void onChanged(ProductFilterData productFilterData) {
                productInfoList.clear();
                productInfoList.addAll(productFilterData.getProductList());


                productCardViewAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

            }
        };


    }


    @Override
    public void onStart() {
        super.onStart();
        productViewModel.getOfferProducts();
        productViewModel.getOfferProductsLiveData().observe(this,listObserver);

    }

    @Override
    public void onStop() {
        super.onStop();
        productViewModel.getOfferProductsLiveData().removeObservers(this);
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

        orderBy=view.findViewById(R.id.order_by);
        filterBy=view.findViewById(R.id.filter_by);
        progressBar=view.findViewById(R.id.prog);
        listRecycler=view.findViewById(R.id.list_recyclerview);

        navController= Navigation.findNavController(view);

        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getContext(), R.color.orange), PorterDuff.Mode.SRC_IN);


        productCardViewAdapter=new ProductCardViewAdapter(getContext(),productInfoList,LIST,navController);

        listRecycler.setAdapter(productCardViewAdapter);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int displayWidth = displayMetrics.widthPixels;


        listRecycler.setLayoutManager(new GridLayoutManager(getContext(),2));
        listRecycler.addItemDecoration(new GridSpacingItemDecoration(2,GridSpacingItemDecoration.dpToPx(5,getResources()),GridSpacingItemDecoration.ListLayout,displayWidth,(int)getResources().getDimension(R.dimen.list_card_width)));







    }
}
