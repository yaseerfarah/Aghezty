package com.example.aghezty.View;


import android.app.Dialog;
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

import com.example.aghezty.Adapter.FilterOrderCardViewAdapter;
import com.example.aghezty.POJO.BrandInfo;
import com.example.aghezty.POJO.CategoryInfo;
import com.example.aghezty.POJO.FilterInfo;
import com.example.aghezty.R;
import com.example.aghezty.ViewModel.ProductViewModel;
import com.example.aghezty.ViewModel.ViewModelFactory;

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
public class Filter extends Fragment {

    @Inject
    ViewModelFactory viewModelFactory;
    private ProductViewModel productViewModel;
    private Observer parentCategoriesObserver;
    private Observer brandCategoriesObserver;
    private NavController navController;



    List<FilterInfo> heavyMachines=new ArrayList<>();
    List<FilterInfo> lightDevices=new ArrayList<>();
    List<FilterInfo> brandInfoList=new ArrayList<>();
    List<String> priceRange;


    List<FilterInfo> heavyMachines_select=new ArrayList<>();
    List<FilterInfo> lightDevices_select=new ArrayList<>();
    List<FilterInfo> brandInfoList_select=new ArrayList<>();
    List<FilterInfo> priceRange_select=new ArrayList<>();


    boolean isparentCatDone=false;
    boolean isBrandCatDone=false;

    @BindView(R.id.heavy_sub_cat)
    RelativeLayout heavy_layout;
    @BindView(R.id.light_sub_cat)
    RelativeLayout light_layout;
    @BindView(R.id.brand_sub_cat)
    RelativeLayout brand_layout;
    @BindView(R.id.price_sub_cat)
    RelativeLayout price_layout;

    @BindView(R.id.root)
    RelativeLayout root;


    @BindView(R.id.heavy_choice)
    TextView heavy_choice;
    @BindView(R.id.light_choice)
    TextView light_choice;
    @BindView(R.id.brand_choice)
    TextView brand_choice;
    @BindView(R.id.price_choice)
    TextView price_choice;

    @BindView(R.id.apply)
    Button apply;


    @BindView(R.id.prog)
    ProgressBar progressBar;






    public Filter() {
        // Required empty public constructor

        parentCategoriesObserver=new Observer<List<CategoryInfo>>() {
            @Override
            public void onChanged(List<CategoryInfo> categoryInfos) {
                if (categoryInfos!=null){

                    for (CategoryInfo categoryInfo:categoryInfos){

                        if (categoryInfo.getId()==3&&categoryInfo.getSub_cats()!=null){

                            for (CategoryInfo Info:categoryInfo.getSub_cats()){
                                heavyMachines.add(new FilterInfo( Info.getId(),Info.getTitle_en()));
                            }


                        }else if (categoryInfo.getId()==13&&categoryInfo.getSub_cats()!=null){
                            for (CategoryInfo Info:categoryInfo.getSub_cats()){
                                lightDevices.add(new FilterInfo( Info.getId(),Info.getTitle_en()));
                            }

                        }

                    }

                    isparentCatDone=true;
                    progress();

                }
            }

        };

        brandCategoriesObserver=new Observer<List<BrandInfo>>() {
            @Override
            public void onChanged(List<BrandInfo> brandInfos) {

                if (brandInfos!=null){
                    for (BrandInfo Info:brandInfos){
                        brandInfoList.add(new FilterInfo(Info.getId(),Info.getTitle_en()) );
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
        productViewModel.getProductFilter();
        productViewModel.getParentCategoriesLiveData().observe(this,parentCategoriesObserver);
        productViewModel.getBrandCategoriesLiveData().observe(this,brandCategoriesObserver);

    }

    @Override
    public void onStop() {
        super.onStop();
        productViewModel.getParentCategoriesLiveData().removeObservers(this);
        productViewModel.getBrandCategoriesLiveData().removeObservers(this);
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSupportInjection.inject(this);
        productViewModel= ViewModelProviders.of(this,viewModelFactory).get(ProductViewModel.class);
        priceRange= Arrays.asList(getResources().getStringArray(R.array.price_range_filter));

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

        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getContext(), R.color.orange), PorterDuff.Mode.SRC_IN);


        navController= Navigation.findNavController(view);

        priceRange_select.add(new FilterInfo(0,priceRange.get(0)));

        heavy_layout.setOnClickListener(v -> {



            create_dialog("Heavy Machines",heavyMachines,heavyMachines_select,heavy_choice,FILTER);

        });

        light_layout.setOnClickListener(v -> {



            create_dialog("Light Devices",lightDevices,lightDevices_select,light_choice,FILTER);

        });


        brand_layout.setOnClickListener(v -> {



            create_dialog("Brand",brandInfoList,brandInfoList_select,brand_choice,FILTER);

        });


        price_layout.setOnClickListener(v -> {

            List<FilterInfo>items=new ArrayList<>();

            items.add(new FilterInfo(0,priceRange.get(0)) );
            items.add(new FilterInfo(1000,priceRange.get(1)) );
            items.add(new FilterInfo(3000,priceRange.get(2)) );
            items.add(new FilterInfo(6000,priceRange.get(3)) );
            items.add(new FilterInfo(10000,priceRange.get(4)) );
            items.add(new FilterInfo(20000,priceRange.get(5)) );



            create_dialog("Price Range",items,priceRange_select,price_choice,ORDER);
        });




        apply.setOnClickListener(v -> {

            List<FilterInfo> catIDList=new ArrayList<>();
            catIDList.addAll(heavyMachines_select);
            catIDList.addAll(lightDevices_select);

            productViewModel.setFilter(catIDList,brandInfoList_select,priceRange_select.get(0).getId(),false);

            navController.navigate(R.id.action_filter_to_productList);

        });


    }


    private void progress(){

        if (isBrandCatDone&&isparentCatDone){

            progressBar.setVisibility(View.GONE);
            root.setVisibility(View.VISIBLE);

        }
    }



    private void create_dialog(String name, List<FilterInfo> items,List<FilterInfo> selected,TextView choice, int type){


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

        done.setOnClickListener(v -> {
            selected.clear();
            selected.addAll(filterOrderCardViewAdapter.getItemListSelected());
            if (!selected.isEmpty()) {
                choice.setText(selected.get(0).getName());
            }else {
                choice.setText("Nothing");
            }
            dialog.dismiss();

        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();



    }



}
