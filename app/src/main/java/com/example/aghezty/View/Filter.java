package com.example.aghezty.View;


import android.app.Dialog;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aghezty.Adapter.FilterOrderCardViewAdapter;
import com.example.aghezty.Adapter.FilterParentCategoryCardViewAdapter;
import com.example.aghezty.BroadcastReceiver.NetworkReceiver;
import com.example.aghezty.Interface.InternetStatus;
import com.example.aghezty.Interface.OnParentCategoryClick;
import com.example.aghezty.POJO.BrandInfo;
import com.example.aghezty.POJO.CategoryInfo;
import com.example.aghezty.POJO.FilterInfo;
import com.example.aghezty.R;
import com.example.aghezty.ViewModel.ProductViewModel;
import com.example.aghezty.ViewModel.ViewModelFactory;
import com.gturedi.views.CustomStateOptions;
import com.gturedi.views.StatefulLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

import static com.example.aghezty.Adapter.FilterOrderCardViewAdapter.FILTER;
import static com.example.aghezty.Adapter.FilterOrderCardViewAdapter.ORDER;

/**
 * A simple {@link Fragment} subclass.
 */
public class Filter extends Fragment implements InternetStatus , OnParentCategoryClick {


    static public final String IS_OFFERS="isOffers";




    @Inject
    ViewModelFactory viewModelFactory;
    private ProductViewModel productViewModel;
    private Observer parentCategoriesObserver;
    private Observer brandCategoriesObserver;
    private NavController navController;


    private FilterParentCategoryCardViewAdapter filterParentCategoryCardViewAdapter;


    List<FilterInfo> filterParentList=new ArrayList<>();

    List<CategoryInfo> parentCategoryInfoList=new ArrayList<>();
    List<FilterInfo> brandInfoList=new ArrayList<>();
    List<FilterInfo> priceRange=new ArrayList<>();


    List<FilterInfo> categories_select=new ArrayList<>();
    List<FilterInfo> brandInfoList_select=new ArrayList<>();
    List<FilterInfo> priceRange_select=new ArrayList<>();


    boolean isparentCatDone=false;
    boolean isBrandCatDone=false;


    @BindView(R.id.root)
    RelativeLayout root;


    @BindView(R.id.filter_recycler_list)
    RecyclerView filterRecycler;

    @BindView(R.id.clear)
    TextView clear_choices;

    @BindView(R.id.apply)
    Button apply;


    @BindView(R.id.stateful)
    StatefulLayout statefulLayout;

    boolean isOffers;


    private NetworkReceiver networkReceiver;
    private CustomStateOptions networkCustom=new CustomStateOptions().image(R.drawable.ic_signal_wifi_off_black_24dp);




    public Filter() {
        // Required empty public constructor

        parentCategoriesObserver=new Observer<List<CategoryInfo>>() {
            @Override
            public void onChanged(List<CategoryInfo> categoryInfos) {
                if (categoryInfos!=null&&!isparentCatDone){


                    parentCategoryInfoList.addAll(categoryInfos);


                    isparentCatDone=true;
                    progress();



                }
            }

        };

        brandCategoriesObserver=new Observer<List<BrandInfo>>() {
            @Override
            public void onChanged(List<BrandInfo> brandInfos) {

                if (brandInfos!=null&&!isBrandCatDone){
                    for (BrandInfo Info:brandInfos){
                        brandInfoList.add(new FilterInfo(Info.getId(),Info.getTitle_en(),Info.getTitle_ar(),null,FilterInfo.BRAND) );
                    }
                    isBrandCatDone=true;
                    progress();


                }

            }

        };


    }


    @Override
    public void onStart() {
        super.onStart();
        clear();

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
        isOffers=getArguments().getBoolean(IS_OFFERS);
        networkReceiver=new NetworkReceiver(this);


        List<String> priceList= Arrays.asList(getResources().getStringArray(R.array.price_range_filter));


        priceRange.add(new FilterInfo(0,priceList.get(0),priceList.get(0),null,FilterInfo.PRICE) );
        priceRange.add(new FilterInfo(1000,priceList.get(1),priceList.get(1),null,FilterInfo.PRICE) );
        priceRange.add(new FilterInfo(3000,priceList.get(2),priceList.get(2),null,FilterInfo.PRICE) );
        priceRange.add(new FilterInfo(6000,priceList.get(3),priceList.get(3),null,FilterInfo.PRICE) );
        priceRange.add(new FilterInfo(10000,priceList.get(4),priceList.get(4),null,FilterInfo.PRICE) );
        priceRange.add(new FilterInfo(20000,priceList.get(5),priceList.get(5),null,FilterInfo.PRICE) );




    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

       // progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getContext(), R.color.orange), PorterDuff.Mode.SRC_IN);

        navController= Navigation.findNavController(view);

        priceRange_select.add(priceRange.get(0));

        filterParentCategoryCardViewAdapter=new FilterParentCategoryCardViewAdapter(getContext(),filterParentList,this);

        filterRecycler.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));

        filterRecycler.setAdapter(filterParentCategoryCardViewAdapter);


        //create_dialog(getResources().getString(R.string.price),items,priceRange_select,price_choice,ORDER);




        apply.setOnClickListener(v -> {

            productViewModel.setFilter(categories_select,brandInfoList_select,priceRange_select.get(0).getId(),ProductViewModel.ALL,false);

            if (isOffers) {
               // navController.navigate(R.id.action_global_offers);
                navController.navigateUp();
            }else {
                //navController.navigate(R.id.action_global_productList);
                navController.navigateUp();
            }

        });


        clear_choices.setOnClickListener(v -> {
            clear();
        });




    }


    private void progress(){

        if (isBrandCatDone&&isparentCatDone){

            filterParentList.clear();

            for (CategoryInfo categoryInfo:parentCategoryInfoList){
                if (!categoryInfo.getSub_cats().isEmpty())
                filterParentList.add(new FilterInfo(categoryInfo.getId(),categoryInfo.getTitle_en(),categoryInfo.getTitle_ar(),null,FilterInfo.CATEGORY));
            }

            filterParentList.add(new FilterInfo(0,getResources().getString(R.string.brand),getResources().getString(R.string.brand),null,FilterInfo.BRAND));
            filterParentList.add(new FilterInfo(0,getResources().getString(R.string.price),getResources().getString(R.string.price),null,FilterInfo.PRICE));

            filterParentCategoryCardViewAdapter.notifyDataSetChanged();

            statefulLayout.showContent();

        }
    }



    private void createFilterItemsdialog(String name, List<FilterInfo> items,List<FilterInfo> selected, int type){


        final Dialog dialog=new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_filter);

        TextView title=dialog.findViewById(R.id.title);
        Button done=dialog.findViewById(R.id.done);
        RecyclerView recyclerView=dialog.findViewById(R.id.filter_recycler);
        title.setText(name);

        FilterOrderCardViewAdapter filterOrderCardViewAdapter=new FilterOrderCardViewAdapter(getContext(),items,selected,type);

        recyclerView.setAdapter(filterOrderCardViewAdapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setHasFixedSize(true);

        done.setOnClickListener(v -> {
            selected.clear();
            selected.addAll(filterOrderCardViewAdapter.getItemListSelected());
           // Toast.makeText(getContext(),String.valueOf(categories_select.size()),Toast.LENGTH_SHORT).show();
            if (!selected.isEmpty()) {
            }else {
            }
            dialog.dismiss();

        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();



    }



    private void clear(){

        if( productViewModel.getFilterOption()!=null){
            categories_select.clear();
            brandInfoList_select.clear();
            priceRange_select.clear();

            categories_select.addAll(productViewModel.getFilterOption().getCategoriesID());
            brandInfoList_select.addAll(productViewModel.getFilterOption().getBrandID());
            priceRange_select.add(new FilterInfo(productViewModel.getFilterOption().getPriceRange(),null,null,null,FilterInfo.CATEGORY));



        }

    }




    private List<FilterInfo> getSubCategory(int id){

        List<FilterInfo> subCategoryList=new ArrayList<>();

        for (CategoryInfo categoryInfo:parentCategoryInfoList){

            if (categoryInfo.getId()==id){

                for (CategoryInfo categoryInfo1:categoryInfo.getSub_cats()){

                    subCategoryList.add(new FilterInfo(categoryInfo1.getId(),categoryInfo1.getTitle_en(),categoryInfo1.getTitle_ar(),categoryInfo1.getImage(),FilterInfo.CATEGORY));
                }
            }
        }

        return subCategoryList;

    }







    @Override
    public void onClick(FilterInfo filterInfo) {

        switch (filterInfo.getType()){
            case FilterInfo.CATEGORY :
                createFilterItemsdialog(filterInfo.getName(),getSubCategory(filterInfo.getId()),categories_select,FILTER);
                break;

            case FilterInfo.BRAND :
                createFilterItemsdialog(getResources().getString(R.string.brand),brandInfoList,brandInfoList_select,FILTER);
                break;

            case FilterInfo.PRICE :
                createFilterItemsdialog(getResources().getString(R.string.price),priceRange,priceRange_select,ORDER);
                break;

        }


    }



    @Override
    public void Connect() {
        statefulLayout.showLoading(" ");
        productViewModel.getParentCategories();
        productViewModel.getBrandCategories();
        progress();


    }

    @Override
    public void notConnect() {
        statefulLayout.showCustom(networkCustom.message(getResources().getString(R.string.check_connection)));

    }


}
