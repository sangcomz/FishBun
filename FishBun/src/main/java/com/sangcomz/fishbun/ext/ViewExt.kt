package com.sangcomz.fishbun.ext

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBar(resId: Int, duration: Int = Snackbar.LENGTH_SHORT) {
    this.showSnackBar(context.getString(resId), duration)
}

fun View.showSnackBar(charSequence: CharSequence?, duration: Int = Snackbar.LENGTH_SHORT) {
    if (charSequence.isNullOrEmpty()) return
    Snackbar.make(this, charSequence, duration)
}