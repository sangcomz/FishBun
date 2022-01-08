package com.sangcomz.fishbundemo

import android.net.Uri
import android.widget.ImageView
import coil.api.load
import coil.size.Scale

/**
 * Created by sangc on 2015-11-06.
 */
class ImageController(private val imgMain: ImageView) {

    fun setImgMain(path: Uri) {
        imgMain.load(path) {
            scale(Scale.FILL)
            scale(Scale.FIT)
        }
    }
}