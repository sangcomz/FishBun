package com.sangcomz.fishbun.ui.album.listener

import com.sangcomz.fishbun.ui.album.model.Album

interface AlbumClickListener {
    fun onAlbumClick(position: Int, album: Album)
}