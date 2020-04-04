package com.sangcomz.fishbun.ui.picker

import android.net.Uri

interface PickerView{
    fun showImageList(imageList: List<Uri>)
}