package com.sangcomz.fishbun.util

import android.content.ContentResolver
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.provider.MediaStore
import android.widget.ImageView
import androidx.exifinterface.media.ExifInterface
import com.squareup.picasso.Transformation

class ImageRotateTransformation(private val imageView : ImageView, private val loadUrl : Uri) : Transformation {
    private val contentResolver : ContentResolver = imageView.context.contentResolver;

    override fun key(): String = "imageRotateTransformation";

    override fun transform(source: Bitmap): Bitmap {
        val realPath: String = getRealPathFromUri(contentResolver, loadUrl)
        if (realPath == "") return source
        val exifInterface : ExifInterface = ExifInterface(realPath)
        val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        var rotate : Float = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }.toFloat()

        var matrix : Matrix = Matrix()
        matrix.setRotate(rotate, (source.width / 2).toFloat(), (source.height / 2).toFloat())
        var newBitmap : Bitmap = Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true);
        source.recycle();
        return newBitmap;
    }

    private fun getRealPathFromUri(contentResolver: ContentResolver, uri : Uri) : String {
        var result : String = ""
        var cursor : Cursor? = null
        try {
            cursor = contentResolver.query(uri, null, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                if (idx != -1)
                    result = cursor.getString(idx)
            }
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
        return result
    }
}