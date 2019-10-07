package com.sangcomz.fishbun.adapter.image.impl;

import android.net.Uri;
import android.widget.ImageView;

import com.sangcomz.fishbun.adapter.image.ImageAdapter;
import com.squareup.picasso.Picasso;

/**
 * Created by sangcomz on 23/07/2017.
 */

public class PicassoAdapter implements ImageAdapter {
    @Override
    public void loadImage(ImageView target, Uri loadUrl, int orientation) {
        Picasso
                .get()
                .load(loadUrl)
                .fit()
                .centerCrop()
                .rotate(orientation)
                .into(target);
    }

    @Override
    public void loadDetailImage(ImageView target, Uri loadUrl, int orientation) {
        Picasso.get()
                .load(loadUrl)
                .fit()
                .centerInside()
                .rotate(orientation)
                .into(target);
    }
}
