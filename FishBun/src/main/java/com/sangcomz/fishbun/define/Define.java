package com.sangcomz.fishbun.define;

import android.graphics.Color;

public class Define {
    public static int ALBUM_THUMBNAIL_SIZE = -1;
    public static int ALBUM_PICKER_COUNT = 10;
    public static int PHOTO_SPAN_COUNT = 3;

    public static int ALBUM_PORTRAIT_SPAN_COUNT = 1;
    public static int ALBUM_LANDSCAPE_SPAN_COUNT = 2;

    public static boolean IS_BUTTON = false;
    public static boolean IS_AUTOMATIC_CLOSE = false;


    //    public static int ACTIONBAR_COLOR = "#3F51B5";
    public static int ACTIONBAR_COLOR = Color.parseColor("#3F51B5");
    //    public static int STATUS_BAR_COLOR = "#303F9F";
    public static int STATUS_BAR_COLOR = Color.parseColor("#303F9F");

    public static boolean IS_CAMERA = false;

    public static int ADD_PHOTO_REQUEST_CODE = 127;
    public static int TAKE_A_PICK_REQUEST_CODE = 128;
    public static int ENTER_ALBUM_REQUEST_CODE = 128;
    public final static int ALBUM_REQUEST_CODE = 27;
    public final static int PERMISSION_STORAGE = 28;
    public final static int TRANS_IMAGES_RESULT_CODE = 29;
    public final static int ADD_IMAGE_CODE = 30;

    public static String INTENT_PATH = "intent_path";
    public static String INTENT_ADD_PATH = "intent_add_path";
    public static String INTENT_POSITION = "intent_position";

    public static String MESSAGE_NOTHING_SELECTED = "";
    public static String MESSAGE_LIMIT_REACHED = "";


    public static final String SAVE_INSTANCE_ALBUM_LIST = "instance_album_list";
    public static final String SAVE_INSTANCE_ALBUM_THUMB_LIST = "instance_album_thumb_list";

    public static final String SAVE_INSTANCE_PICK_IMAGES = "instance_pick_images";
    public static final String SAVE_INSTANCE_NEW_IMAGES = "instance_new_images";
    public static final String SAVE_INSTANCE_SAVED_IMAGE = "instance_saved_image";
    public static final String SAVE_INSTANCE_SAVED_IMAGE_THUMBNAILS = "instance_saved_image_thumbnails";
}
