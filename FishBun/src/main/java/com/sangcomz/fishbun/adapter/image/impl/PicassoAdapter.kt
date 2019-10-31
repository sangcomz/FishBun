package com.sangcomz.fishbun.adapter.image.impl

import android.net.Uri
import android.widget.ImageView

import com.sangcomz.fishbun.adapter.image.ImageAdapter
import com.squareup.picasso.Picasso


/**
 * Created by sangcomz on 23/07/2017.
 */

class PicassoAdapter : ImageAdapter {
    override fun loadImage(target: ImageView, loadUrl: Uri) =
            Picasso.with(target.context)
                    .load(loadUrl)
                    .fit()
                    .centerCrop()
                    .into(target)

    override fun loadDetailImage(target: ImageView, loadUrl: Uri) =
            Picasso.with(target.context)
                    .load(loadUrl)
                    .fit()
                    .centerInside()
                    .into(target)
}
