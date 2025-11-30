package com.sangcomz.fishbun

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sangcomz.fishbun.permission.PermissionCheck
import com.sangcomz.fishbun.util.CameraUtil

abstract class BaseActivity : AppCompatActivity() {

    protected val cameraUtil: CameraUtil by lazy { CameraUtil() }
    protected val permissionCheck: PermissionCheck by lazy { PermissionCheck(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Fishton.enableEdgeToEdge) {
            val isLight =
                if (Fishton.isStatusBarLight) SystemBarStyle.dark(Color.BLACK)
                else SystemBarStyle.auto(
                    Color.TRANSPARENT,
                    Color.TRANSPARENT
                )
            enableEdgeToEdge(
                statusBarStyle = isLight

            )
        }

        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        super.onCreate(savedInstanceState)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)

        if (Fishton.enableEdgeToEdge) {
            setupNavigationBarInsets(findViewById(android.R.id.content))
        }
    }

    fun setupStatusBarInsets(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val statusBars = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            v.setPadding(0, statusBars.top, 0, 0)
            insets
        }
    }

    fun setupNavigationBarInsets(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val navigationBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            v.setPadding(navigationBars.left, 0, navigationBars.right, navigationBars.bottom)
            insets
        }
    }

    companion object {
        const val PERMISSION_STORAGE = 28
        const val PERMISSION_CAMERA = 29
        const val TAKE_A_NEW_PICTURE_RESULT_CODE = 29
        const val TAKE_A_PICTURE_REQUEST_CODE = 128
        const val ENTER_ALBUM_REQUEST_CODE = 129
        const val ENTER_DETAIL_REQUEST_CODE = 130

        const val SAVE_INSTANCE_NEW_IMAGES = "instance_new_images"
        const val SAVE_INSTANCE_SAVED_IMAGE = "instance_saved_image"
    }
}