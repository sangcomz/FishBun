package com.sangcomz.fishbun.util

import android.content.Context
import android.util.AttributeSet

/**
 * Created by sangc on 2015-12-26.
 */
class SquareFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) : androidx.appcompat.widget.ContentFrameLayout(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}
