package com.sangcomz.fishbun.ui.picker;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import com.sangcomz.fishbun.MimeType;
import com.sangcomz.fishbun.model.PickerRepository;
import com.sangcomz.fishbun.permission.PermissionCheck;
import com.sangcomz.fishbun.util.CameraUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by sangc on 2015-11-05.
 */
public class PickerController {
    private PickerRepository pickerRepository;
    private Future<List<Uri>> imageListFuture;
    private Future<String> dirPathFuture;
    private PickerActivity pickerActivity;
    private ArrayList<Uri> addImagePaths = new ArrayList<>();
    private CameraUtil cameraUtil = new CameraUtil();
    private String pathDir = "";


    PickerController(PickerActivity pickerActivity,
                     PickerRepository pickerRepository) {
        this.pickerRepository = pickerRepository;
        this.pickerActivity = pickerActivity;
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


    boolean checkPermission() {
        PermissionCheck permissionCheck = new PermissionCheck(pickerActivity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionCheck.checkStoragePermission())
                return true;
        } else
            return true;
        return false;
    }

    public boolean checkCameraPermission() {
        PermissionCheck permissionCheck = new PermissionCheck(pickerActivity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionCheck.checkCameraPermission())
                return true;
        } else
            return true;
        return false;
    }

    void displayImage(Long bucketId,
                      List<MimeType> exceptMimeType,
                      List<String> specifyFolderList) {
        try {
            dirPathFuture = pickerRepository.getDirectoryPath(bucketId);
            imageListFuture = pickerRepository.getAllMediaThumbnailsPath(bucketId, exceptMimeType, specifyFolderList);

            pathDir = dirPathFuture.get();
            pickerActivity.setAdapter(imageListFuture.get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    String getPathDir(Long bucketId) {
        if (pathDir.equals("") || bucketId == 0)
            pathDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM + "/Camera").getAbsolutePath();
        return pathDir;
    }

    public void finishActivity() {
        pickerActivity.finishActivity();
    }

    public void release(){
        dirPathFuture.cancel(true);
        imageListFuture.cancel(true);
    }
}