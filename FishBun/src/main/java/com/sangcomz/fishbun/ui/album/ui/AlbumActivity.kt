package com.sangcomz.fishbun.ui.album.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
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
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.R
import com.sangcomz.fishbun.ui.album.model.Album
import com.sangcomz.fishbun.ext.showSnackBar
import com.sangcomz.fishbun.ui.album.model.repository.AlbumRepositoryImpl
import com.sangcomz.fishbun.datasource.ImageDataSourceImpl
import com.sangcomz.fishbun.permission.PermissionCheck
import com.sangcomz.fishbun.ui.album.AlbumContract
import com.sangcomz.fishbun.ui.album.mvp.AlbumPresenter
import com.sangcomz.fishbun.ui.album.adapter.AlbumListAdapter
import com.sangcomz.fishbun.ui.album.listener.AlbumClickListener
import com.sangcomz.fishbun.ui.picker.PickerActivity
import com.sangcomz.fishbun.util.SingleMediaScanner
import com.sangcomz.fishbun.util.isLandscape
import com.sangcomz.fishbun.util.setStatusBarColor
import java.io.File
import java.util.*

class AlbumActivity : BaseActivity(),
    AlbumContract.View, AlbumClickListener {
    private val albumPresenter: AlbumContract.Presenter by lazy {
        AlbumPresenter(
            this,
            AlbumRepositoryImpl(
                ImageDataSourceImpl(
                    contentResolver,
                    fishton.exceptMimeTypeList,
                    fishton.specifyFolderList
                )
            )
        )
    }
    private var groupEmptyView: Group? = null
    private var recyclerAlbumList: RecyclerView? = null
    private var adapter: AlbumListAdapter? = null
    private var txtAlbumMessage: TextView? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_album)
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
                cameraUtil.takePicture(
                    this@AlbumActivity,
                    albumPresenter.getPathDir(),
                    TAKE_A_PICK_REQUEST_CODE
                )
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

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ENTER_ALBUM_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                finishActivityWithResult()
            } else if (resultCode == TRANS_IMAGES_RESULT_CODE) {
                val addPath = data?.getParcelableArrayListExtra<Uri>(INTENT_ADD_PATH)
                val position = data?.getIntExtra(INTENT_POSITION, -1)
                refreshAlbumItem(position, addPath)
                changeToolbarTitle()
            }
        } else if (requestCode == TAKE_A_PICK_REQUEST_CODE) {
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
            PERMISSION_STORAGE -> {
                if (grantResults.isNotEmpty()) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // permission was granted, yay!
                        loadAlbumList()
                    } else {
                        permissionCheck.showPermissionDialog()
                        finish()
                    }
                }
            }
            PERMISSION_CAMERA -> {
                if (grantResults.isNotEmpty()) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // permission was granted, yay!
                        cameraUtil.takePicture(
                            this,
                            albumPresenter.getPathDir(),
                            TAKE_A_PICK_REQUEST_CODE
                        )
                    } else {
                        permissionCheck.showPermissionDialog()
                    }
                }
            }
        }
    }

    override fun showAlbumList(albumList: List<Album>) {
        if (albumList.isNotEmpty()) {
            bindAlbumList(albumList)
        } else {
            showEmptyView()
        }
    }


    override fun onAlbumClick(position: Int, album: Album) {
        PickerActivity.getPickerActivityIntent(this, album.id, album.displayName, position)
            .also { startActivityForResult(it, ENTER_ALBUM_REQUEST_CODE) }
    }

    private fun changeToolbarTitle() {
        if (adapter == null) return
        val total = fishton.selectedImages.size

        supportActionBar?.apply {
            title = if (fishton.maxCount == 1 || !fishton.isShowCount) fishton.titleActionBar
            else fishton.titleActionBar + " (" + total + "/" + fishton.maxCount + ")"
        }
    }

    private fun finishActivityWithResult() {
        val i = Intent()
        i.putParcelableArrayListExtra(FishBun.INTENT_PATH, fishton.selectedImages)
        setResult(Activity.RESULT_OK, i)
        finish()
    }

    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionCheck.checkStoragePermission(PERMISSION_STORAGE)
        } else true
    }

    private fun checkCameraPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionCheck.checkCameraPermission(PERMISSION_CAMERA)
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
        albumPresenter.loadAlbumList(allViewTitle = fishton.titleAlbumAllView ?: getString(R.string.str_all_view))
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
                val thumbnailPath = imagePath[imagePath.size - 1].toString()
                val addedCount = imagePath.size
                adapter?.updateAlbumMeta(0, addedCount, thumbnailPath)
                adapter?.updateAlbumMeta(position, addedCount, thumbnailPath)
            }
        }
    }

    private fun setAlbumListAdapter(albumList: List<Album>) {
        if (adapter == null) {
            adapter = AlbumListAdapter(
                this,
                fishton.albumThumbnailSize,
                fishton.imageAdapter
            )
        }
        adapter?.let {
            it.setAlbumList(albumList)
            recyclerAlbumList?.adapter = it
            it.notifyDataSetChanged()
            changeToolbarTitle()
        }
    }

    private fun bindAlbumList(albumList: List<Album>) {
        recyclerAlbumList?.visibility = View.VISIBLE
        groupEmptyView?.visibility = View.GONE
        initRecyclerView()
        setAlbumListAdapter(albumList)
    }

    private fun showEmptyView() {
        groupEmptyView?.visibility = View.VISIBLE
        recyclerAlbumList?.visibility = View.INVISIBLE
        txtAlbumMessage?.setText(R.string.msg_no_image)
    }
}