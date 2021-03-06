package com.example.aghezty.View;


import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.aghezty.Adapter.SliderAdapter;
import com.example.aghezty.BroadcastReceiver.NetworkReceiver;
import com.example.aghezty.Interface.CompletableListener;
import com.example.aghezty.Interface.InnerProductListener;
import com.example.aghezty.Interface.InternetStatus;
import com.example.aghezty.POJO.ProductInfo;
import com.example.aghezty.POJO.Rate;
import com.example.aghezty.POJO.SliderInfo;
import com.example.aghezty.R;
import com.example.aghezty.ViewModel.ProductViewModel;
import com.example.aghezty.ViewModel.UserViewModel;
import com.example.aghezty.ViewModel.ViewModelFactory;
import com.gturedi.views.CustomStateOptions;
import com.gturedi.views.StatefulLayout;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;
import es.dmoral.toasty.Toasty;

import static com.example.aghezty.Adapter.SliderAdapter.DETAILS;
import static com.example.aghezty.Constants.localeLanguage;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductDetails extends Fragment implements InternetStatus {


    static public String PRODUCT_INFO="ProductInfo";

    @Inject
    ViewModelFactory viewModelFactory;
    private ProductViewModel productViewModel;
    private UserViewModel userViewModel;

    private NavController navController;

    @BindView(R.id.stateful)
    StatefulLayout statefulLayout;

    @BindView(R.id.v_view_pager)
    SliderView sliderView;

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
     LinearLayout evaluation_layout;
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


    @BindView(R.id.language)
    RadioGroup language;

    @BindView(R.id.english)
    RadioButton english;

    @BindView(R.id.arabic)
    RadioButton arabic;


    private List<String> sliderInfoList=new ArrayList<>();

    private ProductInfo productInfo;

    private SliderAdapter sliderAdapter;

    private InnerProductListener innerProductListener;
    private int page=0;
    private Timer timer;
    private boolean isStart=false;
    private boolean isInnerProduct=false;
    private boolean isDescriptionEnglish;

    private NetworkReceiver networkReceiver;
    private CustomStateOptions networkCustom=new CustomStateOptions().image(R.drawable.ic_signal_wifi_off_black_24dp);


    public ProductDetails() {
        // Required empty public constructor
        innerProductListener= new InnerProductListener() {
            @Override
            public void onSuccess(ProductInfo innerProduct) {
                assignView(productInfo);
                root.setVisibility(View.VISIBLE);
                statefulLayout.showContent();
                productInfo=innerProduct;
                isInnerProduct=true;

               /* if (innerProduct.getGallery().size()>1){
                    //viewpager_timer(5);
                }*/
            }

            @Override
            public void onFailure(Exception e) {

            }
        };


    }


    @Override
    public void onStart() {
        super.onStart();


        IntentFilter netFilter=new IntentFilter();
        netFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        getActivity().registerReceiver(networkReceiver,netFilter);




    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(networkReceiver);
        //cancel_timer();

    }




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSupportInjection.inject(this);
        productViewModel= ViewModelProviders.of(this,viewModelFactory).get(ProductViewModel.class);
        userViewModel= ViewModelProviders.of(this,viewModelFactory).get(UserViewModel.class);

        productInfo=getArguments().getParcelable(PRODUCT_INFO);
        networkReceiver=new NetworkReceiver(this);

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

        if (userViewModel.checkInCart(productInfo)){
            addToCart.setEnabled(false);
            addToCart.setText(getResources().getString(R.string.already_in_cart));
        }

        if (localeLanguage.getDisplayLanguage().matches(Locale.ENGLISH.getDisplayLanguage())){
            english.setChecked(true);
            english.setTextColor(getResources().getColor(R.color.white));
            arabic.setTextColor(getResources().getColor(R.color.black));
            isDescriptionEnglish=true;
        }else {
            arabic.setChecked(true);
            arabic.setTextColor(getResources().getColor(R.color.white));
            english.setTextColor(getResources().getColor(R.color.black));
            isDescriptionEnglish=false;
        }





        english.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked){
                english.setTextColor(getResources().getColor(R.color.white));
                pro_Description.setText(Html.fromHtml(productInfo.getDescription_en()));
                isDescriptionEnglish=true;

            }else {
                english.setTextColor(getResources().getColor(R.color.black));
            }

        });


        arabic.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked){
                arabic.setTextColor(getResources().getColor(R.color.white));
                pro_Description.setText(Html.fromHtml(productInfo.getDescription_ar()));
                isDescriptionEnglish=false;
            }else {
                arabic.setTextColor(getResources().getColor(R.color.black));
            }

        });





        addToCart.setOnClickListener(v -> {
            if (userViewModel.isLogin()) {
                addToCart.setEnabled(false);
                userViewModel.addCartInfo(productInfo, quantity, new CompletableListener() {
                    @Override
                    public void onSuccess() {
                        Toasty.success(getContext(), getResources().getString(R.string.success_add_cart), Toast.LENGTH_SHORT).show();
                        addToCart.setText(getResources().getString(R.string.already_in_cart));
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

        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorRadius(3);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
         sliderView.setInfiniteAdapterEnabled(false);
        sliderView.setIndicatorSelectedColor(getResources().getColor(R.color.orange));
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();

        pro_title.setText(productInfo.getTitile());

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
            pro_Description.setText(Html.fromHtml(productInfo.getDescription()));
        }


        pro_quantity.setText(String.valueOf(quantity));
        order_quantity.setText(String.valueOf(quantity));

        add_q.setOnClickListener(v -> {
            if (quantity<productInfo.getStock()) {
                quantity++;
                pro_quantity.setText(String.valueOf(quantity));
                order_quantity.setText(String.valueOf(quantity));
            }

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
        pro_Description.setText(Html.fromHtml(productInfo.getDescription()));
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

                pro_Description.setText(Html.fromHtml(productInfo.getDescription()));

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





    @Override
    public void Connect() {
        if (!isInnerProduct) {
            statefulLayout.showLoading(" ");
            productViewModel.getInnerProductByID(productInfo, innerProductListener);

        }else {

            statefulLayout.showContent();
        }
    }

    @Override
    public void notConnect() {
        statefulLayout.showCustom(networkCustom.message(getResources().getString(R.string.check_connection)));
    }


}
