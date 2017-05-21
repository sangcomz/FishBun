package com.sangcomz.fishbun;

import android.graphics.drawable.Drawable;

/**
 * Created by sangcomz on 13/05/2017.
 */

interface CustomizationProperty {
    RequestCreator setAlbumThumbnailSize(int size);

    RequestCreator setPickerSpanCount(int spanCount);

    RequestCreator setActionBarColor(int actionbarColor);

    RequestCreator setActionBarTitleColor(int actionbarTitleColor);

    RequestCreator setActionBarColor(int actionbarColor, int statusBarColor);

    RequestCreator setActionBarColor(int actionbarColor, int statusBarColor, boolean isStatusBarLight);

    RequestCreator setCamera(boolean isCamera);

    RequestCreator textOnNothingSelected(String message);

    RequestCreator textOnImagesSelectionLimitReached(String message);

    RequestCreator setButtonInAlbumActivity(boolean isButton);

    RequestCreator setAlbumSpanCount(int portraitSpanCount, int landscapeSpanCount);

    RequestCreator setAlbumSpanCountOnlyLandscape(int landscapeSpanCount);

    RequestCreator setAlbumSpanCountOnlPortrait(int portraitSpanCount);

    RequestCreator setAllViewTitle(String allViewTitle);

    RequestCreator setActionBarTitle(String actionBarTitle);

    RequestCreator setHomeAsUpIndicatorDrawable(Drawable icon);

    RequestCreator setOkButtonDrawable(Drawable icon);

    RequestCreator setMenuText(String text);

    RequestCreator setMenuTextColor(int color);
}
