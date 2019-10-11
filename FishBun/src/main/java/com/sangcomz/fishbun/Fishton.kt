package com.sangcomz.fishbun

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import com.sangcomz.fishbun.adapter.image.ImageAdapter
import com.sangcomz.fishbun.util.getDimension
import java.util.ArrayList

/**
 * Created by seokwon.jeong on 04/01/2018.
 */
class Fishton {
    var imageAdapter: ImageAdapter? = null
    var pickerImages: Array<Uri>? = null

    //BaseParams
    var maxCount: Int = 0
    var minCount: Int = 0
    var isExceptGif: Boolean = false
    var selectedImages = ArrayList<Uri>()

    //CustomizationParams
    var photoSpanCount: Int = 0
    var albumPortraitSpanCount: Int = 0
    var albumLandscapeSpanCount: Int = 0

    var isAutomaticClose: Boolean = false
    var isButton: Boolean = false

    var colorActionBar: Int = 0
    var colorActionBarTitle: Int = 0
    var colorStatusBar: Int = 0

    var isStatusBarLight: Boolean = false
    var isCamera: Boolean = false

    var albumThumbnailSize: Int = 0

    var messageNothingSelected: String? = null
    var messageLimitReached: String? = null
    var titleAlbumAllView: String? = null
    var titleActionBar: String? = null

    var drawableHomeAsUpIndicator: Drawable? = null
    var drawableDoneButton: Drawable? = null
    var drawableAllDoneButton: Drawable? = null
    var isUseAllDoneButton: Boolean = false

    var strDoneMenu: String? = null
    var strAllDoneMenu: String? = null

    var colorTextMenu: Int = 0

    var isUseDetailView: Boolean = false

    var isShowCount: Boolean = false

    var colorSelectCircleStroke: Int = 0

    var isStartInAllView: Boolean = false

    init {
        init()
    }

    fun refresh() = init()

    private fun init() {
        //Adapter
        imageAdapter = null

        //BaseParams
        maxCount = 10
        minCount = 1
        isExceptGif = true
        selectedImages = ArrayList()

        //CustomizationParams
        photoSpanCount = 3
        albumPortraitSpanCount = 1
        albumLandscapeSpanCount = 2

        isAutomaticClose = false
        isButton = false

        colorActionBar = Color.parseColor("#3F51B5")
        colorActionBarTitle = Color.parseColor("#ffffff")
        colorStatusBar = Color.parseColor("#303F9F")

        isStatusBarLight = false
        isCamera = false

        albumThumbnailSize = Integer.MAX_VALUE

        drawableHomeAsUpIndicator = null
        drawableDoneButton = null
        drawableAllDoneButton = null

        strDoneMenu = null
        strAllDoneMenu = null

        colorTextMenu = Integer.MAX_VALUE

        isUseAllDoneButton = false
        isUseDetailView = true
        isShowCount = true

        colorSelectCircleStroke = Color.parseColor("#c1ffffff")
        isStartInAllView = false
    }

    fun setDefaultMessage(context: Context) {
        messageNothingSelected =
            messageNothingSelected ?: context.getString(R.string.msg_no_selected)

        messageLimitReached =
            messageLimitReached ?: context.getString(R.string.msg_full_image)

        titleAlbumAllView =
            titleAlbumAllView ?: context.getString(R.string.str_all_view)

        titleActionBar =
            titleActionBar ?: context.getString(R.string.album)
    }

    fun setMenuTextColor() {
        if (drawableDoneButton != null
            || drawableAllDoneButton != null
            || strDoneMenu == null
            || colorTextMenu != Integer.MAX_VALUE)
            return

        colorTextMenu = if (isStatusBarLight) Color.BLACK else colorTextMenu
    }

    fun setDefaultDimen(context: Context) {
        albumThumbnailSize =
            if (albumThumbnailSize == Int.MAX_VALUE) {
                context.getDimension(R.dimen.album_thum_size)
            } else {
                albumThumbnailSize
            }
    }

    private object FishtonHolder {
        val INSTANCE = Fishton()
    }

    companion object {
        @JvmStatic
        fun getInstance() = FishtonHolder.INSTANCE
    }
}