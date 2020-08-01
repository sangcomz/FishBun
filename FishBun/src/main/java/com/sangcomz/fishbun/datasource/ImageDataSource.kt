package com.sangcomz.fishbun.datasource

import android.net.Uri
import com.sangcomz.fishbun.MimeType
import com.sangcomz.fishbun.ui.album.model.Album
import com.sangcomz.fishbun.ui.album.model.AlbumMetaData
import com.sangcomz.fishbun.util.future.CallableFutureTask

interface ImageDataSource {
    fun getAlbumList(
        allViewTitle: String,
        exceptMimeTypeList: List<MimeType>,
        specifyFolderList: List<String>
    ): CallableFutureTask<List<Album>>

    fun getAllBucketImageUri(
        bucketId: Long,
        exceptMimeTypeList: List<MimeType>,
        specifyFolderList: List<String>
    ): CallableFutureTask<List<Uri>>

    fun getAlbumMetaData(
        bucketId: Long,
        exceptMimeTypeList: List<MimeType>,
        specifyFolderList: List<String>
    ): CallableFutureTask<AlbumMetaData>

    fun getDirectoryPath(bucketId: Long): CallableFutureTask<String>

    fun addAddedPath(addedImage: Uri)

    fun addAllAddedPath(addedImageList: List<Uri>)

    fun getAddedPathList(): List<Uri>
}