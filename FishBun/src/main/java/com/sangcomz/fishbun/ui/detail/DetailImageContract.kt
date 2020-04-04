package com.sangcomz.fishbun.ui.detail

import android.net.Uri
import android.os.Message

interface DetailImageContract {
    interface Presenter {
        fun getSelectedImage()
        fun onCountClick(position: Int)
        fun changeButtonStatus(position: Int)
        fun setInitPagerPosition(position: Int)
        fun getDesignViewData()
    }

    interface View {
        fun setToolBar(colorStatusBar: Int, isStatusBarLight: Boolean)
        fun unselectImage()
        fun updateRadioButtonWithText(text:String)
        fun updateRadioButtonWithDrawable()
        fun finishActivity()
        fun finishAndShowErrorToast()
        fun showImages(initPosition: Int, pickerImages: List<Uri>)
        fun showSnackbar(message: String)
        fun setCountButton(
            colorActionBar: Int,
            colorActionBarTitle: Int,
            colorSelectCircleStroke: Int
        )

        fun setBackButton()
    }
}