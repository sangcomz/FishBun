package com.sangcomz.fishbun.ui.album.model

import android.graphics.drawable.Drawable

data class AlbumViewData(
    val colorStatusBar: Int,
    val isStatusBarLight: Boolean,
    val colorActionBar: Int,
    val colorActionBarTitle: Int,
    val titleActionBar: String,
    val drawableHomeAsUpIndicator: Drawable?,
    val albumPortraitSpanCount: Int,
    val albumLandscapeSpanCount: Int,
    val albumThumbnailSize: Int,
    val maxCount: Int,
    val isShowCount: Boolean
)
