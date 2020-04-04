package com.sangcomz.fishbun.ui.picker;

import android.content.Context;
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
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.R;
import com.sangcomz.fishbun.datasource.ImageDataSource;
import com.sangcomz.fishbun.datasource.ImageDataSourceImpl;
import com.sangcomz.fishbun.permission.PermissionCheck;
import com.sangcomz.fishbun.ui.picker.model.PickerRepository;
import com.sangcomz.fishbun.ui.picker.model.PickerRepositoryImpl;
import com.sangcomz.fishbun.util.RadioWithTextButton;
import com.sangcomz.fishbun.util.SingleMediaScanner;
import com.sangcomz.fishbun.util.SquareFrameLayout;
import com.sangcomz.fishbun.util.UiUtil;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class PickerActivity extends BaseActivity implements PickerView {

    private static final String TAG = "PickerActivity";


    private RecyclerView recyclerView;
    private PickerPresenter pickerPresenter;
    private Long albumId;
    private String albumName;
    private int albumPosition;
    private PickerGridAdapter adapter;
    private GridLayoutManager layoutManager;

    private void initValue() {
        Intent intent = getIntent();
        albumId = intent.getLongExtra(ALBUM_ID, -1);
        albumName = intent.getStringExtra(ALBUM_NAME);
        albumPosition = intent.getIntExtra(ALBUM_POSITION, -1);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        try {
            outState.putString(SAVE_INSTANCE_SAVED_IMAGE, getSavePath());
            outState.putParcelableArrayList(SAVE_INSTANCE_NEW_IMAGES, pickerPresenter.getAddImagePaths());
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
            ArrayList<Uri> addImages = outState.getParcelableArrayList(SAVE_INSTANCE_NEW_IMAGES);
            String savedImage = outState.getString(SAVE_INSTANCE_SAVED_IMAGE);
            setImageList(getFishton().getPickerImages());
            if (addImages != null) {
                pickerPresenter.setAddImagePaths(addImages);
            }
            if (savedImage != null) {
                setSavePath(savedImage);
            }
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_picker);
        initPresenter();
        initValue();
        initView();
        if (checkPermission())
            pickerPresenter.displayImage(albumId, getFishton().getExceptMimeTypeList(), getFishton().getSpecifyFolderList());

    }

    @Override
    public void onBackPressed() {
        transImageFinish(albumPosition);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_A_PICK_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                File savedFile = new File(getSavePath());
                new SingleMediaScanner(this, savedFile);
                adapter.addImage(Uri.fromFile(savedFile));
            } else {
                new File(getSavePath()).delete();
            }
        } else if (requestCode == ENTER_DETAIL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (getFishton().isAutomaticClose() && getFishton().getSelectedImages().size() == getFishton().getMaxCount())
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
                        pickerPresenter.displayImage(albumId, getFishton().getExceptMimeTypeList(), getFishton().getSpecifyFolderList());
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
                        takePicture(pickerPresenter.getPathDir(albumId));
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

        if (getFishton().getDrawableDoneButton() != null) {
            menuDoneItem.setIcon(getFishton().getDrawableDoneButton());
        } else if (getFishton().getStrDoneMenu() != null) {
            if (getFishton().getColorTextMenu() != Integer.MAX_VALUE) {
                SpannableString spanString = new SpannableString(getFishton().getStrDoneMenu());
                spanString.setSpan(new ForegroundColorSpan(getFishton().getColorTextMenu()), 0, spanString.length(), 0); //fi
                menuDoneItem.setTitle(spanString);
            } else {
                menuDoneItem.setTitle(getFishton().getStrDoneMenu());
            }
            menuDoneItem.setIcon(null);
        }
        if (getFishton().isUseAllDoneButton()) {
            menuAllDoneItem.setVisible(true);
            if (getFishton().getDrawableAllDoneButton() != null) {
                menuAllDoneItem.setIcon(getFishton().getDrawableAllDoneButton());
            } else if (getFishton().getStrAllDoneMenu() != null) {
                if (getFishton().getColorTextMenu() != Integer.MAX_VALUE) {
                    SpannableString spanString = new SpannableString(getFishton().getStrAllDoneMenu());
                    spanString.setSpan(new ForegroundColorSpan(getFishton().getColorTextMenu()), 0, spanString.length(), 0); //fi
                    menuAllDoneItem.setTitle(spanString);
                } else {
                    menuAllDoneItem.setTitle(getFishton().getStrAllDoneMenu());
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
            if (getFishton().getSelectedImages().size() < getFishton().getMinCount()) {
                Snackbar.make(recyclerView, getFishton().getMessageNothingSelected(), Snackbar.LENGTH_SHORT).show();
            } else {
                finishActivity();
            }
            return true;
        } else if (id == R.id.action_all_done) {
            for (Uri pickerImage : getFishton().getPickerImages()) {
                if (getFishton().getSelectedImages().size() == getFishton().getMaxCount()) {
                    break;
                }
                if (!getFishton().getSelectedImages().contains(pickerImage)) {
                    getFishton().getSelectedImages().add(pickerImage);
                }
            }
            finishActivity();
        } else if (id == android.R.id.home)
            transImageFinish(albumPosition);
        return super.onOptionsItemSelected(item);
    }

    public void showToolbarTitle(int total) {
        if (getSupportActionBar() != null) {
            if (getFishton().getMaxCount() == 1 || !getFishton().isShowCount())
                getSupportActionBar()
                        .setTitle(albumName);
            else
                getSupportActionBar()
                        .setTitle(albumName + " (" + total + "/" + getFishton().getMaxCount() + ")");
        }
    }

    private void initPresenter() {
        pickerPresenter =
                new PickerPresenter(this, getRepository());
    }

    private PickerRepository getRepository() {
        return new PickerRepositoryImpl(getImageDataSource());
    }

    private ImageDataSource getImageDataSource() {
        return new ImageDataSourceImpl(this.getContentResolver(),
                getFishton().getExceptMimeTypeList(),
                getFishton().getSpecifyFolderList());
    }

    private void initView() {
        recyclerView = findViewById(R.id.recycler_picker_list);
        layoutManager = new GridLayoutManager(this, getFishton().getPhotoSpanCount(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        initToolBar();
    }

    private void initToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar_picker_bar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getFishton().getColorActionBar());
        toolbar.setTitleTextColor(getFishton().getColorActionBarTitle());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UiUtil.setStatusBarColor(this, getFishton().getColorStatusBar());
        }
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            if (getFishton().getDrawableHomeAsUpIndicator() != null)
                getSupportActionBar().setHomeAsUpIndicator(getFishton().getDrawableHomeAsUpIndicator());
        }

        if (getFishton().isStatusBarLight()
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            toolbar.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        showToolbarTitle(0);
    }


    public void setImageList(List<Uri> imageList) {
        getFishton().setPickerImages(imageList);
        if (adapter == null) {
            adapter = new PickerGridAdapter(this,
                    pickerPresenter.getPathDir(albumId));
            adapter.setActionListener(new PickerGridAdapter.OnPhotoActionListener() {
                @Override
                public void onDeselect() {
                    refreshThumb();
                }
            });
        }
        recyclerView.setAdapter(adapter);
        showToolbarTitle(getFishton().getSelectedImages().size());
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
                    int index = getFishton().getSelectedImages().indexOf(image);
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
                        showToolbarTitle(getFishton().getSelectedImages().size());
                    }
                }

            }
        }
    }

    void transImageFinish(int position) {
        Intent i = new Intent();
        i.putParcelableArrayListExtra(INTENT_ADD_PATH, pickerPresenter.getAddImagePaths());
        i.putExtra(INTENT_POSITION, position);
        setResult(TRANS_IMAGES_RESULT_CODE, i);
        finish();
    }

    public void finishActivity() {
        Intent i = new Intent();
        setResult(RESULT_OK, i);
        if (getFishton().isStartInAllView())
            i.putParcelableArrayListExtra(FishBun.INTENT_PATH, getFishton().getSelectedImages());
        finish();
    }

    public void setToolbarTitle(int total) {
        showToolbarTitle(total);
    }


    public void takePicture(String saveDir) {
        getCameraUtil().takePicture(this, saveDir, TAKE_A_PICK_REQUEST_CODE);
    }


    String getSavePath() {
        return getCameraUtil().getSavePath();
    }

    void setSavePath(String savePath) {
        getCameraUtil().setSavePath(savePath);
    }

    boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getPermissionCheck().checkStoragePermission(PERMISSION_STORAGE))
                return true;
        } else
            return true;
        return false;
    }

    public boolean checkCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getPermissionCheck().checkCameraPermission(PERMISSION_CAMERA))
                return true;
        } else
            return true;
        return false;
    }

    @Override
    public void showImageList(@NotNull List<? extends Uri> imageList) {
        setImageList((List<Uri>) imageList);
    }

    public void setAddImagePath(Uri imagePath) {
        pickerPresenter.setAddImagePath(imagePath);
    }


    private static String ALBUM_ID = "album_id";
    private static String ALBUM_NAME = "album_name";
    private static String ALBUM_POSITION = "album_position";

    public static Intent getPickerActivityIntent(Context context,
                                                 Long albumId,
                                                 String albumName,
                                                 int albumPosition) {
        Intent intent = new Intent(context, PickerActivity.class);
        intent.putExtra(ALBUM_ID, albumId);
        intent.putExtra(ALBUM_NAME, albumName);
        intent.putExtra(ALBUM_POSITION, albumPosition);
        return intent;
    }
}
