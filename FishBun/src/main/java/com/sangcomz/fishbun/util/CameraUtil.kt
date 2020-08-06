package com.sangcomz.fishbun.util

import android.annotation.TargetApi
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by sangcomz on 16/01/2017.
 */
class CameraUtil {
    var savedPath: String? = null

    fun takePicture(activity: Activity, saveDir: String, requestCode: Int) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(activity.packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile(saveDir) //make a file
                savedPath = photoFile.absolutePath
            } catch (ex: IOException) {
                ex.printStackTrace()
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                val uri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    FileProvider.getUriForFile(
                        activity,
                        activity.applicationContext.packageName + ".fishbunfileprovider",
                        photoFile
                    )
                } else {
                    Uri.fromFile(photoFile)
                }
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                activity.startActivityForResult(takePictureIntent, requestCode)
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.Q)
    fun saveImageForAndroidQOrHigher(contentResolver: ContentResolver, file: File) {
        val values = ContentValues()
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, file.name)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        values.put(MediaStore.Audio.Media.IS_PENDING, 1)

        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)?.let {
            contentResolver.openFileDescriptor(it, "w").use { parcelFileDescriptor ->
                val fileDescriptor = parcelFileDescriptor?.fileDescriptor ?: return@use

                FileOutputStream(fileDescriptor).use { fileOutputStream: FileOutputStream ->
                    val inputStream = file.inputStream()
                    inputStream.use {
                        fileOutputStream.write(it.readBytes())
                    }
                }
            }

            values.clear()
            values.put(MediaStore.Images.Media.IS_PENDING, 0)
            contentResolver.update(it, values, null, null)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(saveDir: String): File {
        val dir = File(saveDir)
        if (!dir.exists()) {
            dir.mkdirs()
        }

        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = File(saveDir)

        return File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )
    }
}