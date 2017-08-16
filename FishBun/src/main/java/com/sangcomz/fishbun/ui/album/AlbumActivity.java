package com.sangcomz.fishbun.ui.album;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sangcomz.fishbun.BaseActivity;
import com.sangcomz.fishbun.BaseParams;
import com.sangcomz.fishbun.R;
import com.sangcomz.fishbun.adapter.AlbumListAdapter;
import com.sangcomz.fishbun.bean.Album;
import com.sangcomz.fishbun.define.Define;
import com.sangcomz.fishbun.permission.PermissionCheck;
import com.sangcomz.fishbun.util.ScanListener;
import com.sangcomz.fishbun.util.SingleMediaScanner;
import com.sangcomz.fishbun.util.TextDrawable;

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
            outState.putParcelableArrayList(define.SAVE_INSTANCE_PICK_IMAGES, adapter.getPickedImagePath());
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
        ArrayList<Uri> pickedImagePath = outState.getParcelableArrayList(define.SAVE_INSTANCE_PICK_IMAGES);

        if (albumList != null && thumbList != null && pickedImagePath != null) {
            adapter = new AlbumListAdapter(pickedImagePath,
                    albumSize,
                    getIntent().getExtras());
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
            albumController.getAlbumList(titleAllView, exceptGif);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (recyclerAlbumList != null &&
                recyclerAlbumList.getLayoutManager() != null) {
            if (uiUtil.isLandscape(this))
                ((GridLayoutManager) recyclerAlbumList.getLayoutManager())
                        .setSpanCount(albumLandScapeSize);
            else
                ((GridLayoutManager) recyclerAlbumList.getLayoutManager())
                        .setSpanCount(albumPortraitSize);
        }
    }

    private void initView() {
        LinearLayout linearAlbumCamera = (LinearLayout) findViewById(R.id.lin_album_camera);
        linearAlbumCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                albumController.takePicture(AlbumActivity.this, albumController.getPathDir());
            }
        });
        initToolBar();
    }


    private void initRecyclerView() {
        recyclerAlbumList = (RecyclerView) findViewById(R.id.recycler_album_list);

        GridLayoutManager layoutManager;
        if (uiUtil.isLandscape(this))
            layoutManager = new GridLayoutManager(this, albumLandScapeSize);
        else
            layoutManager = new GridLayoutManager(this, albumPortraitSize);

        if (recyclerAlbumList != null) {
            recyclerAlbumList.setLayoutManager(layoutManager);
        }
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_album_bar);
        relAlbumEmpty = (RelativeLayout) findViewById(R.id.rel_album_empty);
        progressAlbumText = (TextView) findViewById(R.id.txt_album_msg);
        progressAlbumText.setText(R.string.msg_loading_image);

        setSupportActionBar(toolbar);

        toolbar.setBackgroundColor(colorActionBar);
        toolbar.setTitleTextColor(colorActionBarTitle);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            uiUtil.setStatusBarColor(this, colorStatusBar);
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(titleActionBar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (homeAsUpIndicatorDrawable != null)
                getSupportActionBar().setHomeAsUpIndicator(homeAsUpIndicatorDrawable);
        }

        if (statusBarLight
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            toolbar.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

    }

    private void initController() {
        albumController = new AlbumController(this);
    }

    private void setAlbumListAdapter() {
        if (adapter == null) {
            ArrayList<Uri> data = getIntent().getParcelableArrayListExtra(BaseParams.ARRAY_PATHS.name());
            adapter = new AlbumListAdapter(data,
                    albumSize,
                    getIntent().getExtras());
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
                albumController.getAlbumList(titleAllView, exceptGif);
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
        if (isButton) {
            getMenuInflater().inflate(R.menu.menu_photo_album, menu);
            MenuItem item = menu.findItem(R.id.action_ok);
            if (okButtonDrawable != null) {
                item.setIcon(okButtonDrawable);
            } else if (menuText != null) {
                if (colorMenuText != Integer.MAX_VALUE) {
                    item.setIcon(new TextDrawable(getResources(), menuText, colorMenuText));
                } else {
                    item.setTitle(menuText);
                    item.setIcon(null);
                }
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.action_ok) {
            if (adapter != null) {
                if (adapter.getPickedImagePath().size() < minCount) {
                    Snackbar.make(recyclerAlbumList, messageNothingSelected, Snackbar.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent();
                    i.putParcelableArrayListExtra(Define.INTENT_PATH, adapter.getPickedImagePath());
                    setResult(RESULT_OK, i);
                    finish();
                }
            }

        }
        return super.onOptionsItemSelected(item);
    }

    public void changeToolbarTitle() {
        if (adapter == null) return;
        int total = adapter.getPickedImagePath().size();

        if (getSupportActionBar() != null) {
            if (maxCount == 1)
                getSupportActionBar().setTitle(titleActionBar);
            else
                getSupportActionBar().setTitle(titleActionBar + "(" + String.valueOf(total) + "/" + maxCount + ")");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == define.ENTER_ALBUM_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK, data);
                finish();
            } else if (resultCode == define.TRANS_IMAGES_RESULT_CODE) {
                ArrayList<Uri> path = data.getParcelableArrayListExtra(Define.INTENT_PATH);
                ArrayList<Uri> addPath = data.getParcelableArrayListExtra(define.INTENT_ADD_PATH);
                int position = data.getIntExtra(define.INTENT_POSITION, -1);
                refreshList(position, addPath);
                if (adapter != null)
                    adapter.setPickedImagePath(path);

                changeToolbarTitle();
            }
        } else if (requestCode == define.TAKE_A_PICK_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                new SingleMediaScanner(this, new File(albumController.getSavePath()), new ScanListener() {
                    @Override
                    protected void onScanCompleted() {
                        albumController.getAlbumList(titleAllView, exceptGif);
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
                        albumController.getAlbumList(titleAllView, exceptGif);
                    } else {
                        new PermissionCheck(this).showPermissionDialog();
                        finish();
                    }
                }
            }
        }

    }
}
