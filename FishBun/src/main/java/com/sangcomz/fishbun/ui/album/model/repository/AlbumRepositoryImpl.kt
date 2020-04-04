package com.sangcomz.fishbun.ui.album.model.repository

import com.sangcomz.fishbun.MimeType
import com.sangcomz.fishbun.datasource.FishBunDataSource
import com.sangcomz.fishbun.datasource.ImageDataSource
import com.sangcomz.fishbun.ui.album.model.Album
import com.sangcomz.fishbun.ui.album.model.AlbumMetaData
import java.util.concurrent.Future

class AlbumRepositoryImpl(
    private val imageDataSource: ImageDataSource,
    private val fishBunDataSource: FishBunDataSource
) :
    AlbumRepository {
    override fun getAlbumList(allViewTitle: String): Future<List<Album>> {
        return imageDataSource.getAlbumList(
            allViewTitle,
            fishBunDataSource.getExceptMimeTypeList(),
            fishBunDataSource.getSpecifyFolderList()
        )
    }

    override fun getAlbumMetaData(albumId: Long): Future<AlbumMetaData> {
        return imageDataSource.getAlbumMetaData(
            albumId,
            fishBunDataSource.getExceptMimeTypeList(),
            fishBunDataSource.getSpecifyFolderList()
        )
    }
}