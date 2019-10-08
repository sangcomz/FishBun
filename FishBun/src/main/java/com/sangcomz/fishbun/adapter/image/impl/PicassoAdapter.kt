package com.sangcomz.fishbun.adapter.image.impl

import android.net.Uri
import android.widget.ImageView

import com.sangcomz.fishbun.adapter.image.ImageAdapter
import com.squareup.picasso.Picasso

/**
 * Created by sangcomz on 23/07/2017.
 */

class PicassoAdapter : ImageAdapter {
    override fun loadImage(target: ImageView, loadUrl: Uri, orientation: Int) =
            Picasso
                    .get()
                    .load(loadUrl)
                    .fit()
                    .centerCrop()
                    .rotate(orientation.toFloat())
                    .into(target)

    override fun loadDetailImage(target: ImageView, loadUrl: Uri, orientation: Int) =
            Picasso.get()
                    .load(loadUrl)
                    .fit()
                    .centerInside()
                    .rotate(orientation.toFloat())
                    .into(target)
}
