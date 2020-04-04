package com.sangcomz.fishbun.datasource

import android.net.Uri
import com.sangcomz.fishbun.MimeType
import com.sangcomz.fishbun.ui.detail.model.DetailImageViewData

interface FishBunDataSource {
    fun getSelectedImageList(): List<Uri>
    fun selectImage(imageUri: Uri)
    fun unselectImage(imageUri: Uri)
    fun getPickerImages(): List<Uri>
    fun getMaxCount(): Int
    fun getIsAutomaticClose(): Boolean
    fun getMessageLimitReached(): String
    fun getExceptMimeTypeList(): List<MimeType>
    fun getSpecifyFolderList(): List<String>
    fun getDetailImageModel(): DetailImageViewData
}