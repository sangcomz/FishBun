package com.sangcomz.fishbundemo;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by sangc on 2015-11-06.
 */
class ImageController {
    Context context;
    ImageView imgMain;

    ImageController(Context context, ImageView imgMain) {
        this.context = context;
        this.imgMain = imgMain;
    }

    void setImgMain(Uri path) {
        Picasso
                .with(context)
                .load(path)
                .fit()
                .centerCrop()
                .into(imgMain);
    }
}
