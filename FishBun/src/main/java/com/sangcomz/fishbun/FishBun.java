package com.sangcomz.fishbun;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.sangcomz.fishbun.define.Define;
import com.sangcomz.fishbun.ui.album.AlbumActivity;

import java.util.ArrayList;

import kr.co.sangcomz.albummodule.R;

public class FishBun {
    private static BaseProperty baseProperty;

    //    BaseProperty baseProperty;
    public static BaseProperty with(Context context) {
        return baseProperty = new BaseProperty(context);
    }

    public static class BaseProperty implements BasePropertyImpl {

        private ArrayList<String> arrayPaths = new ArrayList<>();
        private Context context;

        public BaseProperty(Context context) {
            this.context = context;
        }

        public BaseProperty setArrayPaths(ArrayList<String> arrayPaths) {
            this.arrayPaths = arrayPaths;
            return baseProperty;
        }

        public BaseProperty setAlbumThumnaliSize(int size) {
            Define.ALBUM_THUMNAIL_SIZE = size;
            return baseProperty;
        }

        public BaseProperty setPickerCount(int count) {
            Define.ALBUM_PICKER_COUNT = count;
            return baseProperty;
        }

        public void startAlbum() {
            if (Define.ALBUM_THUMNAIL_SIZE == -1)
                Define.ALBUM_THUMNAIL_SIZE = (int) context.getResources().getDimension(R.dimen.album_thum_size);

            Intent i = new Intent(context, AlbumActivity.class);
            i.putStringArrayListExtra(Define.INTENT_PATH, arrayPaths);
            ((Activity) context).startActivityForResult(i, Define.ALBUM_REQUEST_CODE);
        }
    }

    interface BasePropertyImpl {

        BaseProperty setArrayPaths(ArrayList<String> arrayPaths);

        BaseProperty setAlbumThumnaliSize(int size);

        BaseProperty setPickerCount(int count);

        void startAlbum();
    }

}
