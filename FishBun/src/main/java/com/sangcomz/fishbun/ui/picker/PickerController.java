package com.sangcomz.fishbun.ui.picker;

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

import com.sangcomz.fishbun.bean.ImageBean;
import com.sangcomz.fishbun.bean.PickedImageBean;
import com.sangcomz.fishbun.define.Define;
import com.sangcomz.fishbun.permission.PermissionCheck;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sangc on 2015-11-05.
 */
public class PickerController {
    PickerActivity pickerActivity;
    private RecyclerView recyclerView;
    private RecyclerView.OnItemTouchListener OnItemTouchListener;
    private ArrayList<String> addImagePaths = new ArrayList<>();
    private String savePath;
    private ContentResolver resolver;
    private String pathDir = "";


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

    /**
     * @param isAble true == can clickable
     */
    public void setRecyclerViewClickable(final boolean isAble) {
        if (isAble)
            recyclerView.removeOnItemTouchListener(OnItemTouchListener);
        else {
            recyclerView.addOnItemTouchListener(OnItemTouchListener);
        }

    }


    public void setToolbarTitle(int total) {
        pickerActivity.showToolbarTitle(total);
    }

    public void takePicture(String saveDir) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(pickerActivity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile(saveDir); //make a file
                setSavePath(photoFile.getAbsolutePath());
            } catch (IOException ex) {
                ex.printStackTrace();
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
//                Log.d("photoFile path ", String.valueOf(photoFile));
                pickerActivity.startActivityForResult(takePictureIntent, Define.TAKE_A_PICK_REQUEST_CODE);
            }
        }
    }

    private File createImageFile(String saveDir) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(saveDir);
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }


    protected String getSavePath() {
        return savePath;
    }

    protected void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public void setAddImagePath(String imagePath) {
        this.addImagePaths.add(imagePath);
    }

    protected ArrayList<String> getAddImagePaths() {
        return addImagePaths;
    }

    public void setAddImagePaths(ArrayList<String> addImagePaths) {
        this.addImagePaths = addImagePaths;
    }

    public void finishActivity(ArrayList<PickedImageBean> pickedImageBeans) {
        ArrayList<String> path = new ArrayList<>();
        for (int i = 0; i < pickedImageBeans.size(); i++) {
            path.add(pickedImageBeans.get(i).getImgPath());
        }
        Intent i = new Intent();
        i.putStringArrayListExtra(Define.INTENT_PATH, path);
        pickerActivity.setResult(pickerActivity.RESULT_OK, i);
        pickerActivity.finish();

    }

    protected void transImageFinish(ArrayList<PickedImageBean> pickedImageBeans, int position) {
        ArrayList<String> path = new ArrayList<>();
        for (int i = 0; i < pickedImageBeans.size(); i++) {
            path.add(pickedImageBeans.get(i).getImgPath());
        }
        Intent i = new Intent();
        i.putStringArrayListExtra(Define.INTENT_PATH, path);
        i.putStringArrayListExtra(Define.INTENT_ADD_PATH, getAddImagePaths());
        i.putExtra(Define.INTENT_POSITION, position);
        pickerActivity.setResult(Define.TRANS_IMAGES_RESULT_CODE, i);
        pickerActivity.finish();
    }

    protected boolean checkPermission() {
        PermissionCheck permissionCheck = new PermissionCheck(pickerActivity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionCheck.CheckStoragePermission())
                return true;
        } else
            return true;
        return false;
    }

    //MediaScanning
    public void startFileMediaScan() {
        pickerActivity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + getSavePath())));
    }

    protected void displayImage(Long bucketId) {
        new DisplayImage(bucketId).execute();
    }

    private class DisplayImage extends AsyncTask<Void, Void, ImageBean[]> {
        private Long bucketId;

        DisplayImage(Long bucketId) {
            this.bucketId = bucketId;
        }

        @Override
        protected ImageBean[] doInBackground(Void... params) {
            return getAllMediaThumbnailsPath(bucketId);
        }

        @Override
        protected void onPostExecute(ImageBean[] result) {
            super.onPostExecute(result);
            pickerActivity.setAdapter(result);
//            if (adapter == null)
//                adapter = new PickerGridAdapter(
//                        result, pickedImageBeans, pickerController);
//            recyclerView.setAdapter(adapter);
//            showToolbarTitle(pickedImageBeans.size());
        }
    }


    @NonNull
    private ImageBean[] getAllMediaThumbnailsPath(long id) {
        String path;
        String selection = MediaStore.Images.Media.BUCKET_ID + " = ?";
        String bucketid = String.valueOf(id);
        String sort = MediaStore.Images.Media._ID + " DESC";
        String[] selectionArgs = {bucketid};

        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor c;
        if (!bucketid.equals("0")) {
            c = resolver.query(images, null, selection, selectionArgs, sort);
        } else {
            c = resolver.query(images, null, null, null, sort);
        }
        ImageBean[] imageBeans = new ImageBean[c == null ? 0 : c.getCount()];
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    setPathDir(c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA)),
                            c.getString(c.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)));
                    int position = -1;
                    do {
                        path = c.getString(c.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
                        imageBeans[++position] = new ImageBean(-1, path);
                    } while (c.moveToNext());
                }
                c.close();
            } catch (Exception e) {
                if (!c.isClosed()) c.close();
            }
        }
        return imageBeans;
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
}
