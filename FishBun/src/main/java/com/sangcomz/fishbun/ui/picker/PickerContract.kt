package com.sangcomz.fishbun.ui.picker

import android.net.Uri
import com.sangcomz.fishbun.adapter.image.ImageAdapter
import com.sangcomz.fishbun.ui.album.model.AlbumMenuViewData
import com.sangcomz.fishbun.ui.picker.model.PickerListItem
import com.sangcomz.fishbun.ui.picker.model.PickerMenuViewData
import com.sangcomz.fishbun.ui.picker.model.PickerViewData

interface PickerContract {
    interface View {
        fun showImageList(imageList: List<PickerListItem>, adapter: ImageAdapter)
        fun takePicture(saveDir: String)
        fun setToolbarTitle(
            pickerViewData: PickerViewData,
            selectedCount: Int,
            albumName: String
        )

        fun initToolBar(pickerViewData: PickerViewData)
        fun initRecyclerView(pickerViewData: PickerViewData)
        fun showLimitReachedMessage(messageLimitReached: String)
        fun onCheckStateChange(position: Int, item: PickerListItem.Item)
        fun showDetailView(position: Int)
        fun finishActivity()
        fun finishActivityWithResult(selectedImages: List<Uri>)
        fun transImageFinish(position: Int, addedImageList: List<Uri>)
    }

    interface Presenter {
        fun takePicture()
        fun getAddedImagePathList(): List<Uri>
        fun addAddedPath(addedImagePath: Uri)
        fun addAllAddedPath(addedImagePathList: List<Uri>)
        fun release()
        fun successTakePicture(addedImagePath: Uri)
        fun getPickerListItem()
        fun transImageFinish()
        fun getDesignViewData()
        fun onClickThumbCount(position: Int)
        fun onClickImage(position: Int)
        fun onDetailImageActivityResult()
        fun getPickerMenuViewData(callback: (PickerMenuViewData) -> Unit)
        fun onClickMenuDone()
        fun onClickMenuAllDone()
    }
}