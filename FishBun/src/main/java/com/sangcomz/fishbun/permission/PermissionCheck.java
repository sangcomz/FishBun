package com.sangcomz.fishbun.permission;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.widget.Toast;

import com.sangcomz.fishbun.R;
import com.sangcomz.fishbun.define.Define;


/**
 * Created by sangc on 2015-10-12.
 */
public class PermissionCheck {
    private Context context;

    private final String[] permissions = new String[] {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    public PermissionCheck(Context context) {
        this.context = context;
    }


    @TargetApi(Build.VERSION_CODES.M)
    public boolean CheckStoragePermission() {
        if (isNecessaryToCheckPermissions()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    (Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                requestPermissions();
            } else {
                // No explanation needed, we can request the permission.
                requestPermissions();

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
            return false;
        } else
            return true;
    }

    private boolean isNecessaryToCheckPermissions() {
        for(int i = 0; i < permissions.length; i++) {
            int result = ContextCompat.checkSelfPermission(context, permissions[i]);
            if(result != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

    private void requestPermissions() {
        final Define define = new Define();
        ActivityCompat.requestPermissions((Activity) context, permissions, define.PERMISSION_STORAGE);
    }

    public void showPermissionDialog() {
        Toast.makeText(context, R.string.msg_permission, Toast.LENGTH_SHORT).show();
    }


}
