package com.sangcomz.fishbun.ui.picker.model

import android.net.Uri
import com.sangcomz.fishbun.adapter.image.ImageAdapter
import com.sangcomz.fishbun.util.future.CallableFutureTask

interface PickerRepository {
    fun getAllBucketImageUri(bucketId: Long, clearCache: Boolean): CallableFutureTask<List<Uri>>

    fun getDirectoryPath(bucketId: Long): CallableFutureTask<String>

    fun addAddedPath(addedImage: Uri)

    fun getAddedPathList(): List<Uri>

    fun addAllAddedPath(addedImagePathList: List<Uri>)

    fun getPickerAlbumData(): AlbumData?

    fun getPickerViewData(): PickerViewData

    fun setCurrentPickerImageList(pickerImageList: List<Uri>)

    fun getSelectedImageList(): List<Uri>

    fun getImageAdapter(): ImageAdapter?

    fun hasCameraInPickerPage(): Boolean

    fun useDetailView(): Boolean

    fun isLimitReached(): Boolean

    fun selectImage(imageUri: Uri)

    fun unselectImage(imageUri: Uri)

    fun isNotSelectedImage(imageUri: Uri): Boolean

    fun getSelectedImage(position: Int): Uri

    fun getSelectedIndex(imageUri: Uri): Int

    fun getPickerImage(imagePosition: Int): Uri

    fun getMessageLimitReached(): String

    fun getPickerMenuViewData(): PickerMenuViewData

    fun getMinCount(): Int

    fun getMaxCount(): Int

    fun getPickerImages(): List<Uri>

    fun getMessageNotingSelected(): String

    fun checkForFinish(): Boolean

    fun isStartInAllView(): Boolean

    fun getDefaultSavePath(): String?
}