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
import com.sangcomz.fishbun.util.SingleMediaScanner;
import com.sangcomz.fishbun.util.UiUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

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

        if (albumList != null && thumbList != null && fishton.getSelectedImages() != null) {
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
            albumController.getAlbumList(fishton.getTitleAlbumAllView(), fishton.isExceptGif());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (recyclerAlbumList != null &&
                recyclerAlbumList.getLayoutManager() != null) {
            if (UiUtil.isLandscape(this))
                ((GridLayoutManager) recyclerAlbumList.getLayoutManager())
                        .setSpanCount(fishton.getAlbumLandscapeSpanCount());
            else
                ((GridLayoutManager) recyclerAlbumList.getLayoutManager())
                        .setSpanCount(fishton.getAlbumPortraitSpanCount());
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
        if (UiUtil.isLandscape(this))
            layoutManager = new GridLayoutManager(this, fishton.getAlbumLandscapeSpanCount());
        else
            layoutManager = new GridLayoutManager(this, fishton.getAlbumPortraitSpanCount());

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

        toolbar.setBackgroundColor(fishton.getColorActionBar());
        toolbar.setTitleTextColor(fishton.getColorActionBarTitle());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UiUtil.setStatusBarColor(this, fishton.getColorStatusBar());
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(fishton.getTitleActionBar());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (fishton.getDrawableHomeAsUpIndicator() != null)
                getSupportActionBar().setHomeAsUpIndicator(fishton.getDrawableHomeAsUpIndicator());
        }

        if (fishton.isStatusBarLight()
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
                albumController.getAlbumList(fishton.getTitleAlbumAllView(), fishton.isExceptGif());
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
        if (fishton.isButton()) {
            getMenuInflater().inflate(R.menu.menu_photo_album, menu);
            MenuItem menuDoneItem = menu.findItem(R.id.action_done);
            menu.findItem(R.id.action_all_done).setVisible(false);
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
                if (fishton.getSelectedImages().size() < fishton.getMinCount()) {
                    Snackbar.make(recyclerAlbumList, fishton.getMessageNothingSelected(), Snackbar.LENGTH_SHORT).show();
                } else {
                    finishActivity();
                }
            }

        }
        return super.onOptionsItemSelected(item);
    }

    public void changeToolbarTitle() {
        if (adapter == null) return;
        int total = fishton.getSelectedImages().size();

        if (getSupportActionBar() != null) {
            if (fishton.getMaxCount() == 1 || !fishton.isShowCount())
                getSupportActionBar().setTitle(fishton.getTitleActionBar());
            else
                getSupportActionBar().setTitle(fishton.getTitleActionBar() + " (" + total + "/" + fishton.getMaxCount() + ")");
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
                new SingleMediaScanner(this, new File(albumController.getSavePath()), new Function0<Unit>() {
                    @Override
                    public Unit invoke() {
                        albumController.getAlbumList(fishton.getTitleAlbumAllView(), fishton.isExceptGif());
                        return Unit.INSTANCE;
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
                        albumController.getAlbumList(fishton.getTitleAlbumAllView(), fishton.isExceptGif());
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
        i.putParcelableArrayListExtra(Define.INTENT_PATH, fishton.getSelectedImages());
        setResult(RESULT_OK, i);
        finish();
    }
}
