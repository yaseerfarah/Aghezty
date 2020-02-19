package com.example.aghezty.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.ablanco.zoomy.Zoomy;
import com.bumptech.glide.Glide;
import com.example.aghezty.POJO.SliderInfo;
import com.example.aghezty.R;
import com.example.aghezty.View.Home;

import java.util.ArrayList;
import java.util.List;




/**
 * Created by DELL on 1/19/2019.
 */

public class SliderAdapter extends PagerAdapter {
    private Context context;
    static public int HOME=1;
    static public int DETAILS=2;
    private List<String> slider_image;
    private int type;
    private Activity activity;



    public SliderAdapter(Context context, List<String> slider_image,Activity activity,int type) {
        this.context = context;
        this.slider_image = slider_image;
        this.type=type;
        this.activity=activity;
    }

    @Override
    public int getCount() {

        return slider_image.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==(RelativeLayout)object;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view;

            view = LayoutInflater.from(container.getContext()).inflate(R.layout.slider, container, false);
        ImageView imageView;
        if (type==HOME) {
                imageView = (ImageView) view.findViewById(R.id.image_slider);
            }else {
            imageView = (ImageView) view.findViewById(R.id.pro_image_slider);
            Zoomy.Builder builder = new Zoomy.Builder(activity).target(imageView);
            builder.register();

        }

            Glide.with(context).load(slider_image.get(position))
                    .into(imageView);

        container.addView(view);

        return view;
    }





    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout)object);
    }




}
