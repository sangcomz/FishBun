package com.sangcomz.fishbun.util

import android.graphics.drawable.Drawable

sealed class RadioType {

    class RadioText(val text: String) : RadioType()

    class RadioDrawable(val drawable: Drawable) : RadioType()

    object None : RadioType()

    inline infix fun isRadioText(block: RadioText.() -> Unit): RadioType {
        if (this is RadioText) {
            block()
        }
        return this
    }

    inline infix fun isRadioDrawable(block: RadioDrawable.() -> Unit): RadioType {
        if (this is RadioDrawable) {
            block()
        }
        return this
    }

    inline infix fun isRadioNone(block: None.() -> Unit): RadioType {
        if (this is None) {
            block()
        }
        return this
    }
}