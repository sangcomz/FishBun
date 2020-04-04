package com.sangcomz.fishbun.ui.album

import com.sangcomz.fishbun.MimeType
import com.sangcomz.fishbun.ui.album.model.Album
import com.sangcomz.fishbun.ui.album.model.AlbumMetaData
import java.util.concurrent.Future

interface AlbumContract {
    interface Presenter {
        fun loadAlbumList(allViewTitle: String)
        fun getAlbumMetaData(albumId: Long): Future<AlbumMetaData>
        fun getPathDir(): String
        fun release()
    }

    interface View {
        fun showAlbumList(albumList: List<Album>)
    }
}