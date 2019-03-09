package com.sangcomz.fishbun.adapter.image;

import android.net.Uri;
import android.widget.ImageView;

/**
 * Created by sangcomz on 23/07/2017.
 */

public interface ImageAdapter {
    void loadImage(ImageView target, Uri loadUrl);
    void loadDetailImage(ImageView target, Uri loadUrl);
}
