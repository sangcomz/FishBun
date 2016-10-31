package com.sangcomz.fishbundemo;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by sangc on 2015-11-06.
 */
class MainController {
    Context context;
    ImageView imgMain;

    MainController(Context context, ImageView imgMain) {
        this.context = context;
        this.imgMain = imgMain;
    }

    void setImgMain(String path) {
        Picasso
                .with(context)
                .load(new File(path))
                .fit()
                .centerCrop()
                .into(imgMain);
    }
}
