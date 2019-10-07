package com.sangcomz.fishbun.util

import android.content.Context
import androidx.annotation.DimenRes

fun Context.getDimension(@DimenRes id: Int) = resources.getDimension(id).toInt()