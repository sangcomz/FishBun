package com.sangcomz.fishbun.model

import com.sangcomz.fishbun.MimeType
import com.sangcomz.fishbun.bean.Album
import java.util.concurrent.Future

class AlbumRepositoryImpl(private val imageDataSource: ImageDataSource) : AlbumRepository {
    override fun getAlbumList(
        allViewTitle: String,
        exceptMimeTypeList: List<MimeType>,
        specifyFolderList: List<String>
    ): Future<List<Album>> {
        return imageDataSource.getAlbumList(allViewTitle, exceptMimeTypeList, specifyFolderList)
    }
}