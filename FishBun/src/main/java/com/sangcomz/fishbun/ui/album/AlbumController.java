package com.sangcomz.fishbun.ui.album;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.sangcomz.fishbun.bean.Album;
import com.sangcomz.fishbun.define.Define;
import com.sangcomz.fishbun.permission.PermissionCheck;
import com.sangcomz.fishbun.util.CameraUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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

    void getAlbumList() {
        new LoadAlbumList().execute();
    }

//    void getThumbnail(ArrayList<Album> albumList) {
//        new DisplayAlbumThumbnail(albumList).execute();
//    }

    private void setSpanCount(int albumListSize) {
        if (Define.ALBUM_LANDSCAPE_SPAN_COUNT > albumListSize)
            Define.ALBUM_LANDSCAPE_SPAN_COUNT = albumListSize;
        if (Define.ALBUM_PORTRAIT_SPAN_COUNT > albumListSize)
            Define.ALBUM_PORTRAIT_SPAN_COUNT = albumListSize;
    }

    private Uri getAllMediaThumbnailsPath(ContentResolver resolver, long id) {
        Uri path = null;
        String selection = MediaStore.Images.Media.BUCKET_ID + " = ?";
        String bucketId = String.valueOf(id);
        String sort = MediaStore.Images.Thumbnails._ID + " DESC";
        String[] selectionArgs = {bucketId};

        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor c;
        if (!bucketId.equals("0")) {
            c = resolver.query(images, null,
                    selection, selectionArgs, sort);
        } else {
            c = resolver.query(images, null,
                    null, null, sort);
        }
        if (c != null) {
            if (c.moveToNext()) {
                selection = MediaStore.Images.Media._ID + " = ?";
                String photoID = c.getString(c.getColumnIndex(MediaStore.Images.Media._ID));
                selectionArgs = new String[]{photoID};

                images = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;
                Cursor cursor = resolver.query(images, null,
                        selection, selectionArgs, sort);
                int imgId = c.getInt(c.getColumnIndex(MediaStore.MediaColumns._ID));
                path = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + imgId);
                System.out.println("path :::: " + path);
                if (cursor != null && cursor.moveToNext()) {
                    if (cursor.isLast())
                        cursor.close();
                }

            } else {
                Log.e("id", "from else");
            }
            c.close();
        }
        return path;
    }

    private class LoadAlbumList extends AsyncTask<Void, Void, ArrayList<Album>> {

        @Override
        protected ArrayList<Album> doInBackground(Void... params) {
            ArrayList<Album> albumList = new ArrayList<>();
            HashMap<Long, Album> albumHashMap = new HashMap<>();
            final String orderBy = MediaStore.Images.Media._ID + " DESC";
            String[] projection = new String[]{
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.Media.BUCKET_ID};

            Cursor c = resolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                    null, null, orderBy);

            long previousId = 0;
            int totalCounter = 0;
            if (c != null) {
                int bucketData = c
                        .getColumnIndex(MediaStore.Images.Media.DATA);
                int bucketColumn = c
                        .getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                int bucketColumnId = c
                        .getColumnIndex(MediaStore.Images.Media.BUCKET_ID);

                albumList = new ArrayList<>();
//                Album totalAlbum = new Album(0, Define.TITLE_ALBUM_ALL_VIEW, "", "", 0);

//                albumHashMap.put((long) 0, new Album(0, Define.TITLE_ALBUM_ALL_VIEW, "", null, 0));
                albumHashMap.put((long) 0, new Album(0, Define.TITLE_ALBUM_ALL_VIEW, null, 0));

//                albumList.add(totalAlbum);

                while (c.moveToNext()) {
                    totalCounter++;
                    long bucketId = c.getInt(bucketColumnId);

//                    if (previousId != bucketId) {
//                        int imgId = c.getInt(c.getColumnIndex(MediaStore.MediaColumns._ID));
//                        Uri path = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + imgId);
//                        System.out.println("path :::: " + path);
//                        Album album = new Album(bucketId, c.getString(bucketColumn), c.getString(bucketData), 1);
//                        albumList.add(album);
//                        previousId = bucketId;
//                    } else {
//                        if (albumList.size() > 0)
//                            albumList.get(albumList.size() - 1).counter++;
//                    }
                    Album album = albumHashMap.get(bucketId);
                    if (album == null) {
                        int imgId = c.getInt(c.getColumnIndex(MediaStore.MediaColumns._ID));

                        Uri path = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + imgId);
                        System.out.println("c.getString(bucketData) :::: " + c.getString(bucketData));
//                        albumHashMap.put(bucketId,
//                                new Album(bucketId,
//                                        c.getString(bucketColumn),
//                                        c.getString(bucketData), path.toString(), 1));

                        albumHashMap.put(bucketId,
                                new Album(bucketId,
                                        c.getString(bucketColumn),
                                        path.toString(), 1));

                        if (c.getPosition() == 0)
                            albumHashMap.get((long) 0).thumbnailPath = path.toString();
                    } else {
                        album.counter++;
                    }
                    if (c.isLast()) {
//                        albumList.get(0).counter = totalCounter;
                        albumHashMap.get((long) 0).counter = totalCounter;
                    }
                }
                c.close();
            }
            if (totalCounter == 0)
                albumList.clear();
            Iterator<Album> iterator = albumHashMap.values().iterator();
            do {
                Album album = iterator.next();
                if (album.bucketId == 0)
                    albumList.add(0, album);
                else
                    albumList.add(album);
            } while (iterator.hasNext());

            return albumList;
        }

        @Override
        protected void onPostExecute(ArrayList<Album> albumList) {
            super.onPostExecute(albumList);
            if (albumList.size() > 0) {
                setSpanCount(albumList.size());
            }
            albumActivity.setAlbumList(albumList);
        }
    }

//    private class DisplayAlbumThumbnail extends AsyncTask<Void, Void, List<Uri>> {
//        ArrayList<Album> albumList;
//
//        DisplayAlbumThumbnail(ArrayList<Album> albumList) {
//            this.albumList = albumList;
//        }
//
//        @Override
//        protected List<Uri> doInBackground(Void... params) {
//            List<Uri> thumbList = new ArrayList<>();
//            String pathDir = Environment.getExternalStoragePublicDirectory(
//                    Environment.DIRECTORY_DCIM + "/Camera").getAbsolutePath();
//            for (int i = 0; i < albumList.size(); i++) {
//                Album album = albumList.get(i);
//                Uri path = getAllMediaThumbnailsPath(resolver, album.bucketId);
//                if (path != null) {
//                    thumbList.add(path);
//                } else {
//                    thumbList.add(Uri.parse(""));
//                }
//                if (i != 0 && album.path.contains(pathDir))
//                    albumActivity.setDefCameraAlbum(i);
//            }
//            return thumbList;
//        }
//
//        @Override
//        protected void onPostExecute(List<Uri> thumbList) {
//            super.onPostExecute(thumbList);
//            albumActivity.setThumbnail(thumbList);
//        }
//
//    }

//    if (i != 0 && album.path.contains(pathDir))
//            albumActivity.setDefCameraAlbum(i);

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
}
