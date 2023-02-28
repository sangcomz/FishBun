package com.sangcomz.fishbun.adapter.image.impl

import android.net.Uri
import android.widget.ImageView
import com.sangcomz.fishbun.adapter.image.ImageAdapter
import com.squareup.picasso.Picasso

class PicassoAdapter: ImageAdapter {
    override fun loadImage(target: ImageView, loadUrl: Uri) {
        Picasso.get()
            .load(loadUrl)
            .fit()
            .centerCrop()
            .into(target)
    }
    override fun loadDetailImage(target: ImageView, loadUrl: Uri) {
        Picasso.get()
            .load(loadUrl)
            .into(target)
    }
}
