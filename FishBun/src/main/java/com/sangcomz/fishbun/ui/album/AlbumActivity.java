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
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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

import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.PublishSubject;


public class AlbumActivity extends AppCompatActivity {

    private List<Album> albumlist = new ArrayList<>();
    private RecyclerView recyclerView;
    private AlbumListAdapter adapter;
    private List<String> thumbList;
    private PermissionCheck permissionCheck;
    private UiUtil uiUtil = new UiUtil();
    private RelativeLayout noAlbum;
    private int defCameraAlbum = 0; //total

    public static PublishSubject<String> changeAlbumPublishSubject;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_album);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        noAlbum = (RelativeLayout) findViewById(R.id.no_album);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Define.ACTIONBAR_COLOR);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            uiUtil.setStatusBarColor(this);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(linearLayoutManager);
        }
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        permissionCheck = new PermissionCheck(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionCheck.CheckStoragePermission())
                new DisplayImage().execute();
        } else
            new DisplayImage().execute();

        changeAlbumPublishSubject = PublishSubject.create();

        Subscription changeAlbumSubscription =
                changeAlbumPublishSubject.subscribe(new Action1<String>() {
                    int position;

                    @Override
                    public void call(String imagePath) {
                        if (imagePath.split("[|]")[0].equals("POSITION")) {
                            position = Integer.parseInt(imagePath.split("[|]")[1]);
                        } else if (imagePath.split("[|]")[0].equals("PATH")) {
                            if (position == 0) {
                                albumlist.get(position).counter++;
                                albumlist.get(defCameraAlbum).counter++;

                                thumbList.set(position, imagePath.split("[|]")[1]);
                                thumbList.set(defCameraAlbum, imagePath.split("[|]")[1]);

                                adapter.notifyItemChanged(0);
                                adapter.notifyItemChanged(defCameraAlbum);
                            } else {
                                albumlist.get(0).counter++;
                                albumlist.get(position).counter++;

                                thumbList.set(0, imagePath.split("[|]")[1]);
                                thumbList.set(position, imagePath.split("[|]")[1]);

                                adapter.notifyItemChanged(0);
                                adapter.notifyItemChanged(position);
                            }

                        }
                    }
                });


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
                adapter.setPath(path);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Define.PERMISSION_STORAGE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    new DisplayImage().execute();
                    // permission was granted, yay! do the
                    // calendar task you need to do.
                } else {
                    permissionCheck.showPermissionDialog();
                    finish();
                }
                return;
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

            Cursor imagecursor = resolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                    null, null, orderBy);

            long previousid = 0;

            int bucketColumn = imagecursor
                    .getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

            int bucketcolumnid = imagecursor
                    .getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
            albumlist = new ArrayList<Album>();
            Album totalAlbum = new Album();
            totalAlbum.bucketid = 0;
            totalAlbum.bucketname = getString(R.string.str_all_view);
            totalAlbum.counter = 0;
            albumlist.add(totalAlbum);
            int totalCounter = 0;

            while (imagecursor.moveToNext()) {
                totalCounter++;
                long bucketid = imagecursor.getInt(bucketcolumnid);
                if (previousid != bucketid) {
                    Album album = new Album();
                    album.bucketid = bucketid;
                    album.bucketname = imagecursor.getString(bucketColumn);
                    album.counter++;
                    albumlist.add(album);
                    previousid = bucketid;

                } else {
                    if (albumlist.size() > 0)
                        albumlist.get(albumlist.size() - 1).counter++;
                }
                if (imagecursor.isLast()) {
                    albumlist.get(0).counter = totalCounter;
                }
            }
            imagecursor.close();
            if (totalCounter == 0) {
                albumlist.clear();
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
                adapter = new AlbumListAdapter(albumlist, getIntent().getStringArrayListExtra(Define.INTENT_PATH));
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                new DisplayThumbnail().execute();
            } else {
                noAlbum.setVisibility(View.VISIBLE);
            }
        }
    }

    public class DisplayThumbnail extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            thumbList = new ArrayList<String>();
        }

        @Override
        protected Void doInBackground(Void... params) {

            String pathDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM + "/Camera").getAbsolutePath();
            for (int i = 0; i < albumlist.size(); i++) {
                Album album = albumlist.get(i);
                String path = getAllMediaThumbnailsPath(album.bucketid);
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


        if (c.moveToNext()) {
            selection = MediaStore.Images.Media._ID + " = ?";
            String photoID = c.getString(c.getColumnIndex(MediaStore.Images.Media._ID));
            selectionArgs = new String[]{photoID};

            images = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;
            Cursor cursor = getContentResolver().query(images, null,
                    selection, selectionArgs, sort);
            if (cursor != null && cursor.moveToNext()) {
                path = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
            } else
                path = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();
        } else {
            Log.e("id", "from else");
        }


        c.close();
        return path;
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
            if (adapter.getPath().size() == 0) {
//                Toast.makeText(this, getString(R.string.msg_no_slected), Toast.LENGTH_SHORT).show();
                Snackbar.make(recyclerView, Define.MESSAGE_NOTHING_SELECTED, Snackbar.LENGTH_SHORT).show();
            } else {
                Intent i = new Intent();
                i.putStringArrayListExtra(Define.INTENT_PATH, adapter.getPath());
                setResult(RESULT_OK, i);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }


}
