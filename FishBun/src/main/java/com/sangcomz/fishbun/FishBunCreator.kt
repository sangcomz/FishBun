package com.sangcomz.fishbun

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import com.sangcomz.fishbun.ui.album.ui.AlbumActivity
import com.sangcomz.fishbun.ui.picker.PickerActivity
import kotlin.collections.ArrayList

/**
 * Created by sangcomz on 17/05/2017.
 */
class FishBunCreator(private val fishBun: FishBun, private val fishton: Fishton) : BaseProperty,
    CustomizationProperty {
    private var requestCode = FishBun.FISHBUN_REQUEST_CODE

    override fun setSelectedImages(selectedImages: ArrayList<Uri>): FishBunCreator = this.apply {
        fishton.selectedImages.addAll(selectedImages)
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

    @Deprecated("instead setCamera(count)", ReplaceWith("hasCameraInPickerPage(mimeType)"))
    override fun setCamera(isCamera: Boolean): FishBunCreator = this.apply {
        fishton.hasCameraInPickerPage = isCamera
    }

    override fun hasCameraInPickerPage(hasCamera: Boolean): FishBunCreator = this.apply {
        fishton.hasCameraInPickerPage = hasCamera
    }

    @Deprecated("To be deleted along with the startAlbum function")
    override fun setRequestCode(requestCode: Int): FishBunCreator = this.apply {
        this.requestCode = requestCode
    }

    override fun textOnNothingSelected(message: String): FishBunCreator = this.apply {
        fishton.messageNothingSelected = message
    }

    override fun textOnImagesSelectionLimitReached(message: String): FishBunCreator = this.apply {
        fishton.messageLimitReached = message
    }

    override fun setButtonInAlbumActivity(isButton: Boolean): FishBunCreator = this.apply {
        fishton.hasButtonInAlbumActivity = isButton
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

    override fun setAllViewTitle(allViewTitle: String): FishBunCreator = this.apply {
        fishton.titleAlbumAllView = allViewTitle
    }

    override fun setActionBarTitle(actionBarTitle: String): FishBunCreator = this.apply {
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

    @Deprecated("instead setMaxCount(count)", ReplaceWith("exceptMimeType(mimeType)"))
    override fun exceptGif(isExcept: Boolean): FishBunCreator = this.apply {
        fishton.exceptMimeTypeList = arrayListOf(MimeType.GIF)
    }

    override fun exceptMimeType(exceptMimeTypeList: List<MimeType>) = this.apply {
        fishton.exceptMimeTypeList = exceptMimeTypeList
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

    override fun setSpecifyFolderList(specifyFolderList: List<String>) = this.apply {
        fishton.specifyFolderList = specifyFolderList
    }

    private fun prepareStartAlbum(context: Context) {
        checkNotNull(fishton.imageAdapter)
        exceptionHandling()
        setDefaultValue(context)
    }

    @Deprecated("instead startAlbumWithOnActivityResult(requestCode)", ReplaceWith("startAlbumWithOnActivityResult(requestCode)"))
    override fun startAlbum() {
        val fishBunContext = fishBun.fishBunContext
        val context = fishBunContext.getContext()

        prepareStartAlbum(context)

        fishBunContext.startActivityForResult(getIntent(context), requestCode)
    }

    override fun startAlbumWithOnActivityResult(requestCode: Int) {
        val fishBunContext = fishBun.fishBunContext
        val context = fishBunContext.getContext()

        prepareStartAlbum(context)

        fishBunContext.startActivityForResult(getIntent(context), requestCode)
    }

    override fun startAlbumWithActivityResultCallback(activityResultLauncher: ActivityResultLauncher<Intent>) {
        val fishBunContext = fishBun.fishBunContext
        val context = fishBunContext.getContext()

        prepareStartAlbum(context)

        fishBunContext.startWithRegisterForActivityResult(activityResultLauncher, getIntent(context))
    }

    private fun getIntent(context: Context): Intent =
        if (fishton.isStartInAllView) {
            PickerActivity.getPickerActivityIntent(context, 0L, fishton.titleAlbumAllView, 0)
        } else {
            Intent(context, AlbumActivity::class.java)
        }

    private fun setDefaultValue(context: Context) {
        with(fishton) {
            setDefaultMessage(context)
            setMenuTextColor()
            setDefaultDimen(context)
        }
    }

    private fun exceptionHandling() {
        if (fishton.hasCameraInPickerPage) {
            fishton.hasCameraInPickerPage = fishton.specifyFolderList.isEmpty()
        }
    }
}

