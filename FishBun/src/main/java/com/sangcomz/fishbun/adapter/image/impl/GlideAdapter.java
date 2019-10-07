package com.sangcomz.fishbun.adapter.image.impl;

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
    public void loadImage(ImageView target, Uri loadUrl, int orientation) {
        RequestOptions options = new RequestOptions().centerCrop();
        Glide
                .with(target.getContext())
                .load(loadUrl)
                .apply(options)
                .into(target);
    }

    @Override
    public void loadDetailImage(ImageView target, Uri loadUrl, int orientation) {
        RequestOptions options = new RequestOptions().centerInside();
        Glide
                .with(target.getContext())
                .load(loadUrl)
                .apply(options)
                .into(target);
    }
}
