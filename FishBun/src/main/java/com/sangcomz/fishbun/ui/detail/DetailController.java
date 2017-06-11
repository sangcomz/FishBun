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

    DetailController(DetailActivity detailActivity) {
        this.detailActivity = detailActivity;
    }


    public void finishActivity(ArrayList<Uri> pickedImages) {
        ArrayList<Uri> path = new ArrayList<>();
        for (int i = 0; i < pickedImages.size(); i++) {
            path.add(pickedImages.get(i));
        }
        Intent i = new Intent();
        i.putParcelableArrayListExtra(Define.INTENT_PATH, path);
        detailActivity.setResult(detailActivity.RESULT_OK, i);
        detailActivity.finish();
    }
}
