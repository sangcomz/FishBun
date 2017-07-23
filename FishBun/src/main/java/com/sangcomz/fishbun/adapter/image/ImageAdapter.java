package com.sangcomz.fishbun.adapter.image;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

/**
 * Created by sangcomz on 23/07/2017.
 */

public interface ImageAdapter {
    void loadImage(Context context, ImageView target, Uri loadUrl);
}
