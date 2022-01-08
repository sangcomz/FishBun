package com.sangcomz.fishbun

import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher

/**
 * Created by sangcomz on 13/05/2017.
 */
interface BaseProperty {
    fun setSelectedImages(selectedImages: ArrayList<Uri>): FishBunCreator

    fun setPickerCount(count: Int): FishBunCreator

    fun setMaxCount(count: Int): FishBunCreator

    fun setMinCount(count: Int): FishBunCreator

    fun setRequestCode(requestCode: Int): FishBunCreator

    fun setReachLimitAutomaticClose(isAutomaticClose: Boolean): FishBunCreator

    fun exceptGif(isExcept: Boolean): FishBunCreator

    fun exceptMimeType(exceptMimeTypeList: List<MimeType>): FishBunCreator

    fun startAlbum()

    fun startAlbumWithOnActivityResult(requestCode: Int)

    fun startAlbumWithActivityResultCallback(activityResultLauncher: ActivityResultLauncher<Intent>)
}