package com.sangcomz.fishbun.ui.album.model.repository

import com.sangcomz.fishbun.MimeType
import com.sangcomz.fishbun.ui.album.model.Album
import com.sangcomz.fishbun.ui.album.model.AlbumMetaData
import java.util.concurrent.Future

interface AlbumRepository {
    fun getAlbumList(allViewTitle: String): Future<List<Album>>

    fun getAlbumMetaData(albumId: Long): Future<AlbumMetaData>
}