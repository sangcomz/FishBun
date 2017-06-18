package com.sangcomz.fishbun.ui.detail;

import android.content.Intent;
import android.net.Uri;

import com.sangcomz.fishbun.define.Define;

import java.util.ArrayList;

/**
 * Created by sangcomz on 11/06/2017.
 */

public class DetailController {

    private DetailActivity detailActivity;
    private ArrayList<Uri> pickedImages = new ArrayList<>();

    DetailController(DetailActivity detailActivity) {
        this.detailActivity = detailActivity;
    }

    public void setPickedImages(ArrayList<Uri> pickedImages) {
        this.pickedImages = pickedImages;
    }

    public ArrayList<Uri> getPickedImage() {
        return pickedImages;
    }

    public void addPickedImage(Uri image) {
        if (!isAdded(image)) {
            pickedImages.add(image);
        }
        detailActivity.onCheckStateChange(image);
    }

    public void removePickedImage(Uri image) {
        pickedImages.remove(image);
        detailActivity.onCheckStateChange(image);
    }

    public boolean isAdded(Uri image) {
        return pickedImages.contains(image);
    }

    public void finishActivity() {
        Intent i = new Intent();
        i.putParcelableArrayListExtra(Define.INTENT_PATH, pickedImages);
        detailActivity.setResult(detailActivity.RESULT_OK, i);
        detailActivity.finish();
    }
}
