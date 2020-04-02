package com.sangcomz.fishbun.ui.album

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.sangcomz.fishbun.BaseActivity
import com.sangcomz.fishbun.R
import com.sangcomz.fishbun.adapter.view.AlbumListAdapter
import com.sangcomz.fishbun.bean.Album
import com.sangcomz.fishbun.define.Define
import com.sangcomz.fishbun.ext.showSnackBar
import com.sangcomz.fishbun.model.AlbumRepositoryImpl
import com.sangcomz.fishbun.model.ImageDataSourceImpl
import com.sangcomz.fishbun.permission.PermissionCheck
import com.sangcomz.fishbun.util.CameraUtil
import com.sangcomz.fishbun.util.SingleMediaScanner
import com.sangcomz.fishbun.util.isLandscape
import com.sangcomz.fishbun.util.setStatusBarColor
import java.io.File
import java.util.*

class AlbumActivity : BaseActivity(), AlbumView {

    private val cameraUtil = CameraUtil()
    private val permissionCheck = PermissionCheck(this)

    private lateinit var albumPresenter: AlbumPresenter
    private var albumList: List<Album> = emptyList()
    private var groupEmptyView: Group? = null
    private var recyclerAlbumList: RecyclerView? = null
    private var adapter: AlbumListAdapter? = null
    private var txtAlbumMessage: TextView? = null

    override fun onSaveInstanceState(outState: Bundle) {
        if (adapter != null) {
            outState.putParcelableArrayList(
                define.SAVE_INSTANCE_ALBUM_LIST,
                adapter?.albumList as ArrayList<out Parcelable?>
            )
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(outState: Bundle) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(outState)
        // Restore state members from saved instance
        val albumList: List<Album>? =
            outState.getParcelableArrayList(define.SAVE_INSTANCE_ALBUM_LIST)
        val thumbList: List<Uri>? =
            outState.getParcelableArrayList(define.SAVE_INSTANCE_ALBUM_THUMB_LIST)
        if (albumList != null && thumbList != null) {
            adapter = AlbumListAdapter()
            adapter?.albumList = albumList
        }
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_album)
        initPresenter()
        initView()
        if (checkPermission()) loadAlbumList()
    }

    override fun onResume() {
        super.onResume()
        setRecyclerViewSpanCount()
    }

    override fun onDestroy() {
        super.onDestroy()
        albumPresenter.release()
    }

    private fun initView() {
        groupEmptyView = findViewById(R.id.group_album_empty)
        recyclerAlbumList = findViewById(R.id.recycler_album_list)
        txtAlbumMessage = findViewById(R.id.txt_album_msg)

        findViewById<ImageView>(R.id.iv_album_camera).setOnClickListener {
            if (checkCameraPermission()) {
                cameraUtil.takePicture(this@AlbumActivity, albumPresenter.pathDir)
            }
        }
        initToolBar()
    }

    private fun initRecyclerView() {
        val layoutManager =
            if (this.isLandscape()) GridLayoutManager(this, fishton.albumLandscapeSpanCount)
            else GridLayoutManager(this, fishton.albumPortraitSpanCount)

        recyclerAlbumList?.layoutManager = layoutManager
    }

    private fun initToolBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar_album_bar)

        txtAlbumMessage?.setText(R.string.msg_loading_image)
        setSupportActionBar(toolbar)
        toolbar.setBackgroundColor(fishton.colorActionBar)
        toolbar.setTitleTextColor(fishton.colorActionBarTitle)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.setStatusBarColor(fishton.colorStatusBar)
        }

        supportActionBar?.let {
            it.title = fishton.titleActionBar
            it.setDisplayHomeAsUpEnabled(true)
            if (fishton.drawableHomeAsUpIndicator != null) {
                it.setHomeAsUpIndicator(fishton.drawableHomeAsUpIndicator)
            }
        }

        if (fishton.isStatusBarLight
            && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
        ) {
            toolbar.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    private fun initPresenter() {
        albumPresenter = AlbumPresenter(
            this,
            AlbumRepositoryImpl(ImageDataSourceImpl(contentResolver))
        )
    }

    private fun setAlbumListAdapter() {
        if (adapter == null) {
            adapter = AlbumListAdapter()
        }
        adapter?.let {
            it.albumList = albumList
            recyclerAlbumList?.adapter = it
            it.notifyDataSetChanged()
            changeToolbarTitle()
        }
    }

    private fun setAlbumList(albumList: List<Album>) {
        this.albumList = albumList
        if (albumList.isNotEmpty()) {
            recyclerAlbumList?.visibility = View.VISIBLE
            groupEmptyView?.visibility = View.GONE
            initRecyclerView()
            setAlbumListAdapter()
        } else {
            groupEmptyView?.visibility = View.VISIBLE
            recyclerAlbumList?.visibility = View.INVISIBLE
            txtAlbumMessage?.setText(R.string.msg_no_image)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (fishton.isButton) {
            menuInflater.inflate(R.menu.menu_photo_album, menu)
            val menuDoneItem = menu.findItem(R.id.action_done)
            menu.findItem(R.id.action_all_done).isVisible = false
            if (fishton.drawableDoneButton != null) {
                menuDoneItem.icon = fishton.drawableDoneButton
            } else if (fishton.strDoneMenu != null) {
                if (fishton.colorTextMenu != Int.MAX_VALUE) {
                    val spanString = SpannableString(fishton.strDoneMenu)
                    spanString.setSpan(
                        ForegroundColorSpan(fishton.colorTextMenu),
                        0,
                        spanString.length,
                        0
                    ) //fi
                    menuDoneItem.title = spanString
                } else {
                    menuDoneItem.title = fishton.strDoneMenu
                }
                menuDoneItem.icon = null
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
        } else if (id == R.id.action_done) {
            if (adapter != null) {
                if (fishton.selectedImages.size < fishton.minCount) {
                    recyclerAlbumList?.showSnackBar(
                        fishton.messageNothingSelected,
                        Snackbar.LENGTH_SHORT
                    )
                } else {
                    finishActivityWithResult()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun changeToolbarTitle() {
        if (adapter == null) return
        val total = fishton.selectedImages.size

        supportActionBar?.apply {
            title = if (fishton.maxCount == 1 || !fishton.isShowCount) fishton.titleActionBar
            else fishton.titleActionBar + " (" + total + "/" + fishton.maxCount + ")"
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == define.ENTER_ALBUM_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                finishActivityWithResult()
            } else if (resultCode == define.TRANS_IMAGES_RESULT_CODE) {
                val addPath =
                    data?.getParcelableArrayListExtra<Uri>(define.INTENT_ADD_PATH)
                val position = data?.getIntExtra(define.INTENT_POSITION, -1)
                refreshAlbumItem(position, addPath)
                changeToolbarTitle()
            }
        } else if (requestCode == define.TAKE_A_PICK_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                SingleMediaScanner(this, File(cameraUtil.savePath)) {
                    loadAlbumList()
                }
            } else {
                File(cameraUtil.savePath).delete()
            }
            changeToolbarTitle()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            define.PERMISSION_STORAGE -> {
                if (grantResults.isNotEmpty()) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // permission was granted, yay!
                        loadAlbumList()
                    } else {
                        PermissionCheck(this).showPermissionDialog()
                        finish()
                    }
                }
            }
            define.PERMISSION_CAMERA -> {
                if (grantResults.isNotEmpty()) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // permission was granted, yay!
                        cameraUtil.takePicture(this, albumPresenter.pathDir)
                    } else {
                        PermissionCheck(this).showPermissionDialog()
                    }
                }
            }
        }
    }

    override fun showAlbumList(albumList: List<Album>) {
        setAlbumList(albumList)
    }

    private fun finishActivityWithResult() {
        val i = Intent()
        i.putParcelableArrayListExtra(Define.INTENT_PATH, fishton.selectedImages)
        setResult(Activity.RESULT_OK, i)
        finish()
    }

    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionCheck.checkStoragePermission()
        } else true
    }

    private fun checkCameraPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionCheck.checkCameraPermission()
        } else true
    }

    private fun setRecyclerViewSpanCount() {
        val recyclerView = recyclerAlbumList ?: return
        val gridLayoutManager = recyclerView.layoutManager as? GridLayoutManager ?: return

        gridLayoutManager.spanCount =
            if (isLandscape()) fishton.albumLandscapeSpanCount
            else fishton.albumPortraitSpanCount
    }

    private fun loadAlbumList() {
        albumPresenter.loadAlbumList(
            allViewTitle = fishton.titleAlbumAllView ?: getString(R.string.str_all_view),
            exceptMimeTypeList = fishton.exceptMimeTypeList,
            specifyFolderList = fishton.specifyFolderList
        )
    }

    private fun refreshAlbumItem(
        position: Int?,
        imagePath: ArrayList<Uri>?
    ) {
        position ?: return
        imagePath ?: return

        if (imagePath.size > 0) {
            if (position == 0) {
                loadAlbumList()
            } else {
                albumList[0].counter += imagePath.size
                albumList[position].counter += imagePath.size
                albumList[0].thumbnailPath = imagePath[imagePath.size - 1].toString()
                albumList[position].thumbnailPath = imagePath[imagePath.size - 1].toString()
                adapter?.notifyItemChanged(0)
                adapter?.notifyItemChanged(position)
            }
        }
    }

}