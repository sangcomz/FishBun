package com.sangcomz.fishbun.ui.detail.mvp

import android.net.Uri
import androidx.annotation.VisibleForTesting
import com.sangcomz.fishbun.ui.detail.DetailImageContract
import com.sangcomz.fishbun.ui.detail.model.DetailImageRepository

/**
 * Created by sangcomz on 11/06/2017.
 */
class DetailImagePresenter(
    private val detailView: DetailImageContract.View,
    private val detailImageRepository: DetailImageRepository,
    val position: Int
) : DetailImageContract.Presenter {

    init {
        initViewPagerAdapter()
        initPagerPosition(position)
    }

    override fun getDesignViewData() {
        val detailImageViewData = detailImageRepository.getDetailPickerViewData()
        with(detailView) {
            setToolBar(detailImageViewData)
            setCountButton(detailImageViewData)
            setBackButton()
        }
    }

    override fun changeButtonStatus(position: Int) {
        detailImageRepository.getPickerImage(position)?.let {
            changeButtonStatusInternal(it)
        }
    }

    override fun onCountClick(position: Int) {
        val imageUri = detailImageRepository.getPickerImage(position) ?: return
        val isSelected = detailImageRepository.isSelected(imageUri)

        if (isSelected) {
            detailImageRepository.unselectImage(imageUri)
        } else {
            if (detailImageRepository.isFullSelected()) {
                detailView.showSnackbar(detailImageRepository.getMessageLimitReached())
            } else {
                detailImageRepository.selectImage(imageUri)
                if (detailImageRepository.checkForFinish()) {
                    detailView.finishActivity()
                }
            }
        }

        changeButtonStatusInternal(imageUri)
    }

    private fun initPagerPosition(position: Int) {
        val pickerImages = detailImageRepository.getPickerImages()
        if (pickerImages.isNotEmpty()) {
            changeButtonStatus(position)
            detailView.showImages(position, pickerImages)
        } else {
            detailView.finishAndShowErrorToast()
        }
    }

    private fun initViewPagerAdapter() {
        detailView.initViewPagerAdapter(detailImageRepository.getImageAdapter())
    }

    private fun changeButtonStatusInternal(imageUri: Uri) {
        when (val imageIndex = detailImageRepository.getImageIndex(imageUri)) {
            -1 -> detailView.unselectImage()

            else -> {
                if (detailImageRepository.getMaxCount() == 1) {
                    detailView.updateRadioButtonWithDrawable()
                } else {
                    detailView.updateRadioButtonWithText("${imageIndex + 1}")
                }
            }
        }
    }
}