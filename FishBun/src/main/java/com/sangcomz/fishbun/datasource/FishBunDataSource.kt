package com.sangcomz.fishbun.datasource

import android.net.Uri
import com.sangcomz.fishbun.ui.detail.model.DetailImageViewData

interface FishBunDataSource {
    fun getSelectedImageList(): List<Uri>
    fun selectImage(imageUri: Uri)
    fun unselectImage(imageUri: Uri)
    fun getPickerImages(): List<Uri>
    fun getMaxCount(): Int
    fun getIsAutomaticClose(): Boolean
    fun getMessageLimitReached(): String
    fun getDetailImageModel(): DetailImageViewData
}