package com.sangcomz.fishbun.datasource

import android.net.Uri
import com.sangcomz.fishbun.MimeType
import com.sangcomz.fishbun.ui.album.model.Album
import com.sangcomz.fishbun.ui.album.model.AlbumMetaData
import java.util.concurrent.Future

interface ImageDataSource {
    fun getAlbumList(allViewTitle: String): Future<List<Album>>

    fun getAllMediaThumbnailsPath(bucketId: Long): Future<List<Uri>>

    fun getAlbumMetaData(bucketId: Long): Future<AlbumMetaData>

    fun getDirectoryPath(bucketId: Long): Future<String>
}