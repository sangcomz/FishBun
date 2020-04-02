package com.sangcomz.fishbun.ui.album

import android.os.Environment
import com.sangcomz.fishbun.MimeType
import com.sangcomz.fishbun.bean.Album
import com.sangcomz.fishbun.model.AlbumRepository
import java.util.concurrent.ExecutionException
import java.util.concurrent.Future

internal class AlbumPresenter(
    private val albumView: AlbumView,
    private val albumRepository: AlbumRepository
) {
    private var albumListFuture: Future<List<Album>>? = null

    val pathDir: String
        get() = Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/Camera")
            .absolutePath

    fun loadAlbumList(
        allViewTitle: String,
        exceptMimeTypeList: List<MimeType>,
        specifyFolderList: List<String>
    ) {
        albumListFuture =
            albumRepository.getAlbumList(allViewTitle, exceptMimeTypeList, specifyFolderList)

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

    fun release() {
        albumListFuture?.cancel(true)
    }

}