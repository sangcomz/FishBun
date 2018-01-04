package com.sangcomz.fishbun;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.Fragment;

import com.sangcomz.fishbun.ui.album.AlbumActivity;

import java.util.ArrayList;

/**
 * Created by sangcomz on 17/05/2017.
 */

public final class FishBunCreator implements BaseProperty, CustomizationProperty {

    private FishBun fishBun;
    private Fishton fishton;

    private int requestCode = 27;


    FishBunCreator(FishBun fishBun) {
        this.fishBun = fishBun;
        this.fishton = Fishton.getInstance();
    }

    public FishBunCreator setArrayPaths(ArrayList<Uri> arrayPaths) {
        fishton.selectedImages = arrayPaths;
//        this.arrayPaths = arrayPaths;
        return this;
    }

    public FishBunCreator setAlbumThumbnailSize(int size) {
        fishton.albumThumbnailSize = size;
        return this;
    }

    @Override
    public FishBunCreator setPickerSpanCount(int spanCount) {
        if (spanCount <= 0)
            spanCount = 3;

        fishton.photoSpanCount = spanCount;
        return this;

    }

    @Deprecated
    @Override
    public FishBunCreator setPickerCount(int count) {
        if (count <= 0)
            count = 1;

        fishton.maxCount = count;

        return this;
    }

    @Override
    public FishBunCreator setMaxCount(int count) {
        if (count <= 0)
            count = 1;

        fishton.maxCount = count;
        return this;
    }

    @Override
    public FishBunCreator setMinCount(int count) {
        if (count <= 0)
            count = 1;
        fishton.minCount = count;
        return this;
    }

    @Override
    public FishBunCreator setActionBarColor(int actionbarColor) {
        fishton.colorActionBar = actionbarColor;
        return this;
    }

    @Override
    public FishBunCreator setActionBarTitleColor(int actionbarTitleColor) {
        fishton.colorActionBarTitle = actionbarTitleColor;
        return this;
    }

    @Override
    public FishBunCreator setActionBarColor(int actionbarColor, int statusBarColor) {
        fishton.colorActionBar = actionbarColor;
        fishton.colorStatusBar = statusBarColor;
        return this;
    }

    @Override
    public FishBunCreator setActionBarColor(int actionbarColor, int statusBarColor, boolean isStatusBarLight) {
        fishton.colorActionBar = actionbarColor;
        fishton.colorStatusBar = statusBarColor;
        fishton.isStatusBarLight = isStatusBarLight;
        return this;
    }

    @Override
    public FishBunCreator setCamera(boolean isCamera) {
        fishton.isCamera = isCamera;
        return this;
    }

    @Override
    public FishBunCreator setRequestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    @Override
    public FishBunCreator textOnNothingSelected(String message) {
        fishton.messageNothingSelected = message;
        return this;
    }

    @Override
    public FishBunCreator textOnImagesSelectionLimitReached(String message) {
        fishton.messageLimitReached = message;
        return this;
    }

    @Override
    public FishBunCreator setButtonInAlbumActivity(boolean isButton) {
        fishton.isButton = isButton;
        return this;
    }

    @Override
    public FishBunCreator setReachLimitAutomaticClose(boolean isAutomaticClose) {
        fishton.isAutomaticClose = isAutomaticClose;
        return this;
    }

    @Override
    public FishBunCreator setAlbumSpanCount(int portraitSpanCount, int landscapeSpanCount) {
        fishton.albumPortraitSpanCount = portraitSpanCount;
        fishton.albumLandscapeSpanCount = landscapeSpanCount;
        return this;
    }

    @Override
    public FishBunCreator setAlbumSpanCountOnlyLandscape(int landscapeSpanCount) {
        fishton.albumLandscapeSpanCount = landscapeSpanCount;
        return this;
    }

    @Override
    public FishBunCreator setAlbumSpanCountOnlPortrait(int portraitSpanCount) {
        fishton.albumPortraitSpanCount = portraitSpanCount;
        return this;
    }

    @Override
    public FishBunCreator setAllViewTitle(String allViewTitle) {
        fishton.titleAlbumAllView = allViewTitle;
        return this;
    }

    @Override
    public FishBunCreator setActionBarTitle(String actionBarTitle) {
        fishton.titleActionBar = actionBarTitle;
        return this;
    }

    @Override
    public FishBunCreator setHomeAsUpIndicatorDrawable(Drawable icon) {
        fishton.drawableHomeAsUpIndicator = icon;
//        bundle.putParcelable(CustomizationParams.DRAWABLE_HOME_AS_UP_INDICATOR.name(),
//                getBitmap(icon));
        return this;
    }

    @Override
    public FishBunCreator setOkButtonDrawable(Drawable icon) {
        fishton.drawableOkButton = icon;
        return this;
    }

    @Override
    public FishBunCreator exceptGif(boolean isExcept) {
        fishton.isExceptGif = isExcept;
        return this;
    }

    @Override
    public FishBunCreator setMenuText(String text) {
        fishton.strTextMenu = text;
        return this;
    }

    @Override
    public FishBunCreator setMenuTextColor(int textColor) {
        fishton.colorTextMenu = textColor;
        return this;
    }

    @Override
    public FishBunCreator setIsUseDetailView(boolean isUse) {
        fishton.isUseDetailView = isUse;
        return this;
    }

    public void startAlbum() {
        Context context = null;
        Activity activity = fishBun.activity.get();
        Fragment fragment = fishBun.fragment.get();
        if (activity != null)
            context = activity;
        else if (fragment != null)
            context = fragment.getActivity();
        else
            try {
                throw new Exception("Activity or Fragment Null");
            } catch (Exception e) {
                e.printStackTrace();
            }

        fishton.setDefaultMessage(context);
        fishton.setMenuTextColor();
        fishton.setDefaultDimen(context);

        Intent i = new Intent(context, AlbumActivity.class);
        if (activity != null) activity.startActivityForResult(i, requestCode);
        else if (fragment != null) fragment.startActivityForResult(i, requestCode);

    }
}