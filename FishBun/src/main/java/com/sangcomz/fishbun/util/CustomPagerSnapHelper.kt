package com.sangcomz.fishbun.util

import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by sangcomz on 11/06/2017.
 */

class CustomPagerSnapHelper @JvmOverloads constructor(
    private val layoutManager: RecyclerView.LayoutManager,
    private val onPageChanged: ((position: Int) -> Unit)? = null) : PagerSnapHelper() {

    override fun onFling(velocityX: Int, velocityY: Int): Boolean {
        onPageChanged?.invoke(findTargetSnapPosition(layoutManager, velocityX, velocityY))
        return super.onFling(velocityX, velocityY)
    }
}
