package com.sangcomz.fishbun.ui.album;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.sangcomz.fishbun.ItemDecoration.DividerItemDecoration;
import com.sangcomz.fishbun.R;
import com.sangcomz.fishbun.adapter.AlbumListAdapter;
import com.sangcomz.fishbun.bean.Album;
import com.sangcomz.fishbun.define.Define;
import com.sangcomz.fishbun.permission.PermissionCheck;
import com.sangcomz.fishbun.util.UiUtil;

import java.util.ArrayList;
import java.util.List;


public class AlbumActivity extends AppCompatActivity {
    private AlbumController albumController;
    private List<Album> albumList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AlbumListAdapter adapter;
    private UiUtil uiUtil = new UiUtil();
    private RelativeLayout noAlbum;
    private int defCameraAlbum = 0;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (adapter != null) {
            outState.putStringArrayList(Define.SAVE_INSTANCE_PICK_IMAGES, adapter.getPickedImagePath());
            outState.putParcelableArrayList(Define.SAVE_INSTANCE_ALBUM_LIST, (ArrayList<? extends Parcelable>) adapter.getAlbumList());
            outState.putStringArrayList(Define.SAVE_INSTANCE_ALBUM_THUMB_LIST,
                    (ArrayList<String>) adapter.getThumbList());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(outState);
        // Restore state members from saved instance
        List<Album> albumList = outState.getParcelableArrayList(Define.SAVE_INSTANCE_ALBUM_LIST);
        List<String> thumbList = outState.getStringArrayList(Define.SAVE_INSTANCE_ALBUM_THUMB_LIST);
        ArrayList<String> pickedImagePath = outState.getStringArrayList(Define.SAVE_INSTANCE_PICK_IMAGES);

        if (albumList != null && thumbList != null && pickedImagePath != null) {
            adapter = new AlbumListAdapter(albumList, pickedImagePath);
            adapter.setThumbList(thumbList);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_album);
        initController();
        initToolBar();
        if (albumController.checkPermission())
            new DisplayImage().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (recyclerView != null &&
                recyclerView.getLayoutManager() != null) {
            if (UiUtil.isLandscape(this))
                ((GridLayoutManager) recyclerView.getLayoutManager())
                        .setSpanCount(Define.ALBUM_LANDSCAPE_SPAN_COUNT);
            else
                ((GridLayoutManager) recyclerView.getLayoutManager())
                        .setSpanCount(Define.ALBUM_PORTRAIT_SPAN_COUNT);
        }
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        GridLayoutManager layoutManager;
        if (UiUtil.isLandscape(this))
            layoutManager = new GridLayoutManager(this, Define.ALBUM_LANDSCAPE_SPAN_COUNT);
        else
            layoutManager = new GridLayoutManager(this, Define.ALBUM_PORTRAIT_SPAN_COUNT);

        if (recyclerView != null) {
            recyclerView.setLayoutManager(layoutManager);
        }
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        noAlbum = (RelativeLayout) findViewById(R.id.no_album);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Define.ACTIONBAR_COLOR);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            uiUtil.setStatusBarColor(this);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initController() {
        albumController = new AlbumController(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (Define.IS_BUTTON)
            getMenuInflater().inflate(R.menu.menu_photo_album, menu);
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
                    Snackbar.make(recyclerView, Define.MESSAGE_NOTHING_SELECTED, Snackbar.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent();
                    i.putStringArrayListExtra(Define.INTENT_PATH, adapter.getPickedImagePath());
                    setResult(RESULT_OK, i);
                    finish();
                }
            }

        }
        return super.onOptionsItemSelected(item);
    }


    private void refreshList(int position, ArrayList<String> imagePath) {
        List<String> thumbList = adapter.getThumbList();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Define.ENTER_ALBUM_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK, data);
                finish();
            } else if (resultCode == Define.TRANS_IMAGES_RESULT_CODE) {
                ArrayList<String> path = data.getStringArrayListExtra(Define.INTENT_PATH);
                ArrayList<String> addPath = data.getStringArrayListExtra(Define.INTENT_ADD_PATH);
                int position = data.getIntExtra(Define.INTENT_POSITION, -1);
                refreshList(position, addPath);
                if (adapter != null)
                    adapter.setPickedImagePath(path);
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
                        new DisplayImage().execute();
                    } else {
                        new PermissionCheck(this).showPermissionDialog();
                        finish();
                    }
                }
            }
        }
    }


    public class DisplayImage extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            final String orderBy = MediaStore.Images.Media.BUCKET_ID;
            final ContentResolver resolver = getContentResolver();
            String[] projection = new String[]{
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.Media.BUCKET_ID};

            Cursor imageCursor = resolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                    null, null, orderBy);

            long previousId = 0;
            int totalCounter = 0;
            if (imageCursor != null) {
                int bucketColumn = imageCursor
                        .getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

                int bucketColumnId = imageCursor
                        .getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
                albumList = new ArrayList<>();
                Album totalAlbum = new Album(0, getString(R.string.str_all_view), 0);

                albumList.add(totalAlbum);


                while (imageCursor.moveToNext()) {
                    totalCounter++;
                    long bucketId = imageCursor.getInt(bucketColumnId);
                    if (previousId != bucketId) {
                        Album album = new Album(bucketId, imageCursor.getString(bucketColumn), 1);
                        albumList.add(album);
                        previousId = bucketId;
                    } else {
                        if (albumList.size() > 0)
                            albumList.get(albumList.size() - 1).counter++;
                    }

                    if (imageCursor.isLast()) {
                        albumList.get(0).counter = totalCounter;
                    }
                }
                imageCursor.close();
            }

            if (totalCounter == 0) {
                albumList.clear();
                return false;
            } else {
                return true;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                noAlbum.setVisibility(View.GONE);
                albumController.setSpanCount(albumList.size());
                initRecyclerView();
                if (adapter == null)
                    adapter = new AlbumListAdapter(albumList, getIntent().getStringArrayListExtra(Define.INTENT_PATH));
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                new DisplayThumbnail().execute();
            } else {
                noAlbum.setVisibility(View.VISIBLE);
            }
        }
    }

    public class DisplayThumbnail extends AsyncTask<Void, Void, Void> {
        List<String> thumbList = new ArrayList<>();

        @Override
        protected Void doInBackground(Void... params) {

            String pathDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM + "/Camera").getAbsolutePath();
            for (int i = 0; i < albumList.size(); i++) {
                Album album = albumList.get(i);
                String path = getAllMediaThumbnailsPath(album.bucketId);
                thumbList.add(path);
                if (i != 0 && path.contains(pathDir))
                    defCameraAlbum = i;


            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            adapter.setThumbList(thumbList);
        }

    }


    private String getAllMediaThumbnailsPath(long id) {
        String path = "";
        String selection = MediaStore.Images.Media.BUCKET_ID + " = ?";
        String bucketid = String.valueOf(id);
        String sort = MediaStore.Images.Thumbnails._ID + " DESC";
        String[] selectionArgs = {bucketid};

        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor c;
        if (!bucketid.equals("0")) {
            c = getContentResolver().query(images, null,
                    selection, selectionArgs, sort);
        } else {
            c = getContentResolver().query(images, null,
                    null, null, sort);
        }
        if (c != null) {
            if (c.moveToNext()) {
                selection = MediaStore.Images.Media._ID + " = ?";
                String photoID = c.getString(c.getColumnIndex(MediaStore.Images.Media._ID));
                selectionArgs = new String[]{photoID};

                images = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;
                Cursor cursor = getContentResolver().query(images, null,
                        selection, selectionArgs, sort);
                if (cursor != null && cursor.moveToNext()) {
                    path = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
                    if (cursor.isLast())
                        cursor.close();
                } else
                    path = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));

            } else {
                Log.e("id", "from else");
            }
            c.close();
        }


        return path;
    }


}
