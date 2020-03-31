package com.sangcomz.fishbun.ui.album;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import com.sangcomz.fishbun.MimeType;
import com.sangcomz.fishbun.bean.Album;
import com.sangcomz.fishbun.ext.MimeTypeExt;
import com.sangcomz.fishbun.permission.PermissionCheck;
import com.sangcomz.fishbun.util.CameraUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class AlbumController {

    private AlbumActivity albumActivity;
    private ContentResolver resolver;
    private CameraUtil cameraUtil = new CameraUtil();


    AlbumController(AlbumActivity albumActivity) {
        this.albumActivity = albumActivity;
        this.resolver = albumActivity.getContentResolver();
    }


    boolean checkPermission() {
        PermissionCheck permissionCheck = new PermissionCheck(albumActivity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionCheck.CheckStoragePermission())
                return true;
        } else
            return true;
        return false;
    }

    boolean checkCameraPermission() {
        PermissionCheck permissionCheck = new PermissionCheck(albumActivity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionCheck.CheckCameraPermission())
                return true;
        } else
            return true;
        return false;
    }

    void getAlbumList(String allViewTitle,
                      List<MimeType> exceptMimeTypeList,
                      List<String> specifyFolderList) {
        new LoadAlbumList(allViewTitle, exceptMimeTypeList, specifyFolderList).execute();
    }

    private class LoadAlbumList extends AsyncTask<Void, Void, List<Album>> {

        String allViewTitle;
        List<MimeType> exceptMimeTypeList;
        List<String> specifyFolderList;

        LoadAlbumList(String allViewTitle,
                      List<MimeType> exceptMimeTypeList,
                      List<String> specifyFolderList) {
            this.allViewTitle = allViewTitle;
            this.exceptMimeTypeList = exceptMimeTypeList;
            this.specifyFolderList = specifyFolderList;
        }

        @Override
        protected List<Album> doInBackground(Void... params) {
            HashMap<Long, Album> albumHashMap = new HashMap<>();
            final String orderBy = MediaStore.Images.Media._ID + " DESC";
            String[] projection = new String[]{
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.Media.MIME_TYPE,
                    MediaStore.Images.Media.BUCKET_ID};

            Cursor c = resolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                    null, null, orderBy);

            int totalCounter = 0;
            if (c != null) {
                int bucketMimeType = c.getColumnIndex(MediaStore.Images.Media.MIME_TYPE);
                int bucketColumn = c
                        .getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                int bucketColumnId = c
                        .getColumnIndex(MediaStore.Images.Media.BUCKET_ID);

                if (!isNotContainsSpecifyFolderList(specifyFolderList, allViewTitle)) {
                    albumHashMap.put((long) 0, new Album(0, allViewTitle, null, 0));
                }

                while (c.moveToNext()) {
                    String mimeType = c.getString(bucketMimeType);
                    String folderName = c.getString(bucketColumn);

                    if (isExceptMemeType(exceptMimeTypeList, mimeType)
                            || isNotContainsSpecifyFolderList(specifyFolderList, folderName))
                        continue;

                    totalCounter++;
                    long bucketId = c.getInt(bucketColumnId);
                    Album album = albumHashMap.get(bucketId);
                    if (album == null) {
                        int imgId = c.getInt(c.getColumnIndex(MediaStore.MediaColumns._ID));
                        Uri path = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + imgId);
                        albumHashMap.put(bucketId, new Album(bucketId, folderName, path.toString(), 1));
                        if (albumHashMap.get(0L) != null
                                && albumHashMap.get(0L).thumbnailPath == null)
                            albumHashMap.get(0L).thumbnailPath = path.toString();
                    } else {
                        album.counter++;
                    }
                }
                Album allAlbum = albumHashMap.get((long) 0);
                if (allAlbum != null) {
                    allAlbum.counter = totalCounter;
                }
                c.close();
            }

            if (totalCounter == 0)
                albumHashMap.clear();

            ArrayList<Album> albumList = new ArrayList<>();
            for (Album album : albumHashMap.values()) {
                if (album.bucketId == 0)
                    albumList.add(0, album);
                else
                    albumList.add(album);
            }
            return albumList;
        }

        @Override
        protected void onPostExecute(List<Album> albumList) {
            super.onPostExecute(albumList);
            albumActivity.setAlbumList(albumList);
        }
    }

    void takePicture(Activity activity, String saveDir) {
        cameraUtil.takePicture(activity, saveDir);
    }

    String getSavePath() {
        return cameraUtil.getSavePath();
    }


    String getPathDir() {
        return Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM + "/Camera").getAbsolutePath();
    }

    private boolean isExceptMemeType(List<MimeType> mimeTypes, String mimeType) {
        for (MimeType type : mimeTypes) {
            if (MimeTypeExt.equalsMimeType(type, mimeType))
                return true;
        }
        return false;
    }

    private boolean isNotContainsSpecifyFolderList(List<String> specifyFolderList, String displayBundleName) {
        if (specifyFolderList.isEmpty()) return false;
        return !specifyFolderList.contains(displayBundleName);
    }
}
