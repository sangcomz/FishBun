package com.sangcomz.fishbun.model

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.*
import com.sangcomz.fishbun.MimeType
import com.sangcomz.fishbun.bean.Album
import com.sangcomz.fishbun.ext.equalsMimeType
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future

class ImageDataSourceImpl(private val contentResolver: ContentResolver) : ImageDataSource {

    private val executorService = Executors.newSingleThreadExecutor()

    override fun getAlbumList(
        allViewTitle: String,
        exceptMimeTypeList: List<MimeType>,
        specifyFolderList: List<String>
    ): Future<List<Album>> {
        return executorService.submit(Callable<List<Album>> {
            val albumHashMap = HashMap<Long, Album>()
            val orderBy = "$_ID DESC"
            val projection = arrayOf(_ID, BUCKET_DISPLAY_NAME, MIME_TYPE, BUCKET_ID)

            val c = contentResolver.query(EXTERNAL_CONTENT_URI, projection, null, null, orderBy)

            var totalCount = 0

            c?.let {
                val bucketMimeType = c.getColumnIndex(MIME_TYPE)
                val bucketColumn = c.getColumnIndex(BUCKET_DISPLAY_NAME)
                val bucketColumnId = c.getColumnIndex(BUCKET_ID)

                if (!isNotContainsSpecifyFolderList(specifyFolderList, allViewTitle))
                    albumHashMap[0] = Album(0, allViewTitle, null, 0)

                while (c.moveToNext()) {
                    val bucketDisplayName = c.getString(bucketColumn)
                    if (isExceptMemeType(exceptMimeTypeList, c.getString(bucketMimeType))
                        || isNotContainsSpecifyFolderList(specifyFolderList, bucketDisplayName)
                    ) continue
                    totalCount++
                    val bucketId = c.getInt(bucketColumnId).toLong()
                    val album = albumHashMap[bucketId]
                    if (album == null) {
                        val imgId = c.getInt(c.getColumnIndex(MediaStore.MediaColumns._ID))
                        val path = Uri.withAppendedPath(EXTERNAL_CONTENT_URI, "" + imgId)
                        albumHashMap[bucketId] =
                            Album(bucketId, bucketDisplayName, path.toString(), 1)

                        if (albumHashMap[0] != null && albumHashMap[0]?.thumbnailPath == null)
                            albumHashMap[0]?.thumbnailPath = path.toString()

                    } else {
                        album.counter++
                    }
                }
                val allAlbum = albumHashMap[0.toLong()]
                if (allAlbum != null) {
                    allAlbum.counter = totalCount
                }
                c.close()
            }

            if (totalCount == 0) albumHashMap.clear()

            val albumList = ArrayList<Album>()
            for (album in albumHashMap.values) {
                if (album.bucketId == 0L) albumList.add(0, album) else albumList.add(album)
            }
            albumList
        })

    }

    override fun getAllMediaThumbnailsPath(
        bucketId: Long,
        exceptMimeTypeList: List<MimeType>,
        specifyFolderList: List<String>
    ): Future<List<Uri>> {
        return executorService.submit(Callable<List<Uri>> {
            val imageUris = arrayListOf<Uri>()
            val selection = "$BUCKET_ID = ?"
            val bucketId: String = bucketId.toString()
            val sort = "$_ID DESC"
            val selectionArgs = arrayOf(bucketId)

            val images = EXTERNAL_CONTENT_URI
            val c = if (bucketId != "0") {
                contentResolver.query(images, null, selection, selectionArgs, sort)
            } else {
                contentResolver.query(images, null, null, null, sort)
            }
            c?.let {
                try {
                    if (c.moveToFirst()) {
                        do {
                            val mimeType = c.getString(c.getColumnIndex(MIME_TYPE))
                            val folderName = c.getString(c.getColumnIndex(BUCKET_DISPLAY_NAME))
                            if (isExceptMemeType(exceptMimeTypeList, mimeType)
                                || isNotContainsSpecifyFolderList(specifyFolderList, folderName)
                            ) continue
                            val imgId = c.getInt(c.getColumnIndex(MediaStore.MediaColumns._ID))
                            val path = Uri.withAppendedPath(
                                EXTERNAL_CONTENT_URI,
                                "" + imgId
                            )
                            imageUris.add(path)
                        } while (c.moveToNext())
                    }
                } finally {
                    if (!c.isClosed) c.close()
                }
            }
            imageUris
        })
    }

    override fun getDirectoryPath(bucketId: Long): Future<String> {
        return executorService.submit(Callable<String> {
            var path = ""
            val selection = "$BUCKET_ID = ?"
            val bucketId: String = bucketId.toString()
            val selectionArgs = arrayOf(bucketId)

            val images = EXTERNAL_CONTENT_URI
            val c = if (bucketId != "0") {
                contentResolver.query(images, null, selection, selectionArgs, null)
            } else {
                contentResolver.query(images, null, null, null, null)
            }
            c?.let {
                try {
                    if (c.moveToFirst()) {
                        path = getPathDir(
                            c.getString(c.getColumnIndex(DATA)),
                            c.getString(c.getColumnIndex(DISPLAY_NAME))
                        )
                    }
                } finally {
                    if (!c.isClosed) c.close()
                }
            }
            path
        })
    }


    private fun isExceptMemeType(
        mimeTypes: List<MimeType>,
        mimeType: String
    ): Boolean {
        for (type in mimeTypes) {
            if (type.equalsMimeType(mimeType)) return true
        }
        return false
    }

    private fun getPathDir(path: String, fileName: String): String {
        return path.replace("/$fileName", "")
    }

    private fun isNotContainsSpecifyFolderList(
        specifyFolderList: List<String>,
        displayBundleName: String
    ): Boolean {
        return if (specifyFolderList.isEmpty()) false
        else !specifyFolderList.contains(displayBundleName)
    }
}