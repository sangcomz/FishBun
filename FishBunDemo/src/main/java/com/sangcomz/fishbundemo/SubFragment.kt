package com.sangcomz.fishbundemo

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.adapter.image.impl.CoilAdapter
import com.sangcomz.fishbundemo.databinding.FragmentSubBinding

/**
 * A simple [Fragment] subclass.
 */
class SubFragment : Fragment() {

    var path = ArrayList<Uri>()
    private lateinit var imageAdapter: ImageAdapter
    private var _binding: FragmentSubBinding? = null
    private val binding get() = _binding!!

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == AppCompatActivity.RESULT_OK) {
            path = it.data?.getParcelableArrayListExtra(FishBun.INTENT_PATH) ?: arrayListOf()
            imageAdapter.changePath(path)
        }
    }


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
            imageAdapter = ImageAdapter(requireActivity(), ImageController(binding.imgMain), path)
            adapter = imageAdapter
        }

        binding.btnAddImages.setOnClickListener {
            FishBun.with(this@SubFragment)
                .setImageAdapter(CoilAdapter())
                .setMaxCount(10)
                .setActionBarColor(Color.parseColor("#3F51B5"), Color.parseColor("#303F9F"))
                .setSelectedImages(path)
                .hasCameraInPickerPage(true)
                .startAlbumWithOnActivityResult(IMAGE_PICKER_REQUEST_CODE)
        }

        binding.btnAddImagesUseCallback.setOnClickListener {
            FishBun.with(this@SubFragment)
                .setImageAdapter(CoilAdapter())
                .setMaxCount(10)
                .setActionBarColor(Color.parseColor("#3F51B5"), Color.parseColor("#303F9F"))
                .setSelectedImages(path)
                .hasCameraInPickerPage(true)
                .startAlbumWithActivityResultCallback(startForResult)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            IMAGE_PICKER_REQUEST_CODE -> if (resultCode == Activity.RESULT_OK) {
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
        const val IMAGE_PICKER_REQUEST_CODE = 1
        fun newInstance() = SubFragment()
    }
}