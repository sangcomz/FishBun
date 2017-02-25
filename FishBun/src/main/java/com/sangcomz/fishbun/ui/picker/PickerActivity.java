package com.sangcomz.fishbun.ui.picker;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.sangcomz.fishbun.R;
import com.sangcomz.fishbun.adapter.PickerGridAdapter;
import com.sangcomz.fishbun.bean.Album;
import com.sangcomz.fishbun.bean.Image;
import com.sangcomz.fishbun.bean.PickedImage;
import com.sangcomz.fishbun.define.Define;
import com.sangcomz.fishbun.permission.PermissionCheck;
import com.sangcomz.fishbun.util.SingleMediaScanner;
import com.sangcomz.fishbun.util.UiUtil;

import java.io.File;
import java.util.ArrayList;

import static com.sangcomz.fishbun.define.Define.homeAsUpIndicatorDrawable;
import static com.sangcomz.fishbun.define.Define.okButtonDrawable;


public class PickerActivity extends AppCompatActivity {

    private static final String TAG = "PickerActivity";

    private RecyclerView recyclerView;
    private ArrayList<PickedImage> pickedImages;
    private PickerController pickerController;
    private Album album;
    private int position;
    private UiUtil uiUtil = new UiUtil();
    private PickerGridAdapter adapter;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        try {
            outState.putParcelableArrayList(Define.SAVE_INSTANCE_PICK_IMAGES, pickedImages);
            outState.putString(Define.SAVE_INSTANCE_SAVED_IMAGE, pickerController.getSavePath());
            outState.putParcelableArray(Define.SAVE_INSTANCE_SAVED_IMAGE_THUMBNAILS, adapter.getImages());
            outState.putParcelableArrayList(Define.SAVE_INSTANCE_NEW_IMAGES, pickerController.getAddImagePaths());
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
            pickedImages = outState.getParcelableArrayList(Define.SAVE_INSTANCE_PICK_IMAGES);
            ArrayList<Uri> addImages = outState.getParcelableArrayList(Define.SAVE_INSTANCE_NEW_IMAGES);
            String savedImage = outState.getString(Define.SAVE_INSTANCE_SAVED_IMAGE);
            Image[] imageBeenList = (Image[]) outState.getParcelableArray(Define.SAVE_INSTANCE_SAVED_IMAGE_THUMBNAILS);
            adapter = new PickerGridAdapter(imageBeenList,
                    pickedImages,
                    pickerController,
                    pickerController.getPathDir(album.bucketId));
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
        initView();
        initController();
        setData(getIntent());

        if (pickerController.checkPermission())
            pickerController.displayImage(album.bucketId);
    }

    @Override
    public void onBackPressed() {
        pickerController.transImageFinish(pickedImages, position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Define.TAKE_A_PICK_REQUEST_CODE) {
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
            case Define.PERMISSION_STORAGE: {
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        pickerController.displayImage(album.bucketId);
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
        if (okButtonDrawable != null)
            menu.findItem(R.id.action_ok).setIcon(okButtonDrawable);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify album parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_ok) {
            if (pickedImages.size() == 0) {
                Snackbar.make(recyclerView, Define.MESSAGE_NOTHING_SELECTED, Snackbar.LENGTH_SHORT).show();
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
            if (Define.ALBUM_PICKER_COUNT == 1)
                getSupportActionBar().setTitle(album.bucketName);
            else
                getSupportActionBar().setTitle(album.bucketName + "(" + String.valueOf(total) + "/" + Define.ALBUM_PICKER_COUNT + ")");
        }

    }

    private void setData(Intent intent) {
        album = intent.getParcelableExtra("album");
        position = intent.getIntExtra("position", -1);

        //only first init
        if (pickedImages == null) {
            pickedImages = new ArrayList<>();
            ArrayList<Uri> path = getIntent().getParcelableArrayListExtra(Define.INTENT_PATH);
            if (path != null) {
                for (int i = 0; i < path.size(); i++) {
                    pickedImages.add(new PickedImage(i + 1, path.get(i), -1));
                }
            }
        }
    }

    private void initController() {
        pickerController = new PickerController(this, recyclerView);
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_picker_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, Define.PHOTO_SPAN_COUNT, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        initToolBar();
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_picker_bar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Define.COLOR_ACTION_BAR);
        toolbar.setTitleTextColor(Define.COLOR_ACTION_BAR_TITLE_COLOR);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            uiUtil.setStatusBarColor(this);
        }
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            if (homeAsUpIndicatorDrawable != null)
                getSupportActionBar().setHomeAsUpIndicator(homeAsUpIndicatorDrawable);
        }

        if (Define.STYLE_STATUS_BAR_LIGHT
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            toolbar.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }
    }


    public void setAdapter(Image[] result) {
        if (adapter == null)
            adapter = new PickerGridAdapter(
                    result, pickedImages, pickerController, pickerController.getPathDir(album.bucketId));
        recyclerView.setAdapter(adapter);
        showToolbarTitle(pickedImages.size());
    }


}
