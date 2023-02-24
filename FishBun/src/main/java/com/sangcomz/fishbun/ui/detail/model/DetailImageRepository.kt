package com.sangcomz.fishbun.ui.detail.model

import android.net.Uri
import com.sangcomz.fishbun.adapter.image.ImageAdapter

interface DetailImageRepository {
    fun getPickerImage(index: Int): Uri?
    fun getPickerImages(): List<Uri>
    fun isSelected(imageUri: Uri): Boolean
    fun getImageIndex(imageUri: Uri): Int
    fun selectImage(imageUri: Uri)
    fun unselectImage(imageUri: Uri)
    fun getImageAdapter(): ImageAdapter?
    fun isFullSelected(): Boolean
    fun checkForFinish(): Boolean
    fun getMessageLimitReached(): String
    fun getMaxCount(): Int
    fun getDetailPickerViewData(): DetailImageViewData
}