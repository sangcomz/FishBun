package com.sangcomz.fishbundemo

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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