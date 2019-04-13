package com.sangcomz.fishbun;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.sangcomz.fishbun.adapter.image.ImageAdapter;

import java.util.ArrayList;

/**
 * Created by seokwon.jeong on 04/01/2018.
 */

public class Fishton {
    public ImageAdapter imageAdapter;
    public Uri[] pickerImages;

    //BaseParams
    public int maxCount;
    public int minCount;
    public boolean isExceptGif;
    public ArrayList<Uri> selectedImages = new ArrayList<>();


    //CustomizationParams
    public int photoSpanCount;
    public int albumPortraitSpanCount;
    public int albumLandscapeSpanCount;

    public boolean isAutomaticClose;
    public boolean isButton;

    public int colorActionBar;
    public int colorActionBarTitle;
    public int colorStatusBar;

    public boolean isStatusBarLight;
    public boolean isCamera;

    public int albumThumbnailSize;

    public String messageNothingSelected;
    public String messageLimitReached;
    public String titleAlbumAllView;
    public String titleActionBar;

    public Drawable drawableHomeAsUpIndicator;
    public Drawable drawableOkButton;

    public String strTextMenu;

    public int colorTextMenu;

    public boolean isUseDetailView;

    public boolean isShowCount;

    public int colorSelectCircleStroke;


    private Fishton() {
        init();
    }

    public static Fishton getInstance() {
        return FishtonHolder.INSTANCE;
    }

    private static class FishtonHolder {
        public static final Fishton INSTANCE = new Fishton();
    }

    public void refresh() {
        init();
    }

    private void init() {
        //Adapter
        imageAdapter = null;

        //BaseParams
        maxCount = 10;
        minCount = 1;
        isExceptGif = true;
        selectedImages = new ArrayList<>();

        //CustomizationParams
        photoSpanCount = 3;
        albumPortraitSpanCount = 1;
        albumLandscapeSpanCount = 2;

        isAutomaticClose = false;
        isButton = false;

        colorActionBar = Color.parseColor("#3F51B5");
        colorActionBarTitle = Color.parseColor("#ffffff");
        colorStatusBar = Color.parseColor("#303F9F");

        isStatusBarLight = false;
        isCamera = false;

        albumThumbnailSize = Integer.MAX_VALUE;

        messageNothingSelected = null;
        messageLimitReached = null;
        titleAlbumAllView = null;
        titleActionBar = null;

        drawableHomeAsUpIndicator = null;
        drawableOkButton = null;

        strTextMenu = null;


        colorTextMenu = Integer.MAX_VALUE;

        isUseDetailView = true;
        isShowCount = true;

        colorSelectCircleStroke = Color.parseColor("#c1ffffff");
    }

    void setDefaultMessage(Context context) {
        if (messageNothingSelected == null)
            messageNothingSelected = context.getResources().getString(R.string.msg_no_selected);

        if (messageLimitReached == null)
            messageLimitReached = context.getResources().getString(R.string.msg_full_image);

        if (titleAlbumAllView == null)
            titleAlbumAllView = context.getResources().getString(R.string.str_all_view);

        if (titleActionBar == null)
            titleActionBar = context.getResources().getString(R.string.album);
    }

    void setMenuTextColor() {
        if (drawableOkButton != null
                || strTextMenu == null
                || colorTextMenu != Integer.MAX_VALUE)
            return;

        if (isStatusBarLight)
            colorTextMenu = Color.BLACK;
        else
            colorTextMenu = Color.WHITE;
    }

    void setDefaultDimen(Context context) {
        if (albumThumbnailSize == Integer.MAX_VALUE)
            albumThumbnailSize = (int) context.getResources().getDimension(R.dimen.album_thum_size);
    }
}
