package com.sangcomz.fishbun.adapter.image

import android.net.Uri
import android.widget.ImageView

/**
 * Created by sangcomz on 23/07/2017.
 */

interface ImageAdapter {
    fun loadImage(target: ImageView, loadUrl: Uri)
    fun loadDetailImage(target: ImageView, loadUrl: Uri)
}
