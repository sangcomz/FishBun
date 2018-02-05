package com.sangcomz.fishbun.adapter.image.impl;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sangcomz.fishbun.adapter.image.ImageAdapter;

/**
 * Created by sangcomz on 23/07/2017.
 */

public class GlideAdapter implements ImageAdapter {
    @Override
    public void loadImage(Context context,
                          ImageView target,
                          Uri loadUrl) {
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        Glide
                .with(context)
                .load(loadUrl)
                .apply(options)
                .into(target);
    }

    @Override
    public void loadDetailImage(Context context, ImageView target, Uri loadUrl) {
        RequestOptions options = new RequestOptions();
        options.centerInside();
        Glide
                .with(context)
                .load(loadUrl)
                .apply(options)
                .into(target);
    }
}
