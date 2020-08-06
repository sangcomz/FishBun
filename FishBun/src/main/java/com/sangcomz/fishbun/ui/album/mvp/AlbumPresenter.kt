package com.sangcomz.fishbun.ui.album.mvp

import android.net.Uri
import android.os.Environment
import com.sangcomz.fishbun.ui.album.model.Album
import com.sangcomz.fishbun.ui.album.model.repository.AlbumRepository
import com.sangcomz.fishbun.ui.album.AlbumContract
import com.sangcomz.fishbun.ui.album.model.AlbumMenuViewData
import com.sangcomz.fishbun.util.UiHandler
import com.sangcomz.fishbun.util.future.CallableFutureTask
import com.sangcomz.fishbun.util.future.FutureCallback
import java.util.ArrayList

class AlbumPresenter(
    private val albumView: AlbumContract.View,
    private val albumRepository: AlbumRepository,
    private val uiHandler: UiHandler
) : AlbumContract.Presenter {

    private var albumListFuture: CallableFutureTask<List<Album>>? = null

    override fun loadAlbumList() {
        albumListFuture = albumRepository.getAlbumList()

        albumListFuture?.let {
            it.execute(object : FutureCallback<List<Album>> {
                override fun onSuccess(result: List<Album>) {
                    uiHandler.run {
                        if (result.isNotEmpty()) {
                            changeToolbarTitle()
                            albumView.showAlbumList(
                                it.get(),
                                albumRepository.getImageAdapter(),
                                albumRepository.getAlbumViewData()
                            )
                        } else {
                            albumView.showEmptyView()
                        }
                    }
                }
            })
        }
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

    companion object {
        private const val defaultDir = "/Camera"
    }
}