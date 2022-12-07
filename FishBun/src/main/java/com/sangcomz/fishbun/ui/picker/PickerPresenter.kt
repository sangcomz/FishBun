package com.sangcomz.fishbun.ui.picker

import android.app.Activity
import android.net.Uri
import com.sangcomz.fishbun.R
import com.sangcomz.fishbun.ui.picker.model.PickerListItem
import com.sangcomz.fishbun.ui.picker.model.PickerMenuViewData
import com.sangcomz.fishbun.ui.picker.model.PickerRepository
import com.sangcomz.fishbun.util.UiHandler
import com.sangcomz.fishbun.util.future.CallableFutureTask
import com.sangcomz.fishbun.util.future.FutureCallback
import java.util.concurrent.ExecutionException

/**
 * Created by sangcomz on 2015-11-05.
 */
class PickerPresenter internal constructor(
    private val pickerView: PickerContract.View,
    private val pickerRepository: PickerRepository,
    private val uiHandler: UiHandler
) : PickerContract.Presenter {

    private var imageListFuture: CallableFutureTask<List<Uri>>? = null
    private var dirPathFuture: CallableFutureTask<String>? = null

    override fun addAddedPath(addedImagePath: Uri) {
        pickerRepository.addAddedPath(addedImagePath)
    }

    override fun addAllAddedPath(addedImagePathList: List<Uri>) {
        pickerRepository.addAllAddedPath(addedImagePathList)
    }

    override fun getAddedImagePathList() = pickerRepository.getAddedPathList()

    override fun getPickerListItem() {
        val albumData = pickerRepository.getPickerAlbumData() ?: return

        imageListFuture = getAllMediaThumbnailsPath(albumData.albumId)
            .also {
                it.execute(object : FutureCallback<List<Uri>> {
                    override fun onSuccess(result: List<Uri>) {
                        handleResult(result)
                    }
                })
            }
    }

    override fun transImageFinish() {
        val albumData = pickerRepository.getPickerAlbumData() ?: return

        pickerView.takeANewPictureWithFinish(
            albumData.albumPosition,
            pickerRepository.getAddedPathList()
        )
    }

    override fun takePicture() {
        val albumData = pickerRepository.getPickerAlbumData() ?: return
        if (albumData.albumId == 0L) {
            pickerRepository.getDefaultSavePath()?.let {
                pickerView.takePicture(it)
            }
        } else {
            try {
                dirPathFuture = pickerRepository.getDirectoryPath(albumData.albumId)
                    .also {
                        it.execute(object : FutureCallback<String> {
                            override fun onSuccess(result: String) {
                                pickerView.takePicture(result)
                            }
                        })
                    }
            } catch (e: ExecutionException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

        }
    }

    override fun successTakePicture(addedImagePath: Uri) {
        addAddedPath(addedImagePath)

        updatePickerListItem()
    }

    override fun getDesignViewData() {
        val viewData = pickerRepository.getPickerViewData()
        with(pickerView) {
            initToolBar(viewData)
            initRecyclerView(viewData)
        }

        setToolbarTitle()
    }

    override fun onClickThumbCount(position: Int) {
        changeImageStatus(position)
    }

    override fun onClickImage(position: Int) {
        if (pickerRepository.useDetailView()) {
            pickerView.showDetailView(getImagePosition(position))
        } else {
            changeImageStatus(position)
        }
    }

    override fun onDetailImageActivityResult() {
        val pickerViewData = pickerRepository.getPickerViewData()

        if (pickerRepository.isLimitReached() && pickerViewData.isAutomaticClose) {
            finish()
        } else {
            getPickerListItem()
        }
    }

    override fun getPickerMenuViewData(callback: (PickerMenuViewData) -> Unit) {
        callback.invoke(pickerRepository.getPickerMenuViewData())
    }

    override fun onClickMenuDone() {
        val selectedCount = pickerRepository.getSelectedImageList().size
        when {
            selectedCount == 0 -> {
                pickerView.showNothingSelectedMessage(pickerRepository.getMessageNotingSelected())
            }

            selectedCount < pickerRepository.getMinCount() -> {
                pickerView.showMinimumImageMessage(pickerRepository.getMinCount())
            }
            else -> {
                pickerView.finishActivity()
            }
        }
    }

    override fun onClickMenuAllDone() {
        pickerRepository.getPickerImages().forEach {
            if (pickerRepository.isLimitReached()) {
                return@forEach
            }
            if (pickerRepository.isNotSelectedImage(it)) {
                pickerRepository.selectImage(it)
            }
        }
        pickerView.finishActivity()
    }

    override fun onSuccessTakePicture() {
        pickerView.onSuccessTakePicture()
    }

    override fun release() {
        dirPathFuture?.cancel(true)
        imageListFuture?.cancel(true)
    }

    private fun changeImageStatus(position: Int) {
        val imagePosition = getImagePosition(position)
        val imageUri = pickerRepository.getPickerImage(imagePosition)

        if (pickerRepository.isNotSelectedImage(imageUri)) {
            selectImage(position, imageUri)
        } else {
            unselectImage(position, imageUri)
        }
    }

    private fun onCheckStateChange(position: Int, imageUri: Uri) {
        pickerView.onCheckStateChange(
            position,
            PickerListItem.Image(
                imageUri,
                pickerRepository.getSelectedIndex(imageUri),
                pickerRepository.getPickerViewData()
            )
        )
    }

    private fun selectImage(position: Int, imageUri: Uri) {
        if (pickerRepository.isLimitReached()) {
            pickerView.showLimitReachedMessage(pickerRepository.getMessageLimitReached())
            return
        }

        pickerRepository.selectImage(imageUri)

        if (pickerRepository.checkForFinish()) {
            finish()
        } else {
            onCheckStateChange(position, imageUri)
            setToolbarTitle()
        }
    }

    private fun unselectImage(position: Int, imageUri: Uri) {
        pickerRepository.unselectImage(imageUri)

        onCheckStateChange(position, imageUri)
        setToolbarTitle()
    }

    private fun setToolbarTitle() {
        val albumName = pickerRepository.getPickerAlbumData()?.albumName ?: ""
        pickerView.setToolbarTitle(
            pickerRepository.getPickerViewData(),
            pickerRepository.getSelectedImageList().size,
            albumName
        )
    }

    private fun getImagePosition(position: Int) =
        if (pickerRepository.hasCameraInPickerPage()) position - 1 else position

    private fun getAllMediaThumbnailsPath(
        albumId: Long,
        clearCache: Boolean = false
    ): CallableFutureTask<List<Uri>> {
        return pickerRepository.getAllBucketImageUri(albumId, clearCache)
    }

    private fun onSuccessAllMediaThumbnailsPath(imageUriList: List<Uri>) {
        val adapter = requireNotNull(pickerRepository.getImageAdapter())
        pickerRepository.setCurrentPickerImageList(imageUriList)

        val viewData = pickerRepository.getPickerViewData()
        val selectedImageList = pickerRepository.getSelectedImageList().toMutableList()
        val pickerList = arrayListOf<PickerListItem>()
        if (pickerRepository.hasCameraInPickerPage()) {
            pickerList.add(PickerListItem.Camera)
        }
        imageUriList.map {
            PickerListItem.Image(it, selectedImageList.indexOf(it), viewData)
        }.also {
            pickerList.addAll(it)
            uiHandler.run {
                pickerView.showImageList(
                    pickerList,
                    adapter,
                    pickerRepository.hasCameraInPickerPage()
                )
                setToolbarTitle()
            }
        }
    }

    private fun updatePickerListItem() {
        val albumData = pickerRepository.getPickerAlbumData() ?: return

        imageListFuture = getAllMediaThumbnailsPath(albumData.albumId, true)
            .also {
                it.execute(object : FutureCallback<List<Uri>> {
                    override fun onSuccess(result: List<Uri>) {
                        handleResult(result)
                    }
                })
            }
    }

    private fun finish() {
        if (pickerRepository.isStartInAllView()) {
            pickerView.finishActivityWithResult(pickerRepository.getSelectedImageList())
        } else {
            pickerView.finishActivity()
        }
    }

    private fun handleResult(result: List<Uri>) {
        if (pickerRepository.getImageAdapter() != null) {
            onSuccessAllMediaThumbnailsPath(result)
        } else {
            // imageAdapter is null, so we can not proceed anymore
            pickerView.showToastAndFinish(
                resId = R.string.msg_error,
                code = Activity.RESULT_CANCELED,
            )
        }
    }
}
