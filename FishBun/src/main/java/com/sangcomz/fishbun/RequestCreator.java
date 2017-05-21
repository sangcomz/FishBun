package com.sangcomz.fishbun;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.Fragment;

import com.sangcomz.fishbun.define.Define;
import com.sangcomz.fishbun.ui.album.AlbumActivity;

import java.util.ArrayList;

/**
 * Created by sangcomz on 17/05/2017.
 */

public final class RequestCreator implements BaseProperty, CustomizationProperty {

    private FishBun fishBun;
//    private Activity activity = null;
//    private Fragment fragment = null;


    private ArrayList<Uri> arrayPaths = new ArrayList<>();

    private int requestCode = Define.ALBUM_REQUEST_CODE;

//        FishBunProperty(Activity activity) {
//            this.activity = new WeakReference<>(activity);
//        }
//
//        FishBunProperty(Fragment fragment) {
//            this.fragment = new WeakReference<>(fragment);
//        }

    RequestCreator(FishBun fishBun) {
        this.fishBun = fishBun;
    }

    public RequestCreator setArrayPaths(ArrayList<Uri> arrayPaths) {
        this.arrayPaths = arrayPaths;
        return this;
    }

    public RequestCreator setAlbumThumbnailSize(int size) {
        Define.ALBUM_THUMBNAIL_SIZE = size;
        return this;
    }

    @Override
    public RequestCreator setPickerSpanCount(int spanCount) {

        if (spanCount <= 0)
            spanCount = 3;
        Define.PHOTO_SPAN_COUNT = spanCount;
        return this;

    }

    @Deprecated
    @Override
    public RequestCreator setPickerCount(int count) {
        if (count <= 0)
            count = 1;
        Define.MAX_COUNT = count;
        return this;
    }

    @Override
    public RequestCreator setMaxCount(int count) {
        if (count <= 0)
            count = 1;
        Define.MAX_COUNT = count;
        return this;
    }

    @Override
    public RequestCreator setMinCount(int count) {
        if (count <= 0)
            count = 1;
        Define.MIN_COUNT = count;
        return this;
    }

    @Override
    public RequestCreator setActionBarColor(int actionbarColor) {
        Define.COLOR_ACTION_BAR = actionbarColor;
        return this;
    }

    @Override
    public RequestCreator setActionBarTitleColor(int actionbarTitleColor) {
        Define.COLOR_ACTION_BAR_TITLE_COLOR = actionbarTitleColor;
        return this;
    }

    @Override
    public RequestCreator setActionBarColor(int actionbarColor, int statusBarColor) {
        Define.COLOR_ACTION_BAR = actionbarColor;
        Define.COLOR_STATUS_BAR = statusBarColor;
        return this;
    }

    @Override
    public RequestCreator setActionBarColor(int actionbarColor, int statusBarColor, boolean isStatusBarLight) {
        Define.COLOR_ACTION_BAR = actionbarColor;
        Define.COLOR_STATUS_BAR = statusBarColor;
        Define.STYLE_STATUS_BAR_LIGHT = isStatusBarLight;
        return this;
    }

    @Override
    public RequestCreator setCamera(boolean isCamera) {
        Define.IS_CAMERA = isCamera;
        return this;
    }

    @Override
    public RequestCreator setRequestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    @Override
    public RequestCreator textOnNothingSelected(String message) {
        Define.MESSAGE_NOTHING_SELECTED = message;
        return this;
    }

    @Override
    public RequestCreator textOnImagesSelectionLimitReached(String message) {
        Define.MESSAGE_LIMIT_REACHED = message;
        return this;
    }

    @Override
    public RequestCreator setButtonInAlbumActivity(boolean isButton) {
        Define.IS_BUTTON = isButton;
        return this;
    }

    @Override
    public RequestCreator setReachLimitAutomaticClose(boolean isAutomaticClose) {
        Define.IS_AUTOMATIC_CLOSE = isAutomaticClose;
        return this;
    }

    @Override
    public RequestCreator setAlbumSpanCount(int portraitSpanCount, int landscapeSpanCount) {
        Define.ALBUM_PORTRAIT_SPAN_COUNT = portraitSpanCount;
        Define.ALBUM_LANDSCAPE_SPAN_COUNT = landscapeSpanCount;
        return this;
    }

    @Override
    public RequestCreator setAlbumSpanCountOnlyLandscape(int landscapeSpanCount) {
        Define.ALBUM_LANDSCAPE_SPAN_COUNT = landscapeSpanCount;
        return this;
    }

    @Override
    public RequestCreator setAlbumSpanCountOnlPortrait(int portraitSpanCount) {
        Define.ALBUM_PORTRAIT_SPAN_COUNT = portraitSpanCount;
        return this;
    }

    @Override
    public RequestCreator setAllViewTitle(String allViewTitle) {
        Define.TITLE_ALBUM_ALL_VIEW = allViewTitle;
        return this;
    }

    @Override
    public RequestCreator setActionBarTitle(String actionBarTitle) {
        Define.TITLE_ACTIONBAR = actionBarTitle;
        return this;
    }

    @Override
    public RequestCreator setHomeAsUpIndicatorDrawable(Drawable icon) {
        Define.homeAsUpIndicatorDrawable = icon;
        return this;
    }

    @Override
    public RequestCreator setOkButtonDrawable(Drawable icon) {
        Define.okButtonDrawable = icon;
        return this;
    }

    @Override
    public RequestCreator exceptGif(boolean isExcept) {
        Define.EXCEPT_GIF = isExcept;
        return this;
    }

    @Override
    public RequestCreator setMenuText(String text) {
        Define.TEXT_MENU = text;
        return this;
    }

    @Override
    public RequestCreator setMenuTextColor(int textColor) {
        Define.COLOR_MENU_TEXT = textColor;
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

        if (Define.ALBUM_THUMBNAIL_SIZE == -1)
            Define.ALBUM_THUMBNAIL_SIZE = (int) context.getResources().getDimension(R.dimen.album_thum_size);

        setDefaultMessage(context);
        setMenuTextColor();

        Intent i = new Intent(context, AlbumActivity.class);
        i.putParcelableArrayListExtra(Define.INTENT_PATH, arrayPaths);

        if (activity != null)
            activity.startActivityForResult(i, requestCode);

        else if (fragment != null)
            fragment.startActivityForResult(i, requestCode);

    }

    private void setDefaultMessage(Context context) {
        if (Define.MESSAGE_NOTHING_SELECTED.equals(""))
            Define.MESSAGE_NOTHING_SELECTED = context.getResources().getString(R.string.msg_no_selected);

        if (Define.MESSAGE_LIMIT_REACHED.equals(""))
            Define.MESSAGE_LIMIT_REACHED = context.getResources().getString(R.string.msg_full_image);

        if (Define.TITLE_ALBUM_ALL_VIEW.equals(""))
            Define.TITLE_ALBUM_ALL_VIEW = context.getResources().getString(R.string.str_all_view);

        if (Define.TITLE_ACTIONBAR.equals(""))
            Define.TITLE_ACTIONBAR = context.getResources().getString(R.string.album);
    }

    private void setMenuTextColor() {
        if (Define.okButtonDrawable != null
                || Define.TEXT_MENU == null
                || Define.COLOR_MENU_TEXT != -1) return;
        if (Define.STYLE_STATUS_BAR_LIGHT)
            Define.COLOR_MENU_TEXT = Color.BLACK;
        else
            Define.COLOR_MENU_TEXT = Color.WHITE;

    }
}