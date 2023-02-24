package com.sangcomz.fishbun.ui.detail.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.android.material.snackbar.Snackbar
import com.sangcomz.fishbun.BaseActivity
import com.sangcomz.fishbun.Fishton
import com.sangcomz.fishbun.R
import com.sangcomz.fishbun.adapter.image.ImageAdapter
import com.sangcomz.fishbun.datasource.FishBunDataSourceImpl
import com.sangcomz.fishbun.ui.detail.DetailImageContract
import com.sangcomz.fishbun.ui.detail.adapter.DetailViewPagerAdapter
import com.sangcomz.fishbun.ui.detail.model.DetailImageRepositoryImpl
import com.sangcomz.fishbun.ui.detail.model.DetailImageViewData
import com.sangcomz.fishbun.ui.detail.mvp.DetailImagePresenter
import com.sangcomz.fishbun.util.RadioWithTextButton
import com.sangcomz.fishbun.util.setStatusBarColor

class DetailImageActivity : BaseActivity(), DetailImageContract.View, OnPageChangeListener {

    private lateinit var presenter: DetailImageContract.Presenter

    private var btnDetailCount: RadioWithTextButton? = null
    private var vpDetailPager: ViewPager? = null
    private var btnDetailBack: ImageButton? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_activity)
        initView()

        presenter = createPresenter()
        presenter.handleOnCreate(intent.getIntExtra(INIT_IMAGE_POSITION, -1))
    }

    override fun onDestroy() {
        vpDetailPager?.removeOnPageChangeListener(this)
        super.onDestroy()
    }

    override fun onBackPressed() {
        finishActivity()
    }

    override fun updateRadioButtonWithText(text: String) {
        //FIXME Need to fix to work without post
        btnDetailCount?.post {
            btnDetailCount?.setText(text)
        }
    }

    override fun updateRadioButtonWithDrawable() {
        val radioButton = btnDetailCount ?: return
        ContextCompat.getDrawable(this, R.drawable.ic_done_white_24dp)?.let {
            radioButton.setDrawable(it)
        }
    }

    override fun showSnackbar(message: String) {
        btnDetailCount?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun unselectImage() {
        btnDetailCount?.unselect()
    }

    override fun onPageSelected(position: Int) {
        presenter.changeButtonStatus(position)
    }

    override fun finishActivity() {
        val i = Intent()
        setResult(Activity.RESULT_OK, i)
        finish()
    }

    override fun setToolBar(detailImageViewData: DetailImageViewData) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.setStatusBarColor(Color.BLACK)
        }
    }

    override fun setCountButton(detailImageViewData: DetailImageViewData) {
        btnDetailCount?.run {
            unselect()
            setCircleColor(detailImageViewData.colorActionBar)
            setTextColor(detailImageViewData.colorActionBarTitle)
            setStrokeColor(detailImageViewData.colorSelectCircleStroke)
            setOnClickListener {
                val currentPosition = vpDetailPager?.currentItem ?: return@setOnClickListener
                presenter.onCountClick(currentPosition)
            }
        }
    }

    override fun setBackButton() {
        btnDetailBack?.setOnClickListener {
            finishActivity()
        }
    }

    override fun showImages(initPosition: Int, pickerImages: List<Uri>) {
        vpDetailPager?.run {
            (adapter as? DetailViewPagerAdapter)?.setImages(pickerImages)
            currentItem = initPosition
        }
    }

    override fun finishAndShowErrorToast() {
        Toast.makeText(this, R.string.msg_error, Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun showToastAndFinish(@StringRes resId: Int, code: Int) {
        runOnUiThread {
            Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
        }
        setResult(code, Intent())
        finish()
    }

    override fun initViewPagerAdapter(imageAdapter: ImageAdapter) {
        vpDetailPager?.run {
            adapter = DetailViewPagerAdapter(imageAdapter)
        }
    }

    override fun onPageScrollStateChanged(state: Int) = Unit

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) =
        Unit

    private fun initView() {
        vpDetailPager = findViewById(R.id.vp_detail_pager)
        btnDetailCount = findViewById(R.id.btn_detail_count)
        btnDetailBack = findViewById(R.id.btn_detail_back)

        vpDetailPager?.addOnPageChangeListener(this@DetailImageActivity)
    }

    private fun createPresenter() = DetailImagePresenter(
        this,
        DetailImageRepositoryImpl(FishBunDataSourceImpl(Fishton))
    )


    companion object {
        private const val INIT_IMAGE_POSITION = "init_image_position"

        fun getDetailImageActivity(context: Context, initPosition: Int): Intent =
            Intent(context, DetailImageActivity::class.java).apply {
                putExtra(INIT_IMAGE_POSITION, initPosition)
            }
    }
}