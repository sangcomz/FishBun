package com.sangcomz.fishbundemo;

import android.net.Uri;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by sangc on 2015-11-06.
 */
class ImageController {
    ImageView imgMain;

    ImageController(ImageView imgMain) {
        this.imgMain = imgMain;
    }

    void setImgMain(Uri path) {
        Picasso
                .get()
                .load(path)
                .fit()
                .centerCrop()
                .into(imgMain);
    }
}