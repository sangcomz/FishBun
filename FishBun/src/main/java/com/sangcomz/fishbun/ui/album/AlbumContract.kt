package com.sangcomz.fishbun.ui.album

import android.net.Uri
import androidx.annotation.StringRes
import com.sangcomz.fishbun.adapter.image.ImageAdapter
import com.sangcomz.fishbun.ui.album.model.Album
import com.sangcomz.fishbun.ui.album.model.AlbumMenuViewData
import com.sangcomz.fishbun.ui.album.model.AlbumViewData

interface AlbumContract {
    interface View {
        fun showAlbumList(
            albumList: List<Album>,
            imageAdapter: ImageAdapter,
            albumViewData: AlbumViewData
        )

        fun showEmptyView()
        fun setRecyclerViewSpanCount(albumViewData: AlbumViewData)
        fun setRecyclerView(albumViewData: AlbumViewData)
        fun setToolBar(albumViewData: AlbumViewData)
        fun changeToolbarTitle(selectedImageCount: Int, albumViewData: AlbumViewData)
        fun finishActivityWithResult(selectedImages: List<Uri>)

        /* show toast and finish the Activity */
        fun showToastAndFinish(@StringRes resId: Int, code: Int)
        fun refreshAlbumItem(position: Int, imagePath: ArrayList<Uri>)
        fun scanAndRefresh()
        fun showNothingSelectedMessage(nothingSelectedMessage: String)
        fun showMinimumImageMessage(currentSelectedCount: Int)
        fun takePicture(saveDir: String)
        fun saveImageForAndroidQOrHigher()
    }

    interface Presenter {
        fun loadAlbumList()
        fun takePicture()
        fun getPathDir(): String?
        fun release()
        fun getDesignViewData()
        fun onResume()
        fun finish()
        fun refreshAlbumItem(position: Int, addedPathList: ArrayList<Uri>)
        fun getAlbumMenuViewData(callback: (AlbumMenuViewData) -> Unit)
        fun onClickMenuDone()
        fun onSuccessTakePicture()
    }
}