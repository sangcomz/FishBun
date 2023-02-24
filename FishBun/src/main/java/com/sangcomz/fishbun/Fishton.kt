package com.sangcomz.fishbun

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import com.sangcomz.fishbun.adapter.image.ImageAdapter
import com.sangcomz.fishbun.util.getDimension

/**
 * Created by seokwon.jeong on 04/01/2018.
 */
object Fishton {
    // System may destroy Activity due to system constraints
    // such as configuration change or memory pressure.
    // That's why this should be belonged to callsite's lifecycle.
    var imageAdapter: ImageAdapter? = null
    var currentPickerImageList: List<Uri> = emptyList()

    //BaseParams
    var maxCount: Int = 0
    var minCount: Int = 0
    var exceptMimeTypeList = emptyList<MimeType>()
    var selectedImages = ArrayList<Uri>()

    //CustomizationParams
    var specifyFolderList = emptyList<String>()
    var photoSpanCount: Int = 0
    var albumPortraitSpanCount: Int = 0
    var albumLandscapeSpanCount: Int = 0

    var isAutomaticClose: Boolean = false
    var hasButtonInAlbumActivity: Boolean = false

    var colorActionBar: Int = 0
    var colorActionBarTitle: Int = 0
    var colorStatusBar: Int = 0

    var isStatusBarLight: Boolean = false
    var hasCameraInPickerPage: Boolean = false

    var albumThumbnailSize: Int = 0

    var messageNothingSelected: String = ""
    var messageLimitReached: String = ""
    var titleAlbumAllView: String = ""
    var titleActionBar: String = ""

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
        initValue()
    }

    fun refresh() = initValue()

    private fun initValue() {
        //BaseParams
        maxCount = 10
        minCount = 1
        exceptMimeTypeList = emptyList()
        selectedImages = ArrayList()

        //CustomizationParams
        specifyFolderList = emptyList()
        photoSpanCount = 4
        albumPortraitSpanCount = 1
        albumLandscapeSpanCount = 2

        isAutomaticClose = false
        hasButtonInAlbumActivity = false

        colorActionBar = Color.parseColor("#3F51B5")
        colorActionBarTitle = Color.parseColor("#ffffff")
        colorStatusBar = Color.parseColor("#303F9F")

        isStatusBarLight = false
        hasCameraInPickerPage = false

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
        if (messageNothingSelected.isEmpty()) {
            messageNothingSelected = context.getString(R.string.msg_no_selected)
        }

        if (messageLimitReached.isEmpty()) {
            messageLimitReached = context.getString(R.string.msg_full_image)
        }

        if (titleAlbumAllView.isEmpty()) {
            titleAlbumAllView = context.getString(R.string.str_all_view)
        }

        if (titleActionBar.isEmpty()) {
            titleActionBar = context.getString(R.string.album)
        }
    }

    fun setMenuTextColor() {
        if (drawableDoneButton != null
            || drawableAllDoneButton != null
            || strDoneMenu == null
            || colorTextMenu != Integer.MAX_VALUE
        )
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
}