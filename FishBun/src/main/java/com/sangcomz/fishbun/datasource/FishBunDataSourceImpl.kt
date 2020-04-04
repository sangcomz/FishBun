package com.sangcomz.fishbun.datasource

import android.net.Uri
import com.sangcomz.fishbun.Fishton
import com.sangcomz.fishbun.ui.detail.model.DetailImageViewData

class FishBunDataSourceImpl(private val fishton: Fishton) : FishBunDataSource {
    override fun getSelectedImageList(): List<Uri> = fishton.selectedImages

    override fun getPickerImages(): List<Uri> = fishton.pickerImages

    override fun getMaxCount(): Int = fishton.maxCount

    override fun getIsAutomaticClose(): Boolean = fishton.isAutomaticClose

    override fun getMessageLimitReached() = fishton.messageLimitReached

    override fun getDetailImageModel() = DetailImageViewData(
        fishton.colorStatusBar,
        fishton.isStatusBarLight,
        fishton.colorActionBar,
        fishton.colorActionBarTitle,
        fishton.colorSelectCircleStroke
    )

    override fun selectImage(imageUri: Uri) {
        fishton.selectedImages.add(imageUri)
    }

    override fun unselectImage(imageUri: Uri) {
        fishton.selectedImages.remove(imageUri)
    }

}