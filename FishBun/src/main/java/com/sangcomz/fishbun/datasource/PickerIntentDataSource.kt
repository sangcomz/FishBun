package com.sangcomz.fishbun.datasource

import com.sangcomz.fishbun.ui.picker.model.AlbumData

interface PickerIntentDataSource {
    fun getAlbumData(): AlbumData?
}