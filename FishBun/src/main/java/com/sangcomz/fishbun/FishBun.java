package com.sangcomz.fishbun;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;

import com.sangcomz.fishbun.define.Define;
import com.sangcomz.fishbun.ui.album.AlbumActivity;

import java.util.ArrayList;


public class FishBun {

    private static BaseProperty baseProperty;

    //    BaseProperty baseProperty;
    public static BaseProperty with(Activity activity) {
        return baseProperty = new BaseProperty(activity);
    }

    public static BaseProperty with(Fragment fragment) {
        return baseProperty = new BaseProperty(fragment);
    }

    public static class BaseProperty implements BasePropertyImpl {

        private ArrayList<String> arrayPaths = new ArrayList<>();
        private Activity activity = null;
        private Fragment fragment = null;
        private int requestCode = Define.ALBUM_REQUEST_CODE;


        public BaseProperty(Activity activity) {
            this.activity = activity;
        }

        public BaseProperty(Fragment fragment) {
            this.fragment = fragment;
        }

        public BaseProperty setArrayPaths(ArrayList<String> arrayPaths) {
            this.arrayPaths = arrayPaths;
            return baseProperty;
        }

        public BaseProperty setAlbumThumnaliSize(int size) {
            Define.ALBUM_THUMNAIL_SIZE = size;
            return baseProperty;
        }

        @Override
        public BaseProperty setPickerSpanCount(int spanCount) {

            if (spanCount <= 0)
                spanCount = 3;
            Define.PHOTO_SPAN_COUNT = spanCount;
            return baseProperty;

        }

        public BaseProperty setPickerCount(int count) {
            if (count <= 0)
                count = 1;
            Define.ALBUM_PICKER_COUNT = count;
            return baseProperty;
        }

        @Override
        public BaseProperty setActionBarColor(int actionbarColor) {
            Define.ACTIONBAR_COLOR = actionbarColor;
            return baseProperty;
        }

        @Override
        public BaseProperty setActionBarColor(int actionbarColor, int statusbarColor) {
            Define.ACTIONBAR_COLOR = actionbarColor;
            Define.STATUSBAR_COLOR = statusbarColor;
            return baseProperty;
        }

        @Override
        public BaseProperty setCamera(boolean isCamera) {
            Define.IS_CAMERA = isCamera;
            return baseProperty;
        }

        @Override
        public BaseProperty setRequestCode(int requestCode) {
            this.requestCode = requestCode;
            return baseProperty;
        }

        @Override
        public BaseProperty textOnNothingSelected(String message) {
            Define.MESSAGE_NOTHING_SELECTED = message;
            return baseProperty;
        }

        @Override
        public BaseProperty textOnImagesSelectionLimitReached(String message) {
            Define.MESSAGE_LIMIT_REACHED = message;
            return baseProperty;
        }

        @Override
        public BaseProperty setButtonInAlbumActiviy(boolean isButton) {
            Define.IS_BUTTON = isButton;
            return baseProperty;
        }

        public void startAlbum() {
            Context context = null;
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

            DisplayMetrics dm;
            dm = context.getResources().getDisplayMetrics();
            if (dm != null) {
                Define.PHOTO_PICKER_SIZE = dm.widthPixels / Define.PHOTO_SPAN_COUNT;
            }

            if (Define.ALBUM_THUMNAIL_SIZE == -1)
                Define.ALBUM_THUMNAIL_SIZE = (int) context.getResources().getDimension(R.dimen.album_thum_size);

            setMessage(context);

            Intent i = new Intent(context, AlbumActivity.class);
            i.putStringArrayListExtra(Define.INTENT_PATH, arrayPaths);

            if (activity != null)
                activity.startActivityForResult(i, requestCode);

            else if (fragment != null)
                fragment.startActivityForResult(i, requestCode);


        }


    }

    interface BasePropertyImpl {

        BaseProperty setArrayPaths(ArrayList<String> arrayPaths);

        BaseProperty setAlbumThumnaliSize(int size);

        BaseProperty setPickerSpanCount(int spanCount);

        BaseProperty setPickerCount(int count);

        BaseProperty setActionBarColor(int actionbarColor);

        BaseProperty setActionBarColor(int actionbarColor, int statusbarColor);

        BaseProperty setCamera(boolean isCamera);

        BaseProperty setRequestCode(int RequestCode);

        BaseProperty textOnNothingSelected(String message);

        BaseProperty textOnImagesSelectionLimitReached(String message);

        BaseProperty setButtonInAlbumActiviy(boolean isButton);

        void startAlbum();
    }


    private static void setMessage(Context context) {
        if (Define.MESSAGE_NOTHING_SELECTED.equals(""))
            Define.MESSAGE_NOTHING_SELECTED = context.getResources().getString(R.string.msg_no_slected);

        if (Define.MESSAGE_LIMIT_REACHED.equals(""))
            Define.MESSAGE_LIMIT_REACHED = context.getResources().getString(R.string.msg_full_image);
    }

}
