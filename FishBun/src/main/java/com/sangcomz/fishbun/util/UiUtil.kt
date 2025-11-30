@file:JvmName("UiUtil")

package com.sangcomz.fishbun.util

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.view.View
import android.view.WindowInsetsController
import androidx.annotation.ColorInt
import androidx.annotation.DimenRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat


/**
 * Created by sangc on 2015-11-20.
 */

fun Activity.setStatusBarColor(@ColorInt colorStatusBar: Int, isLightStatusBar: Boolean = false) {
    if (colorStatusBar == Integer.MAX_VALUE) {
        return
    }

    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM -> {
            val isLightStatusBars =
                AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_NO
            val compat = WindowInsetsControllerCompat(window, window.decorView)
            compat.isAppearanceLightStatusBars = isLightStatusBars
            compat.isAppearanceLightNavigationBars = isLightStatusBars

            // Set light status bar appearance using WindowCompat
            WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars =
                isLightStatusBar
        }

        Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
            window.statusBarColor = colorStatusBar
            window.insetsController?.setSystemBarsAppearance(
                if (isLightStatusBar) WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS else 0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }

        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
            window.statusBarColor = colorStatusBar
            val flags = window.decorView.systemUiVisibility
            window.decorView.systemUiVisibility = if (isLightStatusBar) {
                flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
        }

        else -> {
            // Android 5.0+ (API 21+) - Basic status bar color
            @Suppress("DEPRECATION")
            window.statusBarColor = colorStatusBar
        }
    }
}

fun Context.isLandscape() =
    resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

inline fun <T : Context> T.isLandscape(block: () -> Unit) = if (isLandscape()) block() else Unit

fun Context.getDimension(@DimenRes id: Int) = resources.getDimension(id).toInt()

fun Resources.getDrawableFromBitmap(bitmap: Bitmap?) = bitmap?.let {
    BitmapDrawable(this, it)
}
