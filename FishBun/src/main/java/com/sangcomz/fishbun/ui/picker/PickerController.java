package com.sangcomz.fishbun.ui.picker;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;

import com.sangcomz.fishbun.define.Define;
import com.sangcomz.fishbun.permission.PermissionCheck;
import com.sangcomz.fishbun.util.CameraUtil;
import com.sangcomz.fishbun.util.RegexUtil;

import java.util.ArrayList;

/**
 * Created by sangc on 2015-11-05.
 */
public class PickerController {
    private PickerActivity pickerActivity;
    private RecyclerView recyclerView;
    private RecyclerView.OnItemTouchListener OnItemTouchListener;
    private ArrayList<Uri> addImagePaths = new ArrayList<>();
    private String savePath;
    private ContentResolver resolver;
    private CameraUtil cameraUtil = new CameraUtil();
    private String pathDir = "";
    private ArrayList<Uri> pickedImages;


    PickerController(PickerActivity pickerActivity, RecyclerView recyclerView) {
        this.pickerActivity = pickerActivity;
        this.recyclerView = recyclerView;

        resolver = pickerActivity.getContentResolver();

        OnItemTouchListener = new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return true;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        };
    }

    public void setPickedImages(ArrayList<Uri> pickedImages) {
        this.pickedImages = pickedImages;
    }

    public ArrayList<Uri> getPickedImages() {
        return pickedImages;
    }

    public void takePicture(Activity activity, String saveDir) {
        cameraUtil.takePicture(activity, saveDir);
    }


    public void setToolbarTitle(int total) {
        pickerActivity.showToolbarTitle(total);
    }

    String getSavePath() {
        return cameraUtil.getSavePath();
    }

    void setSavePath(String savePath) {
        cameraUtil.setSavePath(savePath);
    }

    public void setAddImagePath(Uri imagePath) {
        this.addImagePaths.add(imagePath);
    }

    protected ArrayList<Uri> getAddImagePaths() {
        return addImagePaths;
    }

    public void setAddImagePaths(ArrayList<Uri> addImagePaths) {
        this.addImagePaths = addImagePaths;
    }

    public void finishActivity() {
        ArrayList<Uri> path = new ArrayList<>();
        for (int i = 0; i < pickedImages.size(); i++) {
            path.add(pickedImages.get(i));
        }
        Intent i = new Intent();
        i.putParcelableArrayListExtra(Define.INTENT_PATH, path);
        pickerActivity.setResult(pickerActivity.RESULT_OK, i);
        pickerActivity.finish();
    }

    void transImageFinish(int position) {
        Define define = new Define();
        ArrayList<Uri> path = new ArrayList<>();
        for (int i = 0; i < pickedImages.size(); i++) {
            path.add(pickedImages.get(i));
        }
        Intent i = new Intent();
        i.putParcelableArrayListExtra(Define.INTENT_PATH, path);
        i.putParcelableArrayListExtra(define.INTENT_ADD_PATH, getAddImagePaths());
        i.putExtra(define.INTENT_POSITION, position);
        pickerActivity.setResult(define.TRANS_IMAGES_RESULT_CODE, i);
        pickerActivity.finish();
    }

    boolean checkPermission() {
        PermissionCheck permissionCheck = new PermissionCheck(pickerActivity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionCheck.CheckStoragePermission())
                return true;
        } else
            return true;
        return false;
    }


    void displayImage(Long bucketId,
                      Boolean exceptGif) {
        new DisplayImage(bucketId, exceptGif).execute();
    }

    private class DisplayImage extends AsyncTask<Void, Void, Uri[]> {
        private Long bucketId;
        Boolean exceptGif;

        DisplayImage(Long bucketId,
                     Boolean exceptGif) {
            this.bucketId = bucketId;
            this.exceptGif = exceptGif;
        }

        @Override
        protected Uri[] doInBackground(Void... params) {
            return getAllMediaThumbnailsPath(bucketId, exceptGif);
        }

        @Override
        protected void onPostExecute(Uri[] result) {
            super.onPostExecute(result);
            pickerActivity.setAdapter(result);
        }
    }


    @NonNull
    private Uri[] getAllMediaThumbnailsPath(long id,
                                            Boolean exceptGif) {
        String selection = MediaStore.Images.Media.BUCKET_ID + " = ?";
        String bucketId = String.valueOf(id);
        String sort = MediaStore.Images.Media._ID + " DESC";
        String[] selectionArgs = {bucketId};

        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor c;
        if (!bucketId.equals("0")) {
            c = resolver.query(images, null, selection, selectionArgs, sort);
        } else {
            c = resolver.query(images, null, null, null, sort);
        }
        Uri[] imageUris = new Uri[c == null ? 0 : c.getCount()];
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    setPathDir(c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA)),
                            c.getString(c.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)));
                    int position = -1;
                    RegexUtil regexUtil = new RegexUtil();
                    do {
                        if (exceptGif &&
                                regexUtil.checkGif(c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA))))
                            continue;
                        int imgId = c.getInt(c.getColumnIndex(MediaStore.MediaColumns._ID));
                        Uri path = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + imgId);
                        imageUris[++position] = path;
                    } while (c.moveToNext());
                }
                c.close();
            } catch (Exception e) {
                if (!c.isClosed()) c.close();
            }
        }
        return imageUris;
    }

    private String setPathDir(String path, String fileName) {
        return pathDir = path.replace("/" + fileName, "");
    }

    public String getPathDir(Long bucketId) {
        if (pathDir.equals("") || bucketId == 0)
            pathDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM + "/Camera").getAbsolutePath();
        return pathDir;
    }

    public int getPickedImageIndexOf(Uri uri) {
        return pickedImages.indexOf(uri);
    }
}
