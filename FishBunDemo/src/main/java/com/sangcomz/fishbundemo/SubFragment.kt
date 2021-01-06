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
import com.sangcomz.fishbun.adapter.image.impl.CoilAdapter
import com.sangcomz.fishbundemo.databinding.FragmentSubBinding
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class SubFragment : Fragment() {

    var path = ArrayList<Uri>()
    private lateinit var imageAdapter: ImageAdapter
    private var _binding: FragmentSubBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.recyclerview) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            imageAdapter = ImageAdapter(activity!!, ImageController(binding.imgMain), path)
            adapter = imageAdapter
        }

        binding.btnAddImages.setOnClickListener {
            FishBun.with(this@SubFragment)
                .setImageAdapter(CoilAdapter())
                .setMaxCount(10)
                .setActionBarColor(Color.parseColor("#3F51B5"), Color.parseColor("#303F9F"))
                .setSelectedImages(path)
                .setCamera(true)
                .startAlbum()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            FishBun.FISHBUN_REQUEST_CODE -> if (resultCode == Activity.RESULT_OK) {
                path = data?.getParcelableArrayListExtra(FishBun.INTENT_PATH) ?: arrayListOf()
                imageAdapter.changePath(path)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = SubFragment()
    }
}