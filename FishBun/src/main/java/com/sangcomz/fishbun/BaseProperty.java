package com.sangcomz.fishbun;

import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by sangcomz on 13/05/2017.
 */

interface BaseProperty {
    RequestCreator setArrayPaths(ArrayList<Uri> arrayPaths);

    RequestCreator setPickerCount(int count);

    RequestCreator setMaxCount(int count);

    RequestCreator setMinCount(int count);

    RequestCreator setRequestCode(int RequestCode);

    RequestCreator setReachLimitAutomaticClose(boolean isAutomaticClose);

    RequestCreator exceptGif(boolean isExcept);

    void startAlbum();
}
