package com.sangcomz.fishbun.ui.detail.mvp

import android.net.Uri
import com.sangcomz.fishbun.ui.detail.DetailImageContract
import com.sangcomz.fishbun.ui.detail.model.DetailImageRepository

/**
 * Created by sangcomz on 11/06/2017.
 */
class DetailImagePresenter(
    private val detailView: DetailImageContract.View,
    private val detailImageRepository: DetailImageRepository
) : DetailImageContract.Presenter {

    override fun setInitPagerPosition(position: Int) {
        val pickerImages = detailImageRepository.getPickerImages()
        if (pickerImages.isNotEmpty()) {
            changeButtonStatus(position)
            detailView.showImages(position, pickerImages)
        } else {
            detailView.finishAndShowErrorToast()
        }
    }

    override fun getDesignViewData() {
        with(detailImageRepository.getDetailPickerViewData()) {
            detailView.setToolBar(colorStatusBar, isStatusBarLight)
            detailView.setCountButton(colorActionBar, colorActionBarTitle, colorSelectCircleStroke)
            detailView.setBackButton()
        }
    }

    override fun getSelectedImage() {
        detailImageRepository.getSelectedImageList()
    }

    override fun changeButtonStatus(position: Int) {
        getPickerImageUriByIndex(position)?.let {
            changeButtonStatusInternal(it)
        }
    }

    override fun onCountClick(position: Int) {
        val imageUri = getPickerImageUriByIndex(position) ?: return

        if (isSelectedImage(imageUri)) {
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

    private fun changeButtonStatusInternal(imageUri: Uri) {
        val imageIndex = getSelectedImageIndex(imageUri)
        if (imageIndex != -1) {
            if (detailImageRepository.getMaxCount() == 1) {
                detailView.updateRadioButtonWithDrawable()
            } else {
                detailView.updateRadioButtonWithText("${imageIndex + 1}")
            }
        } else {
            detailView.unselectImage()
        }
    }

    private fun getPickerImageUriByIndex(index: Int) = detailImageRepository.getPickerImage(index)

    private fun getSelectedImageIndex(imageUri: Uri) =
        detailImageRepository.getSelectedImageList().indexOf(imageUri)

    private fun isSelectedImage(imageUri: Uri) =
        detailImageRepository.getSelectedImageList().contains(imageUri)
}