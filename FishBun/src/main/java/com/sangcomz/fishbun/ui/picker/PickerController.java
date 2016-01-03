package com.sangcomz.fishbun.ui.picker;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;

import com.sangcomz.fishbun.define.Define;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sangc on 2015-11-05.
 */
public class PickerController {
    PickerActivity pickerActivity;
    private RecyclerView recyclerView;
    private RecyclerView.OnItemTouchListener OnItemTouchListener;
    ActionBar actionBar;
    String bucketTitle;

    private String savePath;

    PickerController(PickerActivity pickerActivity, ActionBar actionBar, RecyclerView recyclerView, String bucketTitle) {
        this.pickerActivity = pickerActivity;
        this.recyclerView = recyclerView;
        this.actionBar = actionBar;
        this.bucketTitle = bucketTitle;

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

    public void setActionbarTitle(int total) {
        if (Define.ALBUM_PICKER_COUNT == 1)
            actionBar.setTitle(bucketTitle);
        else
            actionBar.setTitle(bucketTitle + "(" + String.valueOf(total) + "/" + Define.ALBUM_PICKER_COUNT + ")");
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
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }


    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

}
