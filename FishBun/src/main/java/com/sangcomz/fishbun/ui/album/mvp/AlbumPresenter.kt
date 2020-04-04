package com.sangcomz.fishbun.ui.album.mvp

import android.os.Environment
import com.sangcomz.fishbun.MimeType
import com.sangcomz.fishbun.ui.album.model.Album
import com.sangcomz.fishbun.ui.album.model.repository.AlbumRepository
import com.sangcomz.fishbun.ui.album.AlbumContract
import java.util.concurrent.ExecutionException
import java.util.concurrent.Future

internal class AlbumPresenter(
    private val albumView: AlbumContract.View,
    private val albumRepository: AlbumRepository
) : AlbumContract.Presenter {

    private var albumListFuture: Future<List<Album>>? = null
    override fun loadAlbumList(allViewTitle: String) {
        albumListFuture = albumRepository.getAlbumList(allViewTitle)

        albumListFuture?.let {
            try {
                albumView.showAlbumList(it.get())
            } catch (e: ExecutionException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

   override fun getAlbumMetaData(albumId: Long) = albumRepository.getAlbumMetaData(albumId)

    override fun getPathDir(): String = Environment
        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + defaultDir)
        .absolutePath

    override fun release() {
        albumListFuture?.cancel(true)
    }

    companion object {
        private const val defaultDir = "/Camera"
    }
}