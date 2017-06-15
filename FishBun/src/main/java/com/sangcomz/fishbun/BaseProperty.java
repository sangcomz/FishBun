package com.sangcomz.fishbun;

import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by sangcomz on 13/05/2017.
 */

interface BaseProperty {
    FishBunCreator setArrayPaths(ArrayList<Uri> arrayPaths);

    FishBunCreator setPickerCount(int count);

    FishBunCreator setMaxCount(int count);

    FishBunCreator setMinCount(int count);

    FishBunCreator setRequestCode(int RequestCode);

    FishBunCreator setReachLimitAutomaticClose(boolean isAutomaticClose);

    FishBunCreator exceptGif(boolean isExcept);

    void startAlbum();
}
