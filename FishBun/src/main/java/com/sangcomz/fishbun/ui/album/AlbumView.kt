package com.sangcomz.fishbun.ui.album

import com.sangcomz.fishbun.bean.Album

interface AlbumView{
    fun showAlbumList(albumList: List<Album>)
}