package com.sangcomz.fishbundemo;

import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by sangc on 2015-11-06.
 */
class ImageController {
    ImageView imgMain;

    ImageController(ImageView imgMain) {
        this.imgMain = imgMain;
    }

    void setImgMain(Uri path) {
        Glide.with(imgMain)
                .load(path)
                .fitCenter()
                .centerCrop()
                .into(imgMain);
    }
}
