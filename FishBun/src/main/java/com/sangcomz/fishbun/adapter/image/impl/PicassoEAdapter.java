
package com.sangcomz.fishbun.adapter.image.impl;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.sangcomz.fishbun.adapter.image.ImageAdapter;
import com.squareup.picasso.Picasso;


public class PicassoEAdapter implements ImageAdapter {
    @Override
    public void loadImage(Context context, ImageView target, Uri loadUrl) {
        Picasso
                .get()
                .load(loadUrl)
                .fit()
                .centerCrop()
                .into(target);
    }

    @Override
    public void loadDetailImage(Context context, ImageView target, Uri loadUrl) {
        Picasso
                .get()
                .load(loadUrl)
                .fit()
                .centerInside()
                .into(target);
    }
}
