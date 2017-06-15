package com.sangcomz.fishbun.define;

public class Define {

    public static final int ALBUM_REQUEST_CODE = 27;
    public final int PERMISSION_STORAGE = 28;
    public final int TRANS_IMAGES_RESULT_CODE = 29;
    public final int TAKE_A_PICK_REQUEST_CODE = 128;
    public final int ENTER_ALBUM_REQUEST_CODE = 129;
    public final int ENTER_DETAIL_REQUEST_CODE = 130;

    public final int DEFAULT_MAX_COUNT = 10;
    public final int DEFAULT_MIN_COUNT = 1;

    public final static String INTENT_PATH = "intent_path";
    public final String INTENT_ADD_PATH = "intent_add_path";
    public final String INTENT_POSITION = "intent_position";

    public final String SAVE_INSTANCE_ALBUM_LIST = "instance_album_list";
    public final String SAVE_INSTANCE_ALBUM_THUMB_LIST = "instance_album_thumb_list";

    public final String SAVE_INSTANCE_PICK_IMAGES = "instance_pick_images";
    public final String SAVE_INSTANCE_NEW_IMAGES = "instance_new_images";
    public final String SAVE_INSTANCE_SAVED_IMAGE = "instance_saved_image";
    public final String SAVE_INSTANCE_SAVED_IMAGE_THUMBNAILS = "instance_saved_image_thumbnails";

    public enum BUNDLE_NAME {
        POSITION,
        PICKED_IMAGES,
        IMAGES,
        ALBUM,
        BUNDLE
    }
}
