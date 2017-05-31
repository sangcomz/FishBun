package com.sangcomz.fishbun.ui.picker;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.sangcomz.fishbun.BaseActivity;
import com.sangcomz.fishbun.R;
import com.sangcomz.fishbun.adapter.PickerGridAdapter;
import com.sangcomz.fishbun.bean.Album;
import com.sangcomz.fishbun.define.Define;
import com.sangcomz.fishbun.permission.PermissionCheck;
import com.sangcomz.fishbun.util.RadioWithTextButton;
import com.sangcomz.fishbun.util.SingleMediaScanner;
import com.sangcomz.fishbun.util.TextDrawable;

import java.io.File;
import java.util.ArrayList;


public class PickerActivity extends BaseActivity {

    private static final String TAG = "PickerActivity";

    private RecyclerView recyclerView;
    private ArrayList<Uri> pickedImages;
    private PickerController pickerController;
    private Album album;
    private int position;
    private PickerGridAdapter adapter;
    private GridLayoutManager layoutManager;

    private void initValue() {
        Intent intent = getIntent();
        album = intent.getParcelableExtra(Define.BUNDLE_NAME.ALBUM.name());
        position = intent.getIntExtra(Define.BUNDLE_NAME.POSITION.name(), -1);

        //only first init
        if (pickedImages == null) {
            pickedImages = new ArrayList<>();
            ArrayList<Uri> path = getIntent().getParcelableArrayListExtra(Define.INTENT_PATH);
            if (path != null) {
                for (int i = 0; i < path.size(); i++) {
                    pickedImages.add(path.get(i));
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        try {
            outState.putParcelableArrayList(define.SAVE_INSTANCE_PICK_IMAGES, pickedImages);
            outState.putString(define.SAVE_INSTANCE_SAVED_IMAGE, pickerController.getSavePath());
            outState.putParcelableArray(define.SAVE_INSTANCE_SAVED_IMAGE_THUMBNAILS, adapter.getImages());
            outState.putParcelableArrayList(define.SAVE_INSTANCE_NEW_IMAGES, pickerController.getAddImagePaths());
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(outState);
        // Restore state members from saved instance
        try {
            pickedImages = outState.getParcelableArrayList(define.SAVE_INSTANCE_PICK_IMAGES);
            ArrayList<Uri> addImages = outState.getParcelableArrayList(define.SAVE_INSTANCE_NEW_IMAGES);
            String savedImage = outState.getString(define.SAVE_INSTANCE_SAVED_IMAGE);
            Uri[] images = (Uri[]) outState.getParcelableArray(define.SAVE_INSTANCE_SAVED_IMAGE_THUMBNAILS);
            setAdapter(images);
            if (addImages != null) {
                pickerController.setAddImagePaths(addImages);
            }
            if (savedImage != null) {
                pickerController.setSavePath(savedImage);
            }
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_picker);
        initValue();
        initView();
        initController();
        if (pickerController.checkPermission())
            pickerController.displayImage(album.bucketId, exceptGif);
    }

    @Override
    public void onBackPressed() {
        pickerController.transImageFinish(pickedImages, position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == define.TAKE_A_PICK_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                File savedFile = new File(pickerController.getSavePath());
                new SingleMediaScanner(this, savedFile);
                adapter.addImage(Uri.fromFile(savedFile));
            } else {
                new File(pickerController.getSavePath()).delete();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 28: {
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        pickerController.displayImage(album.bucketId, exceptGif);
                        // permission was granted, yay! do the
                        // calendar task you need to do.
                    } else {
                        new PermissionCheck(this).showPermissionDialog();
                        finish();
                    }
                }
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photo_album, menu);
        MenuItem item = menu.findItem(R.id.action_ok);
        if (okButtonDrawable != null) {
            item.setIcon(okButtonDrawable);
        } else if (menuText != null) {
            item.setIcon(new TextDrawable(getResources(), menuText, colorMenuText));
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify album parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_ok) {
            if (pickedImages.size() < minCount) {
                Snackbar.make(recyclerView, messageNothingSelected, Snackbar.LENGTH_SHORT).show();
            } else {
                pickerController.finishActivity(pickedImages);
            }
            return true;
        } else if (id == android.R.id.home)
            pickerController.transImageFinish(pickedImages, position);
        return super.onOptionsItemSelected(item);
    }

    public void showToolbarTitle(int total) {
        if (getSupportActionBar() != null) {
            if (maxCount == 1)
                getSupportActionBar().setTitle(album.bucketName);
            else
                getSupportActionBar().setTitle(album.bucketName + "(" + String.valueOf(total) + "/" + maxCount + ")");
        }
    }

    private void initController() {
        pickerController = new PickerController(this, recyclerView);
    }

    private void initView() {

        recyclerView = (RecyclerView) findViewById(R.id.recycler_picker_list);
        layoutManager = new GridLayoutManager(this, photoSpanCount, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        initToolBar();
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_picker_bar);
        setSupportActionBar(toolbar);

        toolbar.setBackgroundColor(colorActionBar);
        toolbar.setTitleTextColor(colorActionBarTitle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            uiUtil.setStatusBarColor(this, colorStatusBar);
        }
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            if (homeAsUpIndicatorDrawable != null)
                getSupportActionBar().setHomeAsUpIndicator(homeAsUpIndicatorDrawable);
        }

        if (statusBarLight
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            toolbar.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }
    }


    public void setAdapter(Uri[] result) {
        if (adapter == null) {
            adapter = new PickerGridAdapter(
                    result,
                    pickedImages,
                    pickerController,
                    pickerController.getPathDir(album.bucketId),
                    isCamera,
                    colorActionBar,
                    colorActionBarTitle,
                    maxCount,
                    messageLimitReached,
                    isAutomaticClose);
            adapter.setActionListener(new PickerGridAdapter.OnPhotoActionListener() {
                @Override
                public void onDeselect() {
                    refreshThumb();
                }
            });
        }
        recyclerView.setAdapter(adapter);
        showToolbarTitle(pickedImages.size());
    }

    private void refreshThumb() {
        int firstVisible = layoutManager.findFirstVisibleItemPosition();
        int lastVisible = layoutManager.findLastVisibleItemPosition();
        for (int i = firstVisible; i <= lastVisible; i++) {
            View view = layoutManager.findViewByPosition(i);
            if (view instanceof RelativeLayout) {
                RelativeLayout item = (RelativeLayout) view;
                RadioWithTextButton btnThumbCount = (RadioWithTextButton) item.findViewById(R.id.btn_thumb_count);
                Uri image = (Uri) item.getTag();
                if (image != null) {
                    int index = adapter.getPickedImageIndexOf(image);
                    if (index != -1) {
                        adapter.updateRadioButton(btnThumbCount, String.valueOf(index + 1));
                    }
                }

            }
        }
    }

}
