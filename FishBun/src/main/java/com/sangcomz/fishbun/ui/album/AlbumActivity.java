package com.sangcomz.fishbun.ui.album;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.sangcomz.fishbun.BaseActivity;
import com.sangcomz.fishbun.R;
import com.sangcomz.fishbun.adapter.view.AlbumListAdapter;
import com.sangcomz.fishbun.bean.Album;
import com.sangcomz.fishbun.define.Define;
import com.sangcomz.fishbun.permission.PermissionCheck;
import com.sangcomz.fishbun.util.ScanListener;
import com.sangcomz.fishbun.util.SingleMediaScanner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AlbumActivity extends BaseActivity {
    private AlbumController albumController;
    private ArrayList<Album> albumList = new ArrayList<>();

    private RecyclerView recyclerAlbumList;
    private RelativeLayout relAlbumEmpty;

    private AlbumListAdapter adapter;
    private TextView progressAlbumText;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (adapter != null) {
            outState.putParcelableArrayList(define.SAVE_INSTANCE_ALBUM_LIST, (ArrayList<? extends Parcelable>) adapter.getAlbumList());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(outState);
        // Restore state members from saved instance
        List<Album> albumList = outState.getParcelableArrayList(define.SAVE_INSTANCE_ALBUM_LIST);
        List<Uri> thumbList = outState.getParcelableArrayList(define.SAVE_INSTANCE_ALBUM_THUMB_LIST);

        if (albumList != null && thumbList != null && fishton.selectedImages != null) {
            adapter = new AlbumListAdapter();
            adapter.setAlbumList(albumList);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_album);
        initView();
        initController();
        if (albumController.checkPermission())
            albumController.getAlbumList(fishton.titleAlbumAllView, fishton.isExceptGif);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (recyclerAlbumList != null &&
                recyclerAlbumList.getLayoutManager() != null) {
            if (uiUtil.isLandscape(this))
                ((GridLayoutManager) recyclerAlbumList.getLayoutManager())
                        .setSpanCount(fishton.albumLandscapeSpanCount);
            else
                ((GridLayoutManager) recyclerAlbumList.getLayoutManager())
                        .setSpanCount(fishton.albumPortraitSpanCount);
        }
    }

    private void initView() {
        LinearLayout linearAlbumCamera = findViewById(R.id.lin_album_camera);
        linearAlbumCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (albumController.checkCameraPermission()) {
                    albumController.takePicture(AlbumActivity.this, albumController.getPathDir());
                }
            }
        });
        initToolBar();
    }


    private void initRecyclerView() {
        recyclerAlbumList = findViewById(R.id.recycler_album_list);

        GridLayoutManager layoutManager;
        if (uiUtil.isLandscape(this))
            layoutManager = new GridLayoutManager(this, fishton.albumLandscapeSpanCount);
        else
            layoutManager = new GridLayoutManager(this, fishton.albumPortraitSpanCount);

        if (recyclerAlbumList != null) {
            recyclerAlbumList.setLayoutManager(layoutManager);
        }
    }

    private void initToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar_album_bar);
        relAlbumEmpty = findViewById(R.id.rel_album_empty);
        progressAlbumText = findViewById(R.id.txt_album_msg);
        progressAlbumText.setText(R.string.msg_loading_image);

        setSupportActionBar(toolbar);

        toolbar.setBackgroundColor(fishton.colorActionBar);
        toolbar.setTitleTextColor(fishton.colorActionBarTitle);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            uiUtil.setStatusBarColor(this, fishton.colorStatusBar);
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(fishton.titleActionBar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (fishton.drawableHomeAsUpIndicator != null)
                getSupportActionBar().setHomeAsUpIndicator(fishton.drawableHomeAsUpIndicator);
        }

        if (fishton.isStatusBarLight
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            toolbar.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

    }

    private void initController() {
        albumController = new AlbumController(this);
    }

    private void setAlbumListAdapter() {
        if (adapter == null) {
            adapter = new AlbumListAdapter();
        }
        adapter.setAlbumList(albumList);
        recyclerAlbumList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        changeToolbarTitle();
    }

    protected void setAlbumList(ArrayList<Album> albumList) {
        this.albumList = albumList;
        if (albumList.size() > 0) {
            relAlbumEmpty.setVisibility(View.GONE);
            initRecyclerView();
            setAlbumListAdapter();
        } else {
            relAlbumEmpty.setVisibility(View.VISIBLE);
            progressAlbumText.setText(R.string.msg_no_image);
        }
    }

    private void refreshList(int position, ArrayList<Uri> imagePath) {
        if (imagePath.size() > 0) {
            if (position == 0) {
                albumController.getAlbumList(fishton.titleAlbumAllView, fishton.isExceptGif);
            } else {
                albumList.get(0).counter += imagePath.size();
                albumList.get(position).counter += imagePath.size();

                albumList.get(0).thumbnailPath = imagePath.get(imagePath.size() - 1).toString();
                albumList.get(position).thumbnailPath = imagePath.get(imagePath.size() - 1).toString();

                adapter.notifyItemChanged(0);
                adapter.notifyItemChanged(position);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (fishton.isButton) {
            getMenuInflater().inflate(R.menu.menu_photo_album, menu);
            MenuItem menuDoneItem = menu.findItem(R.id.action_done);
            menu.findItem(R.id.action_all_done).setVisible(false);
            if (fishton.drawableDoneButton != null) {
                menuDoneItem.setIcon(fishton.drawableDoneButton);
            } else if (fishton.strDoneMenu != null) {
                if (fishton.colorTextMenu != Integer.MAX_VALUE) {
                    SpannableString spanString = new SpannableString(fishton.strDoneMenu);
                    spanString.setSpan(new ForegroundColorSpan(fishton.colorTextMenu), 0, spanString.length(), 0); //fi
                    menuDoneItem.setTitle(spanString);
                } else {
                    menuDoneItem.setTitle(fishton.strDoneMenu);
                }
                menuDoneItem.setIcon(null);
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.action_done) {
            if (adapter != null) {
                if (fishton.selectedImages.size() < fishton.minCount) {
                    Snackbar.make(recyclerAlbumList, fishton.messageNothingSelected, Snackbar.LENGTH_SHORT).show();
                } else {
                    finishActivity();
                }
            }

        }
        return super.onOptionsItemSelected(item);
    }

    public void changeToolbarTitle() {
        if (adapter == null) return;
        int total = fishton.selectedImages.size();

        if (getSupportActionBar() != null) {
            if (fishton.maxCount == 1 || !fishton.isShowCount)
                getSupportActionBar().setTitle(fishton.titleActionBar);
            else
                getSupportActionBar().setTitle(fishton.titleActionBar + " (" + total + "/" + fishton.maxCount + ")");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == define.ENTER_ALBUM_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                finishActivity();
            } else if (resultCode == define.TRANS_IMAGES_RESULT_CODE) {
                ArrayList<Uri> addPath = data.getParcelableArrayListExtra(define.INTENT_ADD_PATH);
                int position = data.getIntExtra(define.INTENT_POSITION, -1);
                refreshList(position, addPath);
                changeToolbarTitle();
            }
        } else if (requestCode == define.TAKE_A_PICK_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                new SingleMediaScanner(this, new File(albumController.getSavePath()), new ScanListener() {
                    @Override
                    protected void onScanCompleted() {
                        albumController.getAlbumList(fishton.titleAlbumAllView, fishton.isExceptGif);
                    }
                });
            } else {
                new File(albumController.getSavePath()).delete();
            }
            changeToolbarTitle();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {


        switch (requestCode) {
            case 28: {
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // permission was granted, yay!
                        albumController.getAlbumList(fishton.titleAlbumAllView, fishton.isExceptGif);
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
                        albumController.takePicture(AlbumActivity.this, albumController.getPathDir());
                    } else {
                        new PermissionCheck(this).showPermissionDialog();
                    }
                }
                break;
            }
        }
    }


    private void finishActivity() {
        Intent i = new Intent();
        i.putParcelableArrayListExtra(Define.INTENT_PATH, fishton.selectedImages);
        setResult(RESULT_OK, i);
        finish();
    }
}
