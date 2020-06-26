package com.sangcomz.fishbun

import android.graphics.drawable.Drawable

/**
 * Created by sangcomz on 13/05/2017.
 */
interface CustomizationProperty {
    fun setAlbumThumbnailSize(size: Int): FishBunCreator

    fun setPickerSpanCount(spanCount: Int): FishBunCreator

    fun setActionBarColor(actionbarColor: Int): FishBunCreator

    fun setActionBarTitleColor(actionbarTitleColor: Int): FishBunCreator

    fun setActionBarColor(actionbarColor: Int, statusBarColor: Int): FishBunCreator

    fun setActionBarColor(actionbarColor: Int, statusBarColor: Int, isStatusBarLight: Boolean): FishBunCreator

    fun setCamera(isCamera: Boolean): FishBunCreator

    fun textOnNothingSelected(message: String): FishBunCreator

    fun textOnImagesSelectionLimitReached(message: String): FishBunCreator

    fun setButtonInAlbumActivity(isButton: Boolean): FishBunCreator

    fun setAlbumSpanCount(portraitSpanCount: Int, landscapeSpanCount: Int): FishBunCreator

    fun setAlbumSpanCountOnlyLandscape(landscapeSpanCount: Int): FishBunCreator

    fun setAlbumSpanCountOnlPortrait(portraitSpanCount: Int): FishBunCreator

    fun setAllViewTitle(allViewTitle: String): FishBunCreator

    fun setActionBarTitle(actionBarTitle: String): FishBunCreator

    fun setHomeAsUpIndicatorDrawable(icon: Drawable?): FishBunCreator

    fun setDoneButtonDrawable(icon: Drawable?): FishBunCreator

    fun setAllDoneButtonDrawable(icon: Drawable?): FishBunCreator

    fun setIsUseAllDoneButton(isUse: Boolean): FishBunCreator

    fun setMenuDoneText(text: String?): FishBunCreator

    fun setMenuAllDoneText(text: String?): FishBunCreator

    fun setMenuTextColor(color: Int): FishBunCreator

    fun setIsUseDetailView(isUse: Boolean): FishBunCreator

    fun setIsShowCount(isShow: Boolean): FishBunCreator

    fun setSelectCircleStrokeColor(strokeColor: Int): FishBunCreator

    fun isStartInAllView(isStartInAllView: Boolean): FishBunCreator

    fun setSpecifyFolderList(specifyFolderList: List<String>): FishBunCreator

    fun hasCameraInPickerPage(isCamera: Boolean): FishBunCreator
}