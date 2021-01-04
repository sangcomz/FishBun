package com.sangcomz.fishbundemo

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.MimeType
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter
import com.sangcomz.fishbun.adapter.image.impl.CoilAdapter
import com.sangcomz.fishbundemo.databinding.ActivityWithactivityBinding
import java.util.*

class WithActivityActivity : AppCompatActivity() {

    var path = ArrayList<Uri>()
    private lateinit var imageAdapter: ImageAdapter
    private var mode: Int = 0
    private lateinit var binding: ActivityWithactivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWithactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mode = intent.getIntExtra("mode", -1)

        with(binding.recyclerview) {
            layoutManager = LinearLayoutManager(
                this@WithActivityActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )

            imageAdapter = ImageAdapter(context, ImageController(binding.imgMain), path)
            adapter = imageAdapter
        }
    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int,
        imageData: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, imageData)

        if (requestCode == FishBun.FISHBUN_REQUEST_CODE && resultCode == RESULT_OK) {
            path = imageData?.getParcelableArrayListExtra(FishBun.INTENT_PATH) ?: arrayListOf()
            imageAdapter.changePath(path)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_plus) {
            when (mode) {
                //basic
                0 -> {
                    FishBun.with(this@WithActivityActivity)
                        .setImageAdapter(GlideAdapter())
                        .setSelectedImages(path)
                        .startAlbum()
                }
                //dark
                1 -> {
                    FishBun.with(this@WithActivityActivity)
                        .setImageAdapter(GlideAdapter())
                        .setMaxCount(5)
                        .setMinCount(3)
                        .setPickerSpanCount(5)
                        .setActionBarColor(
                            Color.parseColor("#795548"),
                            Color.parseColor("#5D4037"),
                            false
                        )
                        .setActionBarTitleColor(Color.parseColor("#ffffff"))
                        .setSelectedImages(path)
                        .setAlbumSpanCount(2, 3)
                        .setButtonInAlbumActivity(false)
                        .hasCameraInPickerPage(true)
                        .exceptMimeType(arrayListOf(MimeType.GIF))
                        .setReachLimitAutomaticClose(true)
                        .setHomeAsUpIndicatorDrawable(
                            ContextCompat.getDrawable(
                                this,
                                R.drawable.ic_custom_back_white
                            )
                        )
                        .setDoneButtonDrawable(
                            ContextCompat.getDrawable(
                                this,
                                R.drawable.ic_custom_ok
                            )
                        )
                        .setAllViewTitle("All")
                        .setActionBarTitle("FishBun Dark")
                        .textOnNothingSelected("Please select three or more!")
                        .startAlbum()
                }
                //Light
                2 -> {
                    FishBun.with(this@WithActivityActivity)
                        .setImageAdapter(CoilAdapter())
                        .setMaxCount(50)
                        .setPickerSpanCount(4)
                        .setActionBarColor(
                            Color.parseColor("#ffffff"),
                            Color.parseColor("#ffffff"),
                            true
                        )
                        .setActionBarTitleColor(Color.parseColor("#000000"))
                        .setSelectedImages(path)
                        .setAlbumSpanCount(1, 2)
                        .setButtonInAlbumActivity(true)
                        .hasCameraInPickerPage(false)
                        .exceptMimeType(arrayListOf(MimeType.GIF))
                        .setReachLimitAutomaticClose(false)
                        .setHomeAsUpIndicatorDrawable(
                            ContextCompat.getDrawable(
                                this,
                                R.drawable.ic_arrow_back_black_24dp
                            )
                        )

                        .setIsUseAllDoneButton(true)
                        .setMenuDoneText("Choose")
                        .setMenuAllDoneText("Choose All")
                        .setIsUseAllDoneButton(true)
                        .setAllViewTitle("All of your photos")
                        .setActionBarTitle("FishBun Light")
                        .textOnImagesSelectionLimitReached("You can't select any more.")
                        .textOnNothingSelected("I need a photo!")
                        .startAlbum()
                }
                else -> {
                    finish()
                }
            }

            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
