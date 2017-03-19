package com.sangcomz.fishbun.ui.album;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sangcomz.fishbun.ItemDecoration.DividerItemDecoration;
import com.sangcomz.fishbun.R;
import com.sangcomz.fishbun.adapter.AlbumListAdapter;
import com.sangcomz.fishbun.bean.Album;
import com.sangcomz.fishbun.define.Define;
import com.sangcomz.fishbun.permission.PermissionCheck;
import com.sangcomz.fishbun.util.ScanListener;
import com.sangcomz.fishbun.util.SingleMediaScanner;
import com.sangcomz.fishbun.util.UiUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.sangcomz.fishbun.define.Define.homeAsUpIndicatorDrawable;
import static com.sangcomz.fishbun.define.Define.okButtonDrawable;


public class AlbumActivity extends AppCompatActivity {
    private AlbumController albumController;
    private ArrayList<Album> albumList = new ArrayList<>();

    private RecyclerView recyclerAlbumList;
    private RelativeLayout relAlbumEmpty;

    private AlbumListAdapter adapter;
    private UiUtil uiUtil = new UiUtil();

    private TextView progressAlbumText;

    private int defCameraAlbum = 0;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (adapter != null) {
            outState.putParcelableArrayList(Define.SAVE_INSTANCE_PICK_IMAGES, adapter.getPickedImagePath());
            outState.putParcelableArrayList(Define.SAVE_INSTANCE_ALBUM_LIST, (ArrayList<? extends Parcelable>) adapter.getAlbumList());
            outState.putParcelableArrayList(Define.SAVE_INSTANCE_ALBUM_THUMB_LIST, (ArrayList<Uri>) adapter.getThumbList());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(outState);
        // Restore state members from saved instance
        List<Album> albumList = outState.getParcelableArrayList(Define.SAVE_INSTANCE_ALBUM_LIST);
        List<Uri> thumbList = outState.getParcelableArrayList(Define.SAVE_INSTANCE_ALBUM_THUMB_LIST);
        ArrayList<Uri> pickedImagePath = outState.getParcelableArrayList(Define.SAVE_INSTANCE_PICK_IMAGES);

        if (albumList != null && thumbList != null && pickedImagePath != null) {
            adapter = new AlbumListAdapter(albumList, pickedImagePath);
            adapter.setThumbList(thumbList);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_album);
        initView();
        initController();
        if (albumController.checkPermission())
            albumController.getAlbumList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (recyclerAlbumList != null &&
                recyclerAlbumList.getLayoutManager() != null) {
            if (uiUtil.isLandscape(this))
                ((GridLayoutManager) recyclerAlbumList.getLayoutManager())
                        .setSpanCount(Define.ALBUM_LANDSCAPE_SPAN_COUNT);
            else
                ((GridLayoutManager) recyclerAlbumList.getLayoutManager())
                        .setSpanCount(Define.ALBUM_PORTRAIT_SPAN_COUNT);
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
            layoutManager = new GridLayoutManager(this, Define.ALBUM_LANDSCAPE_SPAN_COUNT);
        else
            layoutManager = new GridLayoutManager(this, Define.ALBUM_PORTRAIT_SPAN_COUNT);

        if (recyclerAlbumList != null) {
            recyclerAlbumList.setLayoutManager(layoutManager);
        }
        recyclerAlbumList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_album_bar);
        relAlbumEmpty = (RelativeLayout) findViewById(R.id.rel_album_empty);
        progressAlbumText = (TextView) findViewById(R.id.txt_album_msg);
        progressAlbumText.setText(R.string.msg_loading_image);

        setSupportActionBar(toolbar);

        toolbar.setBackgroundColor(Define.COLOR_ACTION_BAR);
        toolbar.setTitleTextColor(Define.COLOR_ACTION_BAR_TITLE_COLOR);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            uiUtil.setStatusBarColor(this);
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(Define.TITLE_ACTIONBAR);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (homeAsUpIndicatorDrawable != null)
                getSupportActionBar().setHomeAsUpIndicator(homeAsUpIndicatorDrawable);
        }

        if (Define.STYLE_STATUS_BAR_LIGHT
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            toolbar.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }

    }

    private void initController() {
        albumController = new AlbumController(this);
    }

    private void setAlbumListAdapter() {

        if (adapter == null) {
            ArrayList<Uri> data = getIntent().getParcelableArrayListExtra(Define.INTENT_PATH);
            adapter = new AlbumListAdapter(albumList, data);
        }
        recyclerAlbumList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    protected void setDefCameraAlbum(int position) {
        defCameraAlbum = position;
    }

    protected void setThumbnail(List<Uri> thumbList) {
        adapter.setThumbList(thumbList);
    }


    protected void setAlbumList(ArrayList<Album> albumList) {
        this.albumList = albumList;
        if (albumList.size() > 0) {
            relAlbumEmpty.setVisibility(View.GONE);
            initRecyclerView();
            setAlbumListAdapter();
            albumController.getThumbnail(albumList);
        } else {
            relAlbumEmpty.setVisibility(View.VISIBLE);
            progressAlbumText.setText(R.string.msg_no_image);
        }
    }

    private void refreshList(int position, ArrayList<Uri> imagePath) {
        List<Uri> thumbList = adapter.getThumbList();
        if (imagePath.size() > 0) {
            if (position == 0) {
                albumList.get(position).counter += imagePath.size();
                albumList.get(defCameraAlbum).counter += imagePath.size();

                thumbList.set(position, imagePath.get(imagePath.size() - 1));
                thumbList.set(defCameraAlbum, imagePath.get(imagePath.size() - 1));

                adapter.notifyItemChanged(0);
                adapter.notifyItemChanged(defCameraAlbum);
            } else {
                albumList.get(0).counter += imagePath.size();
                albumList.get(position).counter += imagePath.size();

                thumbList.set(0, imagePath.get(imagePath.size() - 1));
                thumbList.set(position, imagePath.get(imagePath.size() - 1));

                adapter.notifyItemChanged(0);
                adapter.notifyItemChanged(position);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (Define.IS_BUTTON) {
            getMenuInflater().inflate(R.menu.menu_photo_album, menu);
            if (okButtonDrawable != null)
                menu.findItem(R.id.action_ok).setIcon(okButtonDrawable);
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
                if (adapter.getPickedImagePath().size() == 0) {
                    Snackbar.make(recyclerAlbumList, Define.MESSAGE_NOTHING_SELECTED, Snackbar.LENGTH_SHORT).show();
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Define.ENTER_ALBUM_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK, data);
                finish();
            } else if (resultCode == Define.TRANS_IMAGES_RESULT_CODE) {
                ArrayList<Uri> path = data.getParcelableArrayListExtra(Define.INTENT_PATH);
                ArrayList<Uri> addPath = data.getParcelableArrayListExtra(Define.INTENT_ADD_PATH);
                int position = data.getIntExtra(Define.INTENT_POSITION, -1);
                refreshList(position, addPath);
                if (adapter != null)
                    adapter.setPickedImagePath(path);
            }
        } else if (requestCode == Define.TAKE_A_PICK_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                new SingleMediaScanner(this, new File(albumController.getSavePath()), new ScanListener() {
                    @Override
                    protected void onScanCompleted() {
                        albumController.getAlbumList();
                    }
                });
            } else {
                new File(albumController.getSavePath()).delete();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case Define.PERMISSION_STORAGE: {
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // permission was granted, yay!
                        albumController.getAlbumList();
                    } else {
                        new PermissionCheck(this).showPermissionDialog();
                        finish();
                    }
                }
            }
        }
    }
}
