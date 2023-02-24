package com.sangcomz.fishbun.ui.album.mvp

import android.app.Activity
import android.net.Uri
import com.sangcomz.fishbun.R
import com.sangcomz.fishbun.ui.album.AlbumContract
import com.sangcomz.fishbun.ui.album.model.Album
import com.sangcomz.fishbun.ui.album.model.AlbumMenuViewData
import com.sangcomz.fishbun.ui.album.model.repository.AlbumRepository
import com.sangcomz.fishbun.util.UiHandler
import com.sangcomz.fishbun.util.future.CallableFutureTask
import com.sangcomz.fishbun.util.future.FutureCallback

class AlbumPresenter(
    private val albumView: AlbumContract.View,
    private val albumRepository: AlbumRepository,
    private val uiHandler: UiHandler
) : AlbumContract.Presenter {

    private var albumListFuture: CallableFutureTask<List<Album>>? = null

    override fun loadAlbumList() {
        albumListFuture = albumRepository.getAlbumList()
        albumListFuture?.execute(object : FutureCallback<List<Album>> {
            override fun onSuccess(result: List<Album>) {
                handleResult(result)
            }
        })
    }

    override fun takePicture() {
        albumRepository.getDefaultSavePath()?.let {
            albumView.takePicture(it)
        }
    }

    override fun getDesignViewData() {
        val viewData = albumRepository.getAlbumViewData()
        with(albumView) {
            setRecyclerView(viewData)
            setToolBar(viewData)
            changeToolbarTitle()
        }
    }

    override fun onResume() {
        albumView.setRecyclerViewSpanCount(albumRepository.getAlbumViewData())
    }

    override fun finish() {
        albumView.finishActivityWithResult(albumRepository.getSelectedImageList())
    }

    override fun refreshAlbumItem(position: Int, addedPathList: ArrayList<Uri>) {
        changeToolbarTitle()
        if (addedPathList.size > 0) {
            if (position == 0) loadAlbumList()
            else albumView.refreshAlbumItem(position, addedPathList)
        }
    }

    override fun getPathDir(): String? = albumRepository.getDefaultSavePath()

    override fun release() {
        albumListFuture?.cancel(true)
    }

    private fun changeToolbarTitle() {
        albumView.changeToolbarTitle(
            albumRepository.getSelectedImageList().size,
            albumRepository.getAlbumViewData()
        )
    }

    override fun getAlbumMenuViewData(callback: (AlbumMenuViewData) -> Unit) {
        callback.invoke(albumRepository.getAlbumMenuViewData())
    }

    override fun onClickMenuDone() {
        val selectedCount = albumRepository.getSelectedImageList().size
        when {
            selectedCount == 0 -> {
                albumView.showNothingSelectedMessage(albumRepository.getMessageNotingSelected())
            }

            selectedCount < albumRepository.getMinCount() -> {
                albumView.showMinimumImageMessage(albumRepository.getMinCount())
            }
            else -> {
                finish()
            }
        }
    }

    override fun onSuccessTakePicture() {
        albumView.saveImageForAndroidQOrHigher()
        albumView.scanAndRefresh()
    }

    private fun handleResult(result: List<Album>) {
        val adapter = albumRepository.getImageAdapter()
        // imageAdapter is null, so we can not proceed anymore
        if (adapter == null) {
            albumView.showToastAndFinish(
                resId = R.string.msg_error,
                code = Activity.RESULT_CANCELED,
            )
            return
        }
        uiHandler.run {
            if (result.isNotEmpty()) {
                changeToolbarTitle()
                albumView.showAlbumList(
                    result,
                    adapter,
                    albumRepository.getAlbumViewData()
                )
            } else {
                albumView.showEmptyView()
            }
        }
    }

    companion object {
        private const val defaultDir = "/Camera"
    }
}