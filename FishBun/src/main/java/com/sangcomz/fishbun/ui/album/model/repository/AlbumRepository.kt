package com.sangcomz.fishbun.ui.album.model.repository

import android.net.Uri
import com.sangcomz.fishbun.adapter.image.ImageAdapter
import com.sangcomz.fishbun.ui.album.model.Album
import com.sangcomz.fishbun.ui.album.model.AlbumMenuViewData
import com.sangcomz.fishbun.ui.album.model.AlbumMetaData
import com.sangcomz.fishbun.ui.album.model.AlbumViewData
import com.sangcomz.fishbun.util.future.CallableFutureTask

interface AlbumRepository {
    fun getAlbumList(): CallableFutureTask<List<Album>>

    fun getAlbumMetaData(albumId: Long): CallableFutureTask<AlbumMetaData>

    fun getAlbumViewData(): AlbumViewData

    fun getImageAdapter(): ImageAdapter?

    fun getSelectedImageList(): List<Uri>

    fun getAlbumMenuViewData(): AlbumMenuViewData

    fun getMessageNotingSelected(): String

    fun getMinCount(): Int

    fun getDefaultSavePath(): String?
}