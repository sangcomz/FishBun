package com.trams.blindring;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;


import com.trams.blindring.define.Define;
import com.trams.blindring.ui.album.AlbumActivity;

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
        @Override
        public BaseProperty setActionBarText(String text) {

            if(text==null || text.equals(""))text="앨범";

            Define.ACTIONBAR_TEXT = text;

            return baseProperty;
        }
        @Override
        public BaseProperty setImagePadding(int top, int bottom, int left, int right) {
            Define.IMAGE_PADDDING_TOP = top;
            Define.IMAGE_PADDDING_LEFT = left;
            Define.IMAGE_PADDDING_RIGHT = right;
            Define.IMAGE_PADDDING_BOTTOM = bottom;

            return baseProperty;
        }
        @Override
        public BaseProperty setAlbumThumnaliSize(int width,int height) {
            Define.ALBUM_THUMNAIL_WIDTH = width;
            Define.ALBUM_THUMNAIL_HEIGHT=height;
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

            if (Define.ALBUM_THUMNAIL_WIDTH == -1)
                Define.ALBUM_THUMNAIL_WIDTH = (int) context.getResources().getDimension(R.dimen.album_thum_size);

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
        BaseProperty setImagePadding(int top, int bottom, int left, int right);

        BaseProperty setActionBarText(String text);

        BaseProperty setArrayPaths(ArrayList<String> arrayPaths);

        BaseProperty setAlbumThumnaliSize(int size,int height);

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
