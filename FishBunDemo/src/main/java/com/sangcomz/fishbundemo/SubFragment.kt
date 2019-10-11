package com.sangcomz.fishbundemo

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.adapter.image.impl.PicassoAdapter
import com.sangcomz.fishbun.define.Define
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class SubFragment : Fragment() {

    var path = ArrayList<Uri>()
    private lateinit var imgMain: ImageView
    private lateinit var btnAddImages: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageAdapter: ImageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_sub, container, false)

        // Inflate the layout for this fragment
        with(rootView) {
            imgMain = findViewById(R.id.img_main)
            recyclerView = findViewById(R.id.recyclerview)
            btnAddImages = findViewById(R.id.btn_add_images)
        }

        with(recyclerView) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = ImageAdapter(ImageController(imgMain), path)
        }

        btnAddImages.setOnClickListener {
            FishBun.with(this@SubFragment)
                .setImageAdapter(PicassoAdapter())
                .setMaxCount(10)
                .setActionBarColor(Color.parseColor("#3F51B5"), Color.parseColor("#303F9F"))
                .setSelectedImages(path)
                .setCamera(true)
                .startAlbum()
        }

        return rootView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Define.ALBUM_REQUEST_CODE -> if (resultCode == Activity.RESULT_OK) {
                path = data!!.getParcelableArrayListExtra(Define.INTENT_PATH)
                imageAdapter.changePath(path)
            }
        }
    }
}