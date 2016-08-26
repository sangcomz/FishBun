package com.sangcomz.fishbun.ui.album;

import android.os.Build;

import com.sangcomz.fishbun.define.Define;
import com.sangcomz.fishbun.permission.PermissionCheck;

public class AlbumController {

    private AlbumActivity albumActivity;

    AlbumController(AlbumActivity albumActivity) {
        this.albumActivity = albumActivity;
    }

    protected boolean checkPermission() {
        PermissionCheck permissionCheck = new PermissionCheck(albumActivity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionCheck.CheckStoragePermission())
                return true;
        } else
            return true;
        return false;
    }

    public void setSpanCount(int albumListSize) {
        if (Define.ALBUM_LANDSCAPE_SPAN_COUNT > albumListSize)
            Define.ALBUM_LANDSCAPE_SPAN_COUNT = albumListSize;
        if (Define.ALBUM_POTRAIT_SPAN_COUNT > albumListSize)
            Define.ALBUM_POTRAIT_SPAN_COUNT = albumListSize;
    }
}
