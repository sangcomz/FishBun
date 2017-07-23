package com.sangcomz.fishbun.adapter.image.impl;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.sangcomz.fishbun.adapter.image.ImageAdapter;

/**
 * Created by sangcomz on 23/07/2017.
 */

public class FrescoAdapter implements ImageAdapter {

    @Override
    public void loadImage(Context context, ImageView target, Uri loadUrl) {
        Fresco.initialize(context);

    }
}
