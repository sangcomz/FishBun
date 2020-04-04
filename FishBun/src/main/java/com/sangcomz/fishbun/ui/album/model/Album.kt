package com.sangcomz.fishbun.ui.album.model

import com.sangcomz.fishbun.ui.album.model.AlbumMetaData

data class Album(
    val id: Long,
    val displayName: String,
    val metaData: AlbumMetaData
)