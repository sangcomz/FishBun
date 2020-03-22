package com.sangcomz.fishbun

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import com.sangcomz.fishbun.bean.Album
import com.sangcomz.fishbun.define.Define
import com.sangcomz.fishbun.ui.album.AlbumActivity
import com.sangcomz.fishbun.ui.picker.PickerActivity
import java.util.*

/**
 * Created by sangcomz on 17/05/2017.
 */
class FishBunCreator(private val fishBun: FishBun, private val fishton: Fishton) : BaseProperty,
    CustomizationProperty {
    private var requestCode = 27

    override fun setSelectedImages(selectedImages: ArrayList<Uri>): FishBunCreator = this.apply {
        fishton.selectedImages = selectedImages
    }

    override fun setAlbumThumbnailSize(size: Int): FishBunCreator = apply {
        fishton.albumThumbnailSize = size
    }

    override fun setPickerSpanCount(spanCount: Int): FishBunCreator = this.apply {
        fishton.photoSpanCount = if (spanCount <= 0) 3 else spanCount
    }

    @Deprecated("instead setMaxCount(count)", ReplaceWith("setMaxCount(count)"))
    override fun setPickerCount(count: Int): FishBunCreator = this.apply {
        setMaxCount(count)
    }

    override fun setMaxCount(count: Int): FishBunCreator = this.apply {
        fishton.maxCount = if (count <= 0) 1 else count
    }

    override fun setMinCount(count: Int): FishBunCreator = this.apply {
        fishton.minCount = if (count <= 0) 1 else count
    }

    override fun setActionBarTitleColor(actionbarTitleColor: Int): FishBunCreator = this.apply {
        fishton.colorActionBarTitle = actionbarTitleColor
    }

    override fun setActionBarColor(actionbarColor: Int): FishBunCreator = this.apply {
        fishton.colorActionBar = actionbarColor
    }

    override fun setActionBarColor(actionbarColor: Int, statusBarColor: Int): FishBunCreator =
        this.apply {
            fishton.colorActionBar = actionbarColor
            fishton.colorStatusBar = statusBarColor
        }

    override fun setActionBarColor(
        actionbarColor: Int,
        statusBarColor: Int,
        isStatusBarLight: Boolean
    ): FishBunCreator = this.apply {
        fishton.colorActionBar = actionbarColor
        fishton.colorStatusBar = statusBarColor
        fishton.isStatusBarLight = isStatusBarLight
    }

    override fun setCamera(isCamera: Boolean): FishBunCreator = this.apply {
        fishton.isCamera = isCamera
    }

    override fun setRequestCode(requestCode: Int): FishBunCreator = this.apply {
        this.requestCode = requestCode
    }

    override fun textOnNothingSelected(message: String?): FishBunCreator = this.apply {
        fishton.messageNothingSelected = message
    }

    override fun textOnImagesSelectionLimitReached(message: String?): FishBunCreator = this.apply {
        fishton.messageLimitReached = message
    }

    override fun setButtonInAlbumActivity(isButton: Boolean): FishBunCreator = this.apply {
        fishton.isButton = isButton
    }

    override fun setReachLimitAutomaticClose(isAutomaticClose: Boolean): FishBunCreator =
        this.apply {
            fishton.isAutomaticClose = isAutomaticClose
        }

    override fun setAlbumSpanCount(
        portraitSpanCount: Int,
        landscapeSpanCount: Int
    ): FishBunCreator = this.apply {
        fishton.albumPortraitSpanCount = portraitSpanCount
        fishton.albumLandscapeSpanCount = landscapeSpanCount
    }

    override fun setAlbumSpanCountOnlyLandscape(landscapeSpanCount: Int): FishBunCreator =
        this.apply {
            fishton.albumLandscapeSpanCount = landscapeSpanCount
        }

    override fun setAlbumSpanCountOnlPortrait(portraitSpanCount: Int): FishBunCreator = this.apply {
        fishton.albumPortraitSpanCount = portraitSpanCount
    }

    override fun setAllViewTitle(allViewTitle: String?): FishBunCreator = this.apply {
        fishton.titleAlbumAllView = allViewTitle
    }

    override fun setActionBarTitle(actionBarTitle: String?): FishBunCreator = this.apply {
        fishton.titleActionBar = actionBarTitle
    }

    override fun setHomeAsUpIndicatorDrawable(icon: Drawable?): FishBunCreator = this.apply {
        fishton.drawableHomeAsUpIndicator = icon
    }

    override fun setDoneButtonDrawable(icon: Drawable?): FishBunCreator = this.apply {
        fishton.drawableDoneButton = icon
    }

    override fun setAllDoneButtonDrawable(icon: Drawable?): FishBunCreator = this.apply {
        fishton.drawableAllDoneButton = icon
    }

    override fun setIsUseAllDoneButton(isUse: Boolean): FishBunCreator = this.apply {
        fishton.isUseAllDoneButton = isUse
    }

    override fun exceptGif(isExcept: Boolean): FishBunCreator = this.apply {
        fishton.isExceptGif = isExcept
    }

    override fun setMenuDoneText(text: String?): FishBunCreator = this.apply {
        fishton.strDoneMenu = text
    }

    override fun setMenuAllDoneText(text: String?): FishBunCreator = this.apply {
        fishton.strAllDoneMenu = text
    }

    override fun setMenuTextColor(color: Int): FishBunCreator = this.apply {
        fishton.colorTextMenu = color
    }

    override fun setIsUseDetailView(isUse: Boolean): FishBunCreator = this.apply {
        fishton.isUseDetailView = isUse
    }

    override fun setIsShowCount(isShow: Boolean): FishBunCreator = this.apply {
        fishton.isShowCount = isShow
    }

    override fun setSelectCircleStrokeColor(strokeColor: Int): FishBunCreator = this.apply {
        fishton.colorSelectCircleStroke = strokeColor
    }

    override fun isStartInAllView(isStartInAllView: Boolean): FishBunCreator = this.apply {
        fishton.isStartInAllView = isStartInAllView
    }


    override fun startAlbum() {
        val fishBunContext = fishBun.fishBunContext
        val context = fishBunContext.getContext()

        if (fishton.imageAdapter == null) throw NullPointerException("ImageAdapter is Null")

        with(fishton) {
            setDefaultMessage(context)
            setMenuTextColor()
            setDefaultDimen(context)
        }

        val intent: Intent =
            if (fishton.isStartInAllView) {
                Intent(context, PickerActivity::class.java).apply {
                    putExtra(
                        Define.BUNDLE_NAME.ALBUM.name,
                        Album(0, fishton.titleAlbumAllView, null, 0)
                    )
                    putExtra(Define.BUNDLE_NAME.POSITION.name, 0)
                }
            } else {
                Intent(context, AlbumActivity::class.java)
            }

        fishBunContext.startActivityForResult(intent, requestCode)
    }
}

