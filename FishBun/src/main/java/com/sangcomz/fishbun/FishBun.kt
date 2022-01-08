package com.sangcomz.fishbun

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import com.sangcomz.fishbun.adapter.image.ImageAdapter
import java.lang.ref.WeakReference

class FishBun private constructor(activity: Activity?, fragment: Fragment?) {

    private val _activity: WeakReference<Activity?> = WeakReference(activity)
    private val _fragment: WeakReference<Fragment?> = WeakReference(fragment)

    val fishBunContext: FishBunContext get() = FishBunContext()

    fun setImageAdapter(imageAdapter: ImageAdapter): FishBunCreator {
        val fishton = Fishton.apply {
            refresh()
            this.imageAdapter = imageAdapter
        }
        return FishBunCreator(this, fishton)
    }

    companion object {
        @Deprecated("To be deleted along with the startAlbum function")
        const val FISHBUN_REQUEST_CODE = 27
        const val INTENT_PATH = "intent_path"

        @JvmStatic
        fun with(activity: Activity) = FishBun(activity, null)

        @JvmStatic
        fun with(fragment: Fragment) = FishBun(null, fragment)
    }

    inner class FishBunContext {
        private val activity = _activity.get()
        private val fragment = _fragment.get()
        fun getContext(): Context =
            activity ?: fragment?.context ?: throw NullPointerException("Activity or Fragment Null")

        fun startActivityForResult(intent: Intent, requestCode: Int) {
            when {
                activity != null -> activity.startActivityForResult(intent, requestCode)
                fragment != null -> fragment.startActivityForResult(intent, requestCode)
                else -> throw NullPointerException("Activity or Fragment Null")
            }
        }

        fun startWithRegisterForActivityResult(
            activityResultLauncher: ActivityResultLauncher<Intent>,
            intent: Intent
        ) {
            when {
                activity != null || fragment != null -> activityResultLauncher.launch(intent)
                else -> throw NullPointerException("Activity or Fragment Null")
            }
        }
    }
}


