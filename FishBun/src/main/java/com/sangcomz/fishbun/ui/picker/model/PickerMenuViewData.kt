package com.sangcomz.fishbun.ui.picker.model

import android.graphics.drawable.Drawable

data class PickerMenuViewData(
    val drawableDoneButton: Drawable?,
    val drawableAllDoneButton: Drawable?,
    val strDoneMenu: String?,
    val colorTextMenu: Int,
    val strAllDoneMenu:String?,
    val isUseAllDoneButton: Boolean
)
