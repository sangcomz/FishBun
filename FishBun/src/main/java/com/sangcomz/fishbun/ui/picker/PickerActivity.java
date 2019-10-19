package com.sangcomz.fishbun.ui.picker;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.sangcomz.fishbun.BaseActivity;
import com.sangcomz.fishbun.R;
import com.sangcomz.fishbun.adapter.view.PickerGridAdapter;
import com.sangcomz.fishbun.bean.Album;
import com.sangcomz.fishbun.define.Define;
import com.sangcomz.fishbun.permission.PermissionCheck;
import com.sangcomz.fishbun.util.RadioWithTextButton;
import com.sangcomz.fishbun.util.SingleMediaScanner;
import com.sangcomz.fishbun.util.SquareFrameLayout;
import com.sangcomz.fishbun.util.UiUtil;

import java.io.File;
import java.util.ArrayList;


public class PickerActivity extends BaseActivity {

    private static final String TAG = "PickerActivity";

    private RecyclerView recyclerView;
    private PickerController pickerController;
    private Album album;
    private int position;
    private PickerGridAdapter adapter;
    private GridLayoutManager layoutManager;

    private void initValue() {
        Intent intent = getIntent();
        album = intent.getParcelableExtra(Define.BUNDLE_NAME.ALBUM.name());
        position = intent.getIntExtra(Define.BUNDLE_NAME.POSITION.name(), -1);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        try {
            outState.putString(define.SAVE_INSTANCE_SAVED_IMAGE, pickerController.getSavePath());
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
            ArrayList<Uri> addImages = outState.getParcelableArrayList(define.SAVE_INSTANCE_NEW_IMAGES);
            String savedImage = outState.getString(define.SAVE_INSTANCE_SAVED_IMAGE);
            setAdapter(fishton.getPickerImages());
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
        initController();
        initValue();
        initView();
        if (pickerController.checkPermission())
            pickerController.displayImage(album.bucketId, fishton.isExceptGif());

    }

    @Override
    public void onBackPressed() {
        transImageFinish(position);
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
        } else if (requestCode == define.ENTER_DETAIL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (fishton.isAutomaticClose() && fishton.getSelectedImages().size() == fishton.getMaxCount())
                    finishActivity();
                refreshThumb();
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
                        pickerController.displayImage(album.bucketId, fishton.isExceptGif());
                        // permission was granted, yay! do the
                        // calendar task you need to do.
                    } else {
                        new PermissionCheck(this).showPermissionDialog();
                        finish();
                    }
                }
                break;
            }
            case 29: {
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // permission was granted, yay!
                        pickerController.takePicture(this, pickerController.getPathDir(album.bucketId));
                    } else {
                        new PermissionCheck(this).showPermissionDialog();
                    }
                }
                break;
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photo_album, menu);
        MenuItem menuDoneItem = menu.findItem(R.id.action_done);
        MenuItem menuAllDoneItem = menu.findItem(R.id.action_all_done);

        if (fishton.getDrawableDoneButton() != null) {
            menuDoneItem.setIcon(fishton.getDrawableDoneButton());
        } else if (fishton.getStrDoneMenu() != null) {
            if (fishton.getColorTextMenu() != Integer.MAX_VALUE) {
                SpannableString spanString = new SpannableString(fishton.getStrDoneMenu());
                spanString.setSpan(new ForegroundColorSpan(fishton.getColorTextMenu()), 0, spanString.length(), 0); //fi
                menuDoneItem.setTitle(spanString);
            } else {
                menuDoneItem.setTitle(fishton.getStrDoneMenu());
            }
            menuDoneItem.setIcon(null);
        }
        if (fishton.isUseAllDoneButton()){
            menuAllDoneItem.setVisible(true);
            if (fishton.getDrawableAllDoneButton() != null) {
                menuAllDoneItem.setIcon(fishton.getDrawableAllDoneButton());
            } else if (fishton.getStrAllDoneMenu() != null) {
                if (fishton.getColorTextMenu() != Integer.MAX_VALUE) {
                    SpannableString spanString = new SpannableString(fishton.getStrAllDoneMenu());
                    spanString.setSpan(new ForegroundColorSpan(fishton.getColorTextMenu()), 0, spanString.length(), 0); //fi
                    menuAllDoneItem.setTitle(spanString);
                } else {
                    menuAllDoneItem.setTitle(fishton.getStrAllDoneMenu());
                }
                menuAllDoneItem.setIcon(null);
            }
        } else {
            menuAllDoneItem.setVisible(false);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify album parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_done) {
            if (fishton.getSelectedImages().size() < fishton.getMinCount()) {
                Snackbar.make(recyclerView, fishton.getMessageNothingSelected(), Snackbar.LENGTH_SHORT).show();
            } else {
                finishActivity();
            }
            return true;
        } else if (id == R.id.action_all_done){
            for (Uri pickerImage : fishton.getPickerImages()) {
                if (fishton.getSelectedImages().size() == fishton.getMaxCount()){
                    break;
                }
                if (!fishton.getSelectedImages().contains(pickerImage)){
                    fishton.getSelectedImages().add(pickerImage);
                }
            }
            finishActivity();
        } else if (id == android.R.id.home)
            transImageFinish(position);
        return super.onOptionsItemSelected(item);
    }

    public void showToolbarTitle(int total) {
        if (getSupportActionBar() != null) {
            if (fishton.getMaxCount() == 1 || !fishton.isShowCount())
                getSupportActionBar()
                        .setTitle(album.bucketName);
            else
                getSupportActionBar()
                        .setTitle(album.bucketName + " (" + total + "/" + fishton.getMaxCount() + ")");
        }
    }

    private void initController() {
        pickerController = new PickerController(this);
    }

    private void initView() {
        recyclerView = findViewById(R.id.recycler_picker_list);
        layoutManager = new GridLayoutManager(this, fishton.getPhotoSpanCount(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        initToolBar();
    }

    private void initToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar_picker_bar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(fishton.getColorActionBar());
        toolbar.setTitleTextColor(fishton.getColorActionBarTitle());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UiUtil.setStatusBarColor(this, fishton.getColorStatusBar());
        }
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            if (fishton.getDrawableHomeAsUpIndicator() != null)
                getSupportActionBar().setHomeAsUpIndicator(fishton.getDrawableHomeAsUpIndicator());
        }

        if (fishton.isStatusBarLight()
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            toolbar.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        showToolbarTitle(0);
    }


    public void setAdapter(Uri[] result) {
        fishton.setPickerImages(result);
        if (adapter == null) {
            adapter = new PickerGridAdapter(pickerController,
                    pickerController.getPathDir(album.bucketId));
            adapter.setActionListener(new PickerGridAdapter.OnPhotoActionListener() {
                @Override
                public void onDeselect() {
                    refreshThumb();
                }
            });
        }
        recyclerView.setAdapter(adapter);
        showToolbarTitle(fishton.getSelectedImages().size());
    }

    private void refreshThumb() {
        int firstVisible = layoutManager.findFirstVisibleItemPosition();
        int lastVisible = layoutManager.findLastVisibleItemPosition();
        for (int i = firstVisible; i <= lastVisible; i++) {
            View view = layoutManager.findViewByPosition(i);
            if (view instanceof SquareFrameLayout) {
                SquareFrameLayout item = (SquareFrameLayout) view;
                RadioWithTextButton btnThumbCount = item.findViewById(R.id.btn_thumb_count);
                ImageView imgThumbImage = item.findViewById(R.id.img_thumb_image);
                Uri image = (Uri) item.getTag();
                if (image != null) {
                    int index = fishton.getSelectedImages().indexOf(image);
                    if (index != -1) {
                        adapter.updateRadioButton(imgThumbImage,
                                btnThumbCount,
                                String.valueOf(index + 1),
                                true);
                    } else {
                        adapter.updateRadioButton(imgThumbImage,
                                btnThumbCount,
                                "",
                                false);
                        showToolbarTitle(fishton.getSelectedImages().size());
                    }
                }

            }
        }
    }

    void transImageFinish(int position) {
        Define define = new Define();
        Intent i = new Intent();
        i.putParcelableArrayListExtra(define.INTENT_ADD_PATH, pickerController.getAddImagePaths());
        i.putExtra(define.INTENT_POSITION, position);
        setResult(define.TRANS_IMAGES_RESULT_CODE, i);
        finish();
    }

    public void finishActivity() {
        Intent i = new Intent();
        setResult(RESULT_OK, i);
        if (fishton.isStartInAllView())
            i.putParcelableArrayListExtra(Define.INTENT_PATH, fishton.getSelectedImages());
        finish();
    }

}
