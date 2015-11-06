package com.sangcomz.fishbun.permission;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.sangcomz.fishbun.define.Define;


/**
 * Created by sangc on 2015-10-12.
 */
public class PermissionCheck {
    Context context;

    public PermissionCheck(Context context) {
        this.context = context;
    }


    @TargetApi(Build.VERSION_CODES.M)
    public boolean CheckStoragePermission() {
        int permissionCheckWrite = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheckRead = ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheckWrite != PackageManager.PERMISSION_GRANTED ||
                permissionCheckRead != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        Define.PERMISSION_STORAGE);
            } else {
                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        Define.PERMISSION_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
            return false;
        } else
            return true;
    }




    public void showPermissionDialog(View view) {
//        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(true);
//        dialog.setContentView(R.layout.dialog_permission);
//        TextView dTitle = (TextView) dialog.findViewById(R.id.alert_title);
//        TextView dContent = (TextView) dialog.findViewById(R.id.alert_content);
//			TextView dContent = (TextView)dialog.findViewById(R.id.alert_content);

//        dTitle.setText("권한거부 알림");
//        title = title.replaceAll(query, "<font color='#78C70F'>" + query + "</font>");

        String strStorage = "저장소에 관한 권한 거부로 <font color='#78C70F'> '포스트 작성 및 편집', '회원가입 프로필 사진 업로드', '프로필 사진 변경' </font>의 기능 사용시 제약을 받을 수 있습니다.";
        Snackbar.make(view, strStorage, Snackbar.LENGTH_SHORT).show();

    }


}
