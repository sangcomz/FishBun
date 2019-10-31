@file:JvmName("DrawUtil")

package com.sangcomz.fishbun.util

import android.graphics.Paint
import android.graphics.Rect

/**
 * Sets the text size for a Paint object so a given string of text will be a
 * given width.
 *
 * @param paint        the Paint to set the text size for
 * @param desiredWidth the desired width
 */
fun Paint.setTextSizeForWidth(text: String, desiredWidth: Float) {
    // Pick a reasonably large value for the test. Larger values produce
    // more accurate results, but may cause problems with hardware
    // acceleration. But there are workarounds for that, too; refer to
    // http://stackoverflow.com/questions/6253528/font-size-too-large-to-fit-in-cache
    val defaultTextSize = 44f
    val textBounds = Rect()
    textSize = defaultTextSize
    getTextBounds(text, 0, text.length, textBounds)
    // Calculate the desired size as a proportion of our testTextSize.
    if (textBounds.width() > desiredWidth) {
        val desiredTextSize = defaultTextSize * (desiredWidth / textBounds.width())
        // Set the paint for that size.
        textSize = desiredTextSize
    }
}
