package com.sangcomz.fishbun.ui.picker.model

import android.net.Uri

sealed class PickerListItem {

    abstract fun getItemId(): Long

    object Camera : PickerListItem() {
        override fun getItemId() = 0L
    }

    data class Image(
        val imageUri: Uri,
        val selectedIndex: Int,
        val viewData: PickerViewData
    ) : PickerListItem() {
        override fun getItemId() = imageUri.hashCode().toLong()

        constructor(imageUri: Uri, viewData: PickerViewData) : this(imageUri, -1, viewData)
    }
}


