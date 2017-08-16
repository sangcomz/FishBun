package com.sangcomz.fishbun;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.sangcomz.fishbun.ui.album.AlbumActivity;

import java.util.ArrayList;

/**
 * Created by sangcomz on 17/05/2017.
 */

public final class FishBunCreator implements BaseProperty, CustomizationProperty {

    private FishBun fishBun;
    private ArrayList<Uri> arrayPaths = new ArrayList<>();
    private Bundle bundle;

    private int requestCode = 27;


    FishBunCreator(FishBun fishBun, Bundle bundle) {
        this.bundle = bundle;
        this.fishBun = fishBun;
    }

    public FishBunCreator setArrayPaths(ArrayList<Uri> arrayPaths) {
        this.arrayPaths = arrayPaths;
        return this;
    }

    public FishBunCreator setAlbumThumbnailSize(int size) {
        bundle.putInt(CustomizationParams.INT_ALBUM_THUMBNAIL_SIZE.name(), size);
        return this;
    }

    @Override
    public FishBunCreator setPickerSpanCount(int spanCount) {
        if (spanCount <= 0)
            spanCount = 3;
        bundle.putInt(CustomizationParams.INT_PHOTO_SPAN_COUNT.name(), spanCount);
        return this;

    }

    @Deprecated
    @Override
    public FishBunCreator setPickerCount(int count) {
        if (count <= 0)
            count = 1;
        bundle.putInt(BaseParams.INT_MAX_COUNT.name(), count);

        return this;
    }

    @Override
    public FishBunCreator setMaxCount(int count) {
        if (count <= 0)
            count = 1;

        bundle.putInt(BaseParams.INT_MAX_COUNT.name(), count);
        return this;
    }

    @Override
    public FishBunCreator setMinCount(int count) {
        if (count <= 0)
            count = 1;
        bundle.putInt(BaseParams.INT_MIN_COUNT.name(), count);
        return this;
    }

    @Override
    public FishBunCreator setActionBarColor(int actionbarColor) {
        bundle.putInt(CustomizationParams.INT_COLOR_ACTION_BAR.name(), actionbarColor);
        return this;
    }

    @Override
    public FishBunCreator setActionBarTitleColor(int actionbarTitleColor) {
        bundle.putInt(CustomizationParams.INT_COLOR_ACTION_BAR_TITLE_COLOR.name(), actionbarTitleColor);
        return this;
    }

    @Override
    public FishBunCreator setActionBarColor(int actionbarColor, int statusBarColor) {
        bundle.putInt(CustomizationParams.INT_COLOR_ACTION_BAR.name(), actionbarColor);
        bundle.putInt(CustomizationParams.INT_COLOR_STATUS_BAR.name(), statusBarColor);
        return this;
    }

    @Override
    public FishBunCreator setActionBarColor(int actionbarColor, int statusBarColor, boolean isStatusBarLight) {
        bundle.putInt(CustomizationParams.INT_COLOR_ACTION_BAR.name(), actionbarColor);
        bundle.putInt(CustomizationParams.INT_COLOR_STATUS_BAR.name(), statusBarColor);
        bundle.putBoolean(CustomizationParams.BOOLEAN_STYLE_STATUS_BAR_LIGHT.name(), isStatusBarLight);
        return this;
    }

    @Override
    public FishBunCreator setCamera(boolean isCamera) {
        bundle.putBoolean(CustomizationParams.BOOLEAN_IS_CAMERA.name(), isCamera);
        return this;
    }

    @Override
    public FishBunCreator setRequestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    @Override
    public FishBunCreator textOnNothingSelected(String message) {
        bundle.putString(CustomizationParams.STRING_MESSAGE_NOTHING_SELECTED.name(), message);
        return this;
    }

    @Override
    public FishBunCreator textOnImagesSelectionLimitReached(String message) {
        bundle.putString(CustomizationParams.STRING_MESSAGE_LIMIT_REACHED.name(), message);
        return this;
    }

    @Override
    public FishBunCreator setButtonInAlbumActivity(boolean isButton) {
        bundle.putBoolean(CustomizationParams.BOOLEAN_IS_BUTTON.name(), isButton);
        return this;
    }

    @Override
    public FishBunCreator setReachLimitAutomaticClose(boolean isAutomaticClose) {
        bundle.putBoolean(CustomizationParams.BOOLEAN_IS_AUTOMATIC_CLOSE.name(), isAutomaticClose);
        return this;
    }

    @Override
    public FishBunCreator setAlbumSpanCount(int portraitSpanCount, int landscapeSpanCount) {
        bundle.putInt(CustomizationParams.INT_ALBUM_PORTRAIT_SPAN_COUNT.name(), portraitSpanCount);
        bundle.putInt(CustomizationParams.INT_ALBUM_LANDSCAPE_SPAN_COUNT.name(), landscapeSpanCount);
        return this;
    }

    @Override
    public FishBunCreator setAlbumSpanCountOnlyLandscape(int landscapeSpanCount) {
        bundle.putInt(CustomizationParams.INT_ALBUM_LANDSCAPE_SPAN_COUNT.name(), landscapeSpanCount);
        return this;
    }

    @Override
    public FishBunCreator setAlbumSpanCountOnlPortrait(int portraitSpanCount) {
        bundle.putInt(CustomizationParams.INT_ALBUM_PORTRAIT_SPAN_COUNT.name(), portraitSpanCount);
        return this;
    }

    @Override
    public FishBunCreator setAllViewTitle(String allViewTitle) {
        bundle.putString(CustomizationParams.STRING_TITLE_ALBUM_ALL_VIEW.name(), allViewTitle);
        return this;
    }

    @Override
    public FishBunCreator setActionBarTitle(String actionBarTitle) {
        bundle.putString(CustomizationParams.STRING_TITLE_ACTIONBAR.name(), actionBarTitle);
        return this;
    }

    @Override
    public FishBunCreator setHomeAsUpIndicatorDrawable(Drawable icon) {
        bundle.putParcelable(CustomizationParams.DRAWABLE_HOME_AS_UP_INDICATOR.name(),
                getBitmap(icon));
        return this;
    }

    @Override
    public FishBunCreator setOkButtonDrawable(Drawable icon) {
        bundle.putParcelable(CustomizationParams.DRAWABLE_OK_BUTTON_DRAWABLE.name(),
                getBitmap(icon));
        return this;
    }

    @Override
    public FishBunCreator exceptGif(boolean isExcept) {
        bundle.putBoolean(BaseParams.BOOLEAN_EXCEPT_GIF.name(), isExcept);
        return this;
    }

    @Override
    public FishBunCreator setMenuText(String text) {
        bundle.putString(CustomizationParams.STRING_TEXT_MENU.name(), text);
        return this;
    }

    @Override
    public FishBunCreator setMenuTextColor(int textColor) {
        bundle.putInt(CustomizationParams.INT_COLOR_MENU_TEXT.name(), textColor);
        return this;
    }

    @Override
    public FishBunCreator setIsUseDetailView(boolean isUse) {
        bundle.putBoolean(CustomizationParams.BOOLEAN_IS_USE_DETAIL_VIEW.name(), isUse);
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

        setDefaultMessage(context);

        setMenuTextColor();

        Intent i = new Intent(context, AlbumActivity.class);
        i.putParcelableArrayListExtra(BaseParams.ARRAY_PATHS.name(), arrayPaths);
        i.putExtras(bundle);
        if (activity != null) activity.startActivityForResult(i, requestCode);
        else if (fragment != null) fragment.startActivityForResult(i, requestCode);

    }

    private void setDefaultMessage(Context context) {
        if (bundle.getString(CustomizationParams.STRING_MESSAGE_NOTHING_SELECTED.name()) == null) {
            bundle.putString(CustomizationParams.STRING_MESSAGE_NOTHING_SELECTED.name(),
                    context.getResources().getString(R.string.msg_no_selected));
        }

        if (bundle.getString(CustomizationParams.STRING_MESSAGE_LIMIT_REACHED.name()) == null)
            bundle.putString(CustomizationParams.STRING_MESSAGE_LIMIT_REACHED.name(),
                    context.getResources().getString(R.string.msg_full_image));

        if (bundle.getString(CustomizationParams.STRING_TITLE_ALBUM_ALL_VIEW.name()) == null)
            bundle.putString(CustomizationParams.STRING_TITLE_ALBUM_ALL_VIEW.name(),
                    context.getResources().getString(R.string.str_all_view));

        if (bundle.getString(CustomizationParams.STRING_TITLE_ACTIONBAR.name()) == null)
            bundle.putString(CustomizationParams.STRING_TITLE_ACTIONBAR.name(),
                    context.getResources().getString(R.string.album));


    }

    private void setMenuTextColor() {
        if (bundle.getParcelable(CustomizationParams.DRAWABLE_OK_BUTTON_DRAWABLE.name()) != null
                || bundle.getString(CustomizationParams.STRING_TEXT_MENU.name()) == null
                || bundle.getInt(CustomizationParams.INT_COLOR_MENU_TEXT.name(), Integer.MAX_VALUE) != Integer.MAX_VALUE)
            return;

        if (bundle.getBoolean(CustomizationParams.BOOLEAN_STYLE_STATUS_BAR_LIGHT.name(), false))
            bundle.putInt(CustomizationParams.INT_COLOR_MENU_TEXT.name(), Color.BLACK);
        else
            bundle.putInt(CustomizationParams.INT_COLOR_MENU_TEXT.name(), Color.WHITE);
    }

    private Bitmap getBitmap(Drawable drawable) {
        try {
            Bitmap bitmap;

            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            // Handle the error
            return null;
        }
    }
}