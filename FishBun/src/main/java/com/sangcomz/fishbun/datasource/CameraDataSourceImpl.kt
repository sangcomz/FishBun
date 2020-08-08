package com.sangcomz.fishbun.datasource

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File

class CameraDataSourceImpl(private val context: Context) : CameraDataSource {
    override fun getCameraPath(): String {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/Camera").absolutePath
    }

    override fun getPicturePath(): String? {
        return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath
    }


}