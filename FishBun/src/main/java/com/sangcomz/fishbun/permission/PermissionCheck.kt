package com.sangcomz.fishbun.permission

import android.Manifest
import android.Manifest.permission.*
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.sangcomz.fishbun.R

/**
 * Created by sangc on 2015-10-12.
 */
class PermissionCheck(private val context: Context) {
    private fun checkPermission(permissionList: List<String>, requestCode: Int): Boolean {
        if (context !is Activity) return false

        val needRequestPermissionList = permissionList
            .map { it to ContextCompat.checkSelfPermission(context, it) }
            .filter { it.second != PackageManager.PERMISSION_GRANTED }
            .map { it.first }
            .toTypedArray()

        return if (needRequestPermissionList.isEmpty()) {
            true
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    context,
                    needRequestPermissionList.first()
                )
            ) {
                ActivityCompat.requestPermissions(context, needRequestPermissionList, requestCode)
            } else {
                ActivityCompat.requestPermissions(context, needRequestPermissionList, requestCode)
            }
            false
        }
    }

    fun checkStoragePermission(requestCode: Int): Boolean {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q -> {
                checkStoragePermissionUnderAPI30(requestCode)
            }
            Build.VERSION.SDK_INT > Build.VERSION_CODES.Q &&
                    Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU -> {
                checkStoragePermissionUnderAPI30To32(requestCode)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                checkStoragePermissionOrHigherAPI33(requestCode)
            }
            else -> true
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun checkStoragePermissionUnderAPI30(requestCode: Int): Boolean {
        return checkPermission(
            arrayListOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE),
            requestCode
        )
    }

    @TargetApi(Build.VERSION_CODES.R)
    private fun checkStoragePermissionUnderAPI30To32(requestCode: Int): Boolean {
        return checkPermission(
            arrayListOf(READ_EXTERNAL_STORAGE),
            requestCode
        )
    }

    @TargetApi(Build.VERSION_CODES.TIRAMISU)
    fun checkStoragePermissionOrHigherAPI33(requestCode: Int): Boolean {
        return checkPermission(
            arrayListOf(READ_MEDIA_IMAGES),
            requestCode
        )
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun checkCameraPermission(requestCode: Int): Boolean {
        try {
            val info = context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.GET_PERMISSIONS
            )
            //This array contains the requested permissions.
            val permissions = info.requestedPermissions

            return if (permissions?.contains(CAMERA) == true) {
                checkPermission(listOf(CAMERA), requestCode)
            } else {
                false
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            return false
        }
    }

    fun showPermissionDialog() {
        Toast.makeText(context, R.string.msg_permission, Toast.LENGTH_SHORT).show()
    }

}
