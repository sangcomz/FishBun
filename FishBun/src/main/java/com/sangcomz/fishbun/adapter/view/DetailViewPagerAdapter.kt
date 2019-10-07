package com.sangcomz.fishbun.adapter.view

import android.net.Uri
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.sangcomz.fishbun.Fishton
import com.sangcomz.fishbun.R
import kotlinx.android.synthetic.main.detail_item.view.*

/**
 * Created by sangcomz on 15/06/2017.
 */

class DetailViewPagerAdapter(private val inflater: LayoutInflater, private val images: Array<Uri>) : PagerAdapter() {
    private val fishton = Fishton.getInstance()

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = inflater.inflate(R.layout.detail_item, container, false)
        container.addView(itemView)

        fishton.imageAdapter?.loadDetailImage(itemView.img_detail_image, images[position])

        return itemView
    }

    override fun getCount(): Int = images.size

    override fun destroyItem(container: ViewGroup, position: Int, targetObject: Any) {
        if (container is ViewPager) {
            container.removeView(targetObject as ConstraintLayout)
        }
    }

    override fun isViewFromObject(view: View, targetObject: Any): Boolean {
        return view == targetObject
    }
}