package com.sangcomz.fishbun.ui.album.mvp

import android.net.Uri
import android.os.Environment
import com.sangcomz.fishbun.MimeType
import com.sangcomz.fishbun.ui.album.model.Album
import com.sangcomz.fishbun.ui.album.model.repository.AlbumRepository
import com.sangcomz.fishbun.ui.album.AlbumContract
import com.sangcomz.fishbun.ui.album.model.AlbumMenuViewData
import java.util.ArrayList
import java.util.concurrent.ExecutionException
import java.util.concurrent.Future

class AlbumPresenter(
    private val albumView: AlbumContract.View,
    private val albumRepository: AlbumRepository
) : AlbumContract.Presenter {

    private var albumListFuture: Future<List<Album>>? = null

    override fun loadAlbumList() {
        albumListFuture = albumRepository.getAlbumList()

        albumListFuture?.let {
            try {
                val albumList = it.get()
                if (albumList.isNotEmpty()) {
                    changeToolbarTitle()
                    albumView.showAlbumList(
                        it.get(),
                        albumRepository.getImageAdapter(),
                        albumRepository.getAlbumViewData()
                    )
                } else {
                    albumView.showEmptyView()
                }
            } catch (e: ExecutionException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    override fun getDesignViewData() {
        val viewData = albumRepository.getAlbumViewData()
        with(albumView) {
        }
    }

    override fun onResume() {
        albumView.setRecyclerViewSpanCount(albumRepository.getAlbumViewData())
    }

    override fun finish() {
        albumView.finishActivityWithResult(albumRepository.selectedImages())
    }

    override fun refreshAlbumItem(position: Int, addedPathList: ArrayList<Uri>) {
        changeToolbarTitle()
        if (addedPathList.size > 0) {
            if (position == 0) loadAlbumList()
            else albumView.refreshAlbumItem(position, addedPathList)
        }
    }

    override fun getAlbumMetaData(albumId: Long) = albumRepository.getAlbumMetaData(albumId)

    override fun onSuccessTakeAPick() {
        albumView.scanAndRefresh()
        changeToolbarTitle()
    }

    override fun getPathDir(): String = Environment
        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + defaultDir)
        .absolutePath

    override fun release() {
        albumListFuture?.cancel(true)
    }

    private fun changeToolbarTitle() {
        albumView.changeToolbarTitle(
            albumRepository.selectedImages().size,
            albumRepository.getAlbumViewData()
        )
    }

    override fun getAlbumMenuViewData(callback: (AlbumMenuViewData) -> Unit) {
        callback.invoke(albumRepository.getAlbumMenuViewData())
    }

    override fun done() {
        if (albumRepository.isNotEnoughSelectedImages()) {
            albumView.showSnackbar(albumRepository.getMessageNotingSelected())
        } else {
            finish()
        }
    }

    companion object {
        private const val defaultDir = "/Camera"
    }
}