package com.sangcomz.fishbun.ui.detail.adapter

import android.net.Uri
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.sangcomz.fishbun.Fishton
import com.sangcomz.fishbun.R
import com.sangcomz.fishbun.adapter.image.ImageAdapter

/**
 * Created by sangcomz on 15/06/2017.
 */

class DetailViewPagerAdapter(private val imageAdapter: ImageAdapter) : PagerAdapter() {
    private var images: List<Uri> = emptyList()

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView =
            LayoutInflater.from(container.context).inflate(R.layout.detail_item, container, false)
        container.addView(itemView)

        imageAdapter.loadDetailImage(itemView.findViewById(R.id.img_detail_image), images[position])

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

    fun setImages(images: List<Uri>) {
        this.images = images
        notifyDataSetChanged()
    }
}