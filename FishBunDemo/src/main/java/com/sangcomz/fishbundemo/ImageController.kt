package com.sangcomz.fishbundemo

import android.net.Uri
import android.widget.ImageView
import com.squareup.picasso.Picasso

/**
 * Created by sangc on 2015-11-06.
 */
class ImageController(private val imgMain: ImageView) {

    fun setImgMain(path: Uri) {
        Picasso
            .with(imgMain.context)
            .load(path)
            .fit()
            .centerCrop()
            .into(imgMain)
    }
}