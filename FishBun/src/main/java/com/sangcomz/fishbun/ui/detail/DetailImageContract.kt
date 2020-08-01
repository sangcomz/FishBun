package com.sangcomz.fishbun.ui.detail

import android.net.Uri
import android.os.Message
import com.sangcomz.fishbun.adapter.image.ImageAdapter
import com.sangcomz.fishbun.ui.detail.model.DetailImageViewData

interface DetailImageContract {
    interface Presenter {
        fun handleOnCreate(initPosition: Int)
        fun onCountClick(position: Int)
        fun changeButtonStatus(position: Int)
    }

    interface View {
        fun unselectImage()
        fun updateRadioButtonWithText(text: String)
        fun updateRadioButtonWithDrawable()
        fun finishActivity()
        fun finishAndShowErrorToast()
        fun initViewPagerAdapter(imageAdapter: ImageAdapter)
        fun showImages(initPosition: Int, pickerImages: List<Uri>)
        fun showSnackbar(message: String)
        fun setBackButton()
        fun setToolBar(detailImageViewData: DetailImageViewData)
        fun setCountButton(detailImageViewData: DetailImageViewData)
    }
}