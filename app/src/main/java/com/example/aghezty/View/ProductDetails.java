package com.example.aghezty.View;


import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.viewpager.widget.ViewPager;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.aghezty.Adapter.SliderAdapter;
import com.example.aghezty.Interface.CompletableListener;
import com.example.aghezty.Interface.InnerProductListener;
import com.example.aghezty.POJO.ProductInfo;
import com.example.aghezty.POJO.Rate;
import com.example.aghezty.POJO.SliderInfo;
import com.example.aghezty.R;
import com.example.aghezty.ViewModel.ProductViewModel;
import com.example.aghezty.ViewModel.UserViewModel;
import com.example.aghezty.ViewModel.ViewModelFactory;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;
import es.dmoral.toasty.Toasty;

import static com.example.aghezty.Adapter.SliderAdapter.DETAILS;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductDetails extends Fragment {


    static public String PRODUCT_INFO="ProductInfo";

    @Inject
    ViewModelFactory viewModelFactory;
    private ProductViewModel productViewModel;
    private UserViewModel userViewModel;

    private NavController navController;

    @BindView(R.id.prog)
     ProgressBar progressBar;

    @BindView(R.id.v_view_pager)
    ViewPager pro_image;

    @BindView(R.id.vtitle)
     TextView pro_title;
    @BindView(R.id.pro_price)
     TextView pro_price;
    @BindView(R.id.de_available_q)
     TextView pro_Av_quantity;
    @BindView(R.id.pro_exprice)
     TextView pro_exPrice;
    @BindView(R.id.pro_discount)
     TextView pro_discount;
    @BindView(R.id.q_text)
     TextView pro_quantity;
    @BindView(R.id.user_q_number)
     TextView order_quantity;
    @BindView(R.id.pro_dec)
     TextView pro_Description;
    @BindView(R.id.pro_evaluation)
     TextView pro_evaluation;
    @BindView(R.id.rate)
     TextView pro_rate;
    @BindView(R.id.num_rate)
     TextView pro_rateNumber;

    @BindView(R.id.rate5_bar)
     SeekBar rate5;
    @BindView(R.id.rate4_bar)
     SeekBar rate4;
    @BindView(R.id.rate3_bar)
     SeekBar rate3;
    @BindView(R.id.rate2_bar)
     SeekBar rate2;
    @BindView(R.id.rate1_bar)
     SeekBar rate1;


    @BindView(R.id.rate_star)
     RatingBar rateStar;
    @BindView(R.id.ex_dis)
     RelativeLayout discount;
    @BindView(R.id.evaluation)
     RelativeLayout evaluation;

    @BindView(R.id.evaluation_layout)
     RelativeLayout evaluation_layout;
    @BindView(R.id.details_layout)
     RelativeLayout details_layout;

    @BindView(R.id.root)
     RelativeLayout root;

    private int quantity=1;

    @BindView(R.id.increase)
     ImageButton add_q;
    @BindView(R.id.decrease)
     ImageButton remove_q;
    @BindView(R.id.desc_add_tocart)
     Button addToCart;
    @BindView(R.id.details_toggle)
     ToggleButton detailsToggle;
    @BindView(R.id.evaluation_toggle)
     ToggleButton evaluationToggle;


    private List<String> sliderInfoList=new ArrayList<>();

    private ProductInfo productInfo;

    private SliderAdapter sliderAdapter;

    private InnerProductListener innerProductListener;

    public ProductDetails() {
        // Required empty public constructor



    }


    @Override
    public void onStart() {
        super.onStart();

        innerProductListener= new InnerProductListener() {
            @Override
            public void onSuccess(ProductInfo innerProduct) {
                root.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                productInfo=innerProduct;
                assignView(productInfo);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        if (sliderInfoList.isEmpty())
        productViewModel.getInnerProductByID(productInfo,innerProductListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        innerProductListener=null;
    }




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSupportInjection.inject(this);
        productViewModel= ViewModelProviders.of(this,viewModelFactory).get(ProductViewModel.class);
        userViewModel= ViewModelProviders.of(this,viewModelFactory).get(UserViewModel.class);

        productInfo=getArguments().getParcelable(PRODUCT_INFO);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_details, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this,view);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getContext(), R.color.orange), PorterDuff.Mode.SRC_IN);

        if (userViewModel.checkInCart(productInfo)){
            addToCart.setEnabled(false);
            addToCart.setText("Already in Cart");
        }


        addToCart.setOnClickListener(v -> {
            if (userViewModel.isLogin()) {
                addToCart.setEnabled(false);
                userViewModel.addCartInfo(productInfo, quantity, new CompletableListener() {
                    @Override
                    public void onSuccess() {
                        Toasty.success(getContext(), "Success Add to Cart", Toast.LENGTH_SHORT).show();
                        addToCart.setText("Already in Cart");
                    }

                    @Override
                    public void onFailure(String e) {
                        Toasty.error(getContext(), e, Toast.LENGTH_SHORT).show();
                        addToCart.setEnabled(true);
                    }
                });
            }else {
                getActivity().startActivity(new Intent(getContext(),LoginActivity.class));
            }

        });



    }




    private void assignView(ProductInfo productInfo){

        sliderInfoList.addAll(productInfo.getGallery());
        sliderAdapter=new SliderAdapter(getContext(),sliderInfoList,getActivity(),DETAILS);

        pro_image.setAdapter(sliderAdapter);

        pro_title.setText(productInfo.getTitle_en());

        if (productInfo.getDiscount()!=null){

            pro_exPrice.setText(String.valueOf(NumberFormat.getInstance(Locale.US).format(productInfo.getPrice())));
            pro_discount.setText(productInfo.getDiscount()+" OFF");
            pro_price.setText(String.valueOf(NumberFormat.getInstance(Locale.US).format(productInfo.getPrice_after_discount())));
        }else {
            pro_price.setText(String.valueOf(NumberFormat.getInstance(Locale.US).format(productInfo.getPrice())));
            discount.setVisibility(View.GONE);
        }

        pro_Av_quantity.setText(String.valueOf(productInfo.getStock()));

        if (productInfo.getStars() > 0) {
            pro_evaluation.setText(String.valueOf(productInfo.getStars()));
        } else {
            evaluation.setVisibility(View.GONE);
        }


        if (productInfo.getRates()!=null&&!productInfo.getRates().isEmpty()){
            toggleState(detailsToggle,evaluationToggle,productInfo);

        }else {
            evaluationToggle.setEnabled(false);
            detailsToggle.setEnabled(false);
            detailsToggle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,2f));
            evaluationToggle.setVisibility(View.GONE);
            evaluation_layout.setVisibility(View.GONE);
            details_layout.setVisibility(View.VISIBLE);
            pro_Description.setText(Html.fromHtml(productInfo.getDescription_en()));
        }


        pro_quantity.setText(String.valueOf(quantity));
        order_quantity.setText(String.valueOf(quantity));

        add_q.setOnClickListener(v -> {

            quantity++;
            pro_quantity.setText(String.valueOf(quantity));
            order_quantity.setText(String.valueOf(quantity));

        });

        remove_q.setOnClickListener(v -> {

            if (quantity>1){
                quantity--;
                pro_quantity.setText(String.valueOf(quantity));
                order_quantity.setText(String.valueOf(quantity));
            }

        });


    }



    private void toggleState(ToggleButton detailsToggle,ToggleButton evaluationToggle,ProductInfo productInfo){

        evaluationToggle.setEnabled(true);
        detailsToggle.setEnabled(false);
        pro_Description.setText(Html.fromHtml(productInfo.getDescription_en()));
        details_layout.setVisibility(View.VISIBLE);

        detailsToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked){
                evaluationToggle.setChecked(false);
                evaluationToggle.setTextColor(getResources().getColor(R.color.black));
                detailsToggle.setTextColor(getResources().getColor(R.color.orange));
                evaluationToggle.setEnabled(true);
                detailsToggle.setEnabled(false);

                evaluation_layout.setVisibility(View.GONE);
                details_layout.setVisibility(View.VISIBLE);

                pro_Description.setText(Html.fromHtml(productInfo.getDescription_en()));

            }

        });

        evaluationToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked){
                detailsToggle.setChecked(false);
                detailsToggle.setTextColor(getResources().getColor(R.color.black));
                evaluationToggle.setTextColor(getResources().getColor(R.color.orange));
                evaluationToggle.setEnabled(false);
                detailsToggle.setEnabled(true);

                details_layout.setVisibility(View.GONE);
                evaluation_layout.setVisibility(View.VISIBLE);

                pro_rate.setText(productInfo.getStars()+"/5");
                rateStar.setRating(productInfo.getStars());
                pro_rateNumber.setText(String.valueOf(productInfo.getRates().size()));
                assignSeekBar(productInfo.getRates());
            }

        });







    }



    private void assignSeekBar(List<Rate> rateList){

        int[] rates=new int[5];

        for (Rate rate:rateList){

            switch (rate.getRate()){

                case 5:
                    rates[4]++;
                    break;

                case 4:
                    rates[3]++;
                    break;

                case 3:
                    rates[2]++;
                    break;


                case 2:
                    rates[1]++;
                    break;

                case 1:
                    rates[0]++;
                    break;
            }
        }
        rate1.setMax(rateList.size());
        rate2.setMax(rateList.size());
        rate3.setMax(rateList.size());
        rate4.setMax(rateList.size());
        rate5.setMax(rateList.size());

        rate1.setProgress(rates[0]);
        rate2.setProgress(rates[1]);
        rate3.setProgress(rates[2]);
        rate4.setProgress(rates[3]);
        rate5.setProgress(rates[4]);

        rate1.setOnTouchListener((v, event) -> true);
        rate2.setOnTouchListener((v, event) -> true);
        rate3.setOnTouchListener((v, event) -> true);
        rate4.setOnTouchListener((v, event) -> true);
        rate5.setOnTouchListener((v, event) -> true);


    }





}
