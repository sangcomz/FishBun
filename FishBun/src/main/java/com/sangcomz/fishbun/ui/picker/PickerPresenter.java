package com.sangcomz.fishbun.ui.picker;

import android.net.Uri;
import android.os.Environment;

import com.sangcomz.fishbun.MimeType;
import com.sangcomz.fishbun.ui.picker.model.PickerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by sangc on 2015-11-05.
 */
public class PickerPresenter {
    private PickerView pickerView;
    private PickerRepository pickerRepository;
    private Future<List<Uri>> imageListFuture;
    private Future<String> dirPathFuture;
    private ArrayList<Uri> addImagePaths = new ArrayList<>();
    private String pathDir = "";


    PickerPresenter(PickerView pickerView, PickerRepository pickerRepository) {
        this.pickerView = pickerView;
        this.pickerRepository = pickerRepository;
    }



//    public void setToolbarTitle(int total) {
//        pickerActivity.showToolbarTitle(total);
//    }

    public void setAddImagePath(Uri imagePath) {
        this.addImagePaths.add(imagePath);
    }

    protected ArrayList<Uri> getAddImagePaths() {
        return addImagePaths;
    }

    public void setAddImagePaths(ArrayList<Uri> addImagePaths) {
        this.addImagePaths = addImagePaths;
    }


    void displayImage(Long bucketId,
                      List<MimeType> exceptMimeType,
                      List<String> specifyFolderList) {
        try {
            dirPathFuture = pickerRepository.getDirectoryPath(bucketId);
            imageListFuture = pickerRepository.getAllMediaThumbnailsPath(bucketId);

            pathDir = dirPathFuture.get();
            pickerView.showImageList(imageListFuture.get());
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

    public void release(){
        dirPathFuture.cancel(true);
        imageListFuture.cancel(true);
    }
}