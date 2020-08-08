package com.sangcomz.fishbun.adapter.image.impl

import android.graphics.Matrix
import android.net.Uri
import android.widget.ImageView
import coil.api.load
import coil.request.LoadRequestBuilder
import coil.size.Scale

import com.sangcomz.fishbun.adapter.image.ImageAdapter


/**
 * Created by sangcomz on 23/07/2017.
 */

class CoilAdapter : ImageAdapter {
    override fun loadImage(target: ImageView, loadUrl: Uri) {
        target.load(loadUrl) {
            scale(Scale.FIT)
            scale(Scale.FILL)
        }
    }


    override fun loadDetailImage(target: ImageView, loadUrl: Uri) {
        target.load(loadUrl) {
            scale(Scale.FIT)
        }
    }
}
