package com.sangcomz.fishbundemo

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.adapter.image.impl.PicassoAdapter
import com.sangcomz.fishbun.define.Define
import kotlinx.android.synthetic.main.fragment_sub.*
import kotlinx.android.synthetic.main.fragment_sub.view.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class SubFragment : Fragment() {

    var path = ArrayList<Uri>()
    private lateinit var imageAdapter: ImageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_sub, container, false)

        with(recyclerview) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            imageAdapter = ImageAdapter(ImageController(img_main), path)
            adapter = imageAdapter
        }

        btn_add_images.setOnClickListener {
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

    companion object {
        fun newInstance() = SubFragment()
    }
}