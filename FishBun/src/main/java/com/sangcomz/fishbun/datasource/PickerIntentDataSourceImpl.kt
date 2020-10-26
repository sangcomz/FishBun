package com.sangcomz.fishbun.datasource

import android.content.Intent
import com.sangcomz.fishbun.ui.picker.model.AlbumData

class PickerIntentDataSourceImpl(private val intent: Intent) : PickerIntentDataSource {
    override fun getAlbumData(): AlbumData? {
        return if (intent.hasExtra(ARG_ALBUM_NAME)
            && intent.hasExtra(ARG_ALBUM_ID)
            && intent.hasExtra(ARG_ALBUM_POSITION)
        )
            intent.getStringExtra(ARG_ALBUM_NAME)?.let {
                AlbumData(
                    intent.getLongExtra(ARG_ALBUM_ID, -1),
                        it,
                    intent.getIntExtra(ARG_ALBUM_POSITION, -1)
                )
            }
        else
            null
    }

    companion object {
        const val ARG_ALBUM_ID = "album_id"
        const val ARG_ALBUM_NAME = "album_name"
        const val ARG_ALBUM_POSITION = "album_position"
    }
}