@file:JvmName("UiUtil")

package com.sangcomz.fishbun.util

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.view.WindowManager
import androidx.annotation.DimenRes

/**
 * Created by sangc on 2015-11-20.
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
fun Activity.setStatusBarColor(colorStatusBar: Int) {
    if (colorStatusBar == Integer.MAX_VALUE) {
        return
    }
    with(window) {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        statusBarColor = colorStatusBar
    }
}

fun Context.isLandscape() = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

inline fun <T : Context> T.isLandscape(block: () -> Unit) = if (isLandscape()) block() else Unit

fun Context.getDimension(@DimenRes id: Int) = resources.getDimension(id).toInt()

fun Resources.getDrawableFromBitmap(bitmap: Bitmap?) = bitmap?.let {
    BitmapDrawable(this, it)
}
