package com.sangcomz.fishbun.ui.detail

import android.net.Uri
import android.os.Message
import com.sangcomz.fishbun.ui.detail.model.DetailImageViewData

interface DetailImageContract {
    interface Presenter {
        fun getSelectedImage()
        fun onCountClick(position: Int)
        fun changeButtonStatus(position: Int)
        fun getDesignViewData()
    }

    interface View {
        fun unselectImage()
        fun updateRadioButtonWithText(text:String)
        fun updateRadioButtonWithDrawable()
        fun finishActivity()
        fun finishAndShowErrorToast()
        fun showImages(initPosition: Int, pickerImages: List<Uri>)
        fun showSnackbar(message: String)
        fun setBackButton()
        fun setToolBar(detailImageViewData: DetailImageViewData)
        fun setCountButton(detailImageViewData: DetailImageViewData)
    }
}