package com.sangcomz.fishbun

import android.app.Activity
import androidx.fragment.app.Fragment
import com.sangcomz.fishbun.adapter.image.ImageAdapter
import java.lang.ref.WeakReference

class FishBun private constructor(context: Activity?) {

    private val _context: WeakReference<Activity?> = WeakReference(context)

    val context: Activity?
        get() = _context.get()

    fun setImageAdapter(imageAdapter: ImageAdapter): FishBunCreator {
        val fishton = Fishton.getInstance().apply {
            refresh()
            this.imageAdapter = imageAdapter
        }
        return FishBunCreator(this, fishton)
    }

    companion object {
        @JvmStatic
        fun with(activity: Activity) = FishBun(activity)

        @JvmStatic
        fun with(fragment: Fragment) = FishBun(fragment.activity)
    }
}