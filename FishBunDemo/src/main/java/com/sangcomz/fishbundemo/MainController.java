package com.sangcomz.fishbundemo;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by sangc on 2015-11-06.
 */
public class MainController {
    Context context;
    ImageView imgMain;

    MainController(Context context, ImageView imgMain) {
        this.context = context;
        this.imgMain = imgMain;
    }

    public void setImgMain(String path) {
        Glide.with(context).load(path).fitCenter().into(imgMain);
    }
}
