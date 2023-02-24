package com.sangcomz.fishbun.ui.picker.model

import android.net.Uri
import android.os.Build
import android.os.Environment
import com.sangcomz.fishbun.adapter.image.ImageAdapter
import com.sangcomz.fishbun.datasource.CameraDataSource
import com.sangcomz.fishbun.datasource.FishBunDataSource
import com.sangcomz.fishbun.datasource.ImageDataSource
import com.sangcomz.fishbun.util.future.CallableFutureTask
import com.sangcomz.fishbun.datasource.PickerIntentDataSource

class PickerRepositoryImpl(
    private val imageDataSource: ImageDataSource,
    private val fishBunDataSource: FishBunDataSource,
    private val pickerIntentDataSource: PickerIntentDataSource,
    private val cameraDataSource: CameraDataSource
) : PickerRepository {

    private var cachedAllMediaThumbNailPath: CallableFutureTask<List<Uri>>? = null

    override fun getAllBucketImageUri(
        bucketId: Long,
        clearCache: Boolean
    ): CallableFutureTask<List<Uri>> {
        if (clearCache) cachedAllMediaThumbNailPath = null

        return cachedAllMediaThumbNailPath
            ?: imageDataSource.getAllBucketImageUri(
                bucketId,
                fishBunDataSource.getExceptMimeTypeList(),
                fishBunDataSource.getSpecifyFolderList()
            ).also { cachedAllMediaThumbNailPath = it }
    }

    override fun getDirectoryPath(bucketId: Long): CallableFutureTask<String> {
        return imageDataSource.getDirectoryPath(bucketId)
    }

    override fun addAddedPath(addedImage: Uri) {
        imageDataSource.addAddedPath(addedImage)
    }

    override fun getAddedPathList() = imageDataSource.getAddedPathList()

    override fun addAllAddedPath(addedImagePathList: List<Uri>) {
        imageDataSource.addAllAddedPath(addedImagePathList)
    }

    override fun getPickerAlbumData(): AlbumData? {
        return pickerIntentDataSource.getAlbumData()
    }

    override fun getPickerViewData(): PickerViewData = fishBunDataSource.getPickerViewData()

    override fun setCurrentPickerImageList(pickerImageList: List<Uri>) {
        fishBunDataSource.setCurrentPickerImageList(pickerImageList)
    }

    override fun getSelectedImageList() = fishBunDataSource.getSelectedImageList()

    override fun getImageAdapter(): ImageAdapter? = fishBunDataSource.getImageAdapter()

    override fun hasCameraInPickerPage(): Boolean {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                fishBunDataSource.hasCameraInPickerPage()
                        && pickerIntentDataSource.getAlbumData()?.albumId == 0L
            }
            else -> {
                fishBunDataSource.hasCameraInPickerPage()
            }
        }
    }

    override fun useDetailView() = fishBunDataSource.useDetailView()

    override fun isLimitReached() =
        fishBunDataSource.getMaxCount() == fishBunDataSource.getSelectedImageList().size

    override fun selectImage(imageUri: Uri) = fishBunDataSource.selectImage(imageUri)

    override fun unselectImage(imageUri: Uri) = fishBunDataSource.unselectImage(imageUri)

    override fun isNotSelectedImage(imageUri: Uri) =
        !fishBunDataSource.getSelectedImageList().contains(imageUri)

    override fun getSelectedImage(position: Int): Uri =
        fishBunDataSource.getSelectedImageList()[position]

    override fun getSelectedIndex(imageUri: Uri) = getSelectedImageList().indexOf(imageUri)

    override fun getPickerImage(imagePosition: Int) =
        fishBunDataSource.getPickerImages()[imagePosition]

    override fun getMessageLimitReached() = fishBunDataSource.getMessageLimitReached()

    override fun getPickerMenuViewData(): PickerMenuViewData =
        fishBunDataSource.getPickerMenuViewData()

    override fun getMinCount() = fishBunDataSource.getMinCount()

    override fun getMaxCount() = fishBunDataSource.getMinCount()

    override fun getPickerImages() = fishBunDataSource.getPickerImages()

    override fun getMessageNotingSelected() = fishBunDataSource.getMessageNothingSelected()

    override fun checkForFinish(): Boolean =
        fishBunDataSource.getIsAutomaticClose()
                && fishBunDataSource.getSelectedImageList().size == fishBunDataSource.getMaxCount()

    override fun isStartInAllView() = fishBunDataSource.isStartInAllView()

    override fun getDefaultSavePath(): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            cameraDataSource.getPicturePath()
        } else {
            cameraDataSource.getCameraPath()
        }
    }
}