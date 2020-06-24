package com.example.aghezty.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.ablanco.zoomy.Zoomy;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.aghezty.POJO.SliderInfo;
import com.example.aghezty.R;
import com.example.aghezty.View.Home;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;




/**
 * Created by DELL on 1/19/2019.
 */

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderImage> {
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
    public SliderImage onCreateViewHolder(ViewGroup parent) {
        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider, null);
        return new SliderImage(view);
    }

    @Override
    public void onBindViewHolder(SliderImage holder, int position) {

        ImageView imageView;
        holder.progressBar.setVisibility(View.VISIBLE);

        if (type==HOME) {
            imageView = holder.homeSlider;
        }else {
            imageView = holder.detailsSlider;
            Zoomy.Builder builder = new Zoomy.Builder(activity).target(imageView);
            builder.register();
        }

        Glide.with(context).load(slider_image.get(position))
                .apply(RequestOptions.timeoutOf(60*1000))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e(getClass().getName(),e.getMessage());
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.INVISIBLE);
                        return false;
                    }
                })
                .into(imageView);

    }




    class SliderImage extends SliderViewAdapter.ViewHolder {

        ImageView homeSlider;
        ImageView detailsSlider;
        ProgressBar progressBar;


        public SliderImage(View itemView) {
            super(itemView);
            homeSlider=itemView.findViewById(R.id.image_slider);
            detailsSlider=itemView.findViewById(R.id.pro_image_slider);
            progressBar=itemView.findViewById(R.id.progress);
        }
    }







}
