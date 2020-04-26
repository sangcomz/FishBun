package com.sangcomz.fishbun.ui.picker

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.sangcomz.fishbun.BaseActivity
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.Fishton
import com.sangcomz.fishbun.R
import com.sangcomz.fishbun.adapter.image.ImageAdapter
import com.sangcomz.fishbun.datasource.*
import com.sangcomz.fishbun.permission.PermissionCheck
import com.sangcomz.fishbun.ui.detail.ui.DetailImageActivity.Companion.getDetailImageActivity
import com.sangcomz.fishbun.ui.picker.listener.OnPickerActionListener
import com.sangcomz.fishbun.ui.picker.model.PickerListItem
import com.sangcomz.fishbun.ui.picker.model.PickerRepositoryImpl
import com.sangcomz.fishbun.ui.picker.model.PickerViewData
import com.sangcomz.fishbun.util.MainUiHandler
import com.sangcomz.fishbun.util.SingleMediaScanner
import com.sangcomz.fishbun.util.setStatusBarColor
import java.io.File


class PickerActivity : BaseActivity(),
    PickerContract.View, OnPickerActionListener {
    private val pickerPresenter: PickerContract.Presenter by lazy {
        PickerPresenter(
            this, PickerRepositoryImpl(
                ImageDataSourceImpl(this.contentResolver),
                FishBunDataSourceImpl(Fishton.getInstance()),
                PickerIntentDataSourceImpl(intent)
            ),
            MainUiHandler()
        )
    }

    private var recyclerView: RecyclerView? = null
    private var adapter: PickerAdapter? = null
    private var layoutManager: GridLayoutManager? = null

    override fun onSaveInstanceState(outState: Bundle) {
        try {
            outState.putString(SAVE_INSTANCE_SAVED_IMAGE, savePath)
            outState.putParcelableArrayList(
                SAVE_INSTANCE_NEW_IMAGES,
                ArrayList(pickerPresenter.getAddedImagePathList())
            )
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(outState: Bundle) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(outState)
        // Restore state members from saved instance
        try {
            val addedImagePathList = outState.getParcelableArrayList<Uri>(SAVE_INSTANCE_NEW_IMAGES)
            val savedImage = outState.getString(SAVE_INSTANCE_SAVED_IMAGE)
            if (addedImagePathList != null) {
                pickerPresenter.addAllAddedPath(addedImagePathList)
            }
            if (savedImage != null) {
                savePath = savedImage
            }
            pickerPresenter.getPickerListItem()
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_picker)
        initView()
        if (checkPermission()) pickerPresenter.getPickerListItem()
    }

    override fun onBackPressed() {
        pickerPresenter.transImageFinish()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            TAKE_A_PICK_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    onSuccessTakePicture()
                    val savedFile = File(savePath)
                    SingleMediaScanner(this, savedFile)
                    adapter?.addImage(Uri.fromFile(savedFile))
                } else {
                    File(savePath).delete()
                }
            }
            ENTER_DETAIL_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    pickerPresenter.onDetailImageActivityResult()

                }
            }
        }
    }

    private fun onSuccessTakePicture() {
        val savedFile = File(savePath)
        SingleMediaScanner(this, savedFile)
        adapter?.addImage(Uri.fromFile(savedFile))
        pickerPresenter.successTakePicture(Uri.fromFile(savedFile))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_STORAGE -> {
                if (grantResults.isNotEmpty()) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        pickerPresenter.getPickerListItem()
                        // permission was granted, yay! do the
                        // calendar task you need to do.
                    } else {
                        PermissionCheck(this).showPermissionDialog()
                        finish()
                    }
                }
            }
            PERMISSION_CAMERA -> {
                if (grantResults.isNotEmpty()) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // permission was granted, yay!
                        pickerPresenter.takePicture()
                    } else {
                        PermissionCheck(this).showPermissionDialog()
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_photo_album, menu)
        val menuDoneItem = menu.findItem(R.id.action_done)
        val menuAllDoneItem = menu.findItem(R.id.action_all_done)
        pickerPresenter.getPickerMenuViewData {
            if (it.drawableDoneButton != null) {
                menuDoneItem.icon = it.drawableDoneButton
            } else if (it.strDoneMenu != null) {
                if (it.colorTextMenu != Int.MAX_VALUE) {
                    val spanString = SpannableString(it.strDoneMenu)
                    spanString.setSpan(
                        ForegroundColorSpan(it.colorTextMenu),
                        0,
                        spanString.length,
                        0
                    ) //fi
                    menuDoneItem.title = spanString
                } else {
                    menuDoneItem.title = it.strDoneMenu
                }
                menuDoneItem.icon = null
            }
            if (it.isUseAllDoneButton) {
                menuAllDoneItem.isVisible = true
                if (it.drawableAllDoneButton != null) {
                    menuAllDoneItem.icon = it.drawableAllDoneButton
                } else if (it.strAllDoneMenu != null) {
                    if (it.colorTextMenu != Int.MAX_VALUE) {
                        val spanString = SpannableString(it.strAllDoneMenu)
                        spanString.setSpan(
                            ForegroundColorSpan(it.colorTextMenu),
                            0,
                            spanString.length,
                            0
                        ) //fi
                        menuAllDoneItem.title = spanString
                    } else {
                        menuAllDoneItem.title = it.strAllDoneMenu
                    }
                    menuAllDoneItem.icon = null
                }
            } else {
                menuAllDoneItem.isVisible = false
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify album parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_done -> {
                pickerPresenter.onClickMenuDone()
            }
            R.id.action_all_done -> {
                pickerPresenter.onClickMenuAllDone()
            }
            android.R.id.home -> {
                pickerPresenter.transImageFinish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setToolbarTitle(
        pickerViewData: PickerViewData,
        selectedCount: Int,
        albumName: String
    ) {
        supportActionBar?.run {
            title = if (pickerViewData.maxCount == 1 || !pickerViewData.isShowCount)
                albumName
            else
                getString(R.string.title_toolbar, albumName, selectedCount, pickerViewData.maxCount)
        }
    }

    private fun initView() {
        pickerPresenter.getDesignViewData()
    }

    override fun initToolBar(pickerViewData: PickerViewData) {
        val toolbar = findViewById<Toolbar>(R.id.toolbar_picker_bar)
        setSupportActionBar(toolbar)
        toolbar.setBackgroundColor(pickerViewData.colorActionBar)
        toolbar.setTitleTextColor(pickerViewData.colorActionBarTitle)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.setStatusBarColor(pickerViewData.colorStatusBar)
        }
        val bar = supportActionBar
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true)
            if (pickerViewData.drawableHomeAsUpIndicator != null) supportActionBar?.setHomeAsUpIndicator(
                pickerViewData.drawableHomeAsUpIndicator
            )
        }
        if (pickerViewData.isStatusBarLight
            && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
        ) {
            toolbar.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    override fun initRecyclerView(pickerViewData: PickerViewData) {
        recyclerView = findViewById(R.id.recycler_picker_list)
        layoutManager =
            GridLayoutManager(this, pickerViewData.photoSpanCount, RecyclerView.VERTICAL, false)
        recyclerView?.layoutManager = layoutManager
    }

    override fun transImageFinish(position: Int, addedImageList: List<Uri>) {
        val i = Intent()
        i.putParcelableArrayListExtra(INTENT_ADD_PATH, ArrayList(addedImageList))
        i.putExtra(INTENT_POSITION, position)
        setResult(TRANS_IMAGES_RESULT_CODE, i)
        finish()
    }

    override fun takePicture(saveDir: String) {
        cameraUtil.takePicture(this, saveDir, TAKE_A_PICK_REQUEST_CODE)
    }

    private var savePath: String?
        get() = cameraUtil.savePath
        set(savePath) {
            cameraUtil.savePath = savePath
        }

    override fun showImageList(imageList: List<PickerListItem>, adapter: ImageAdapter) {
        setImageList(imageList, adapter)
    }

    override fun takePicture() {
        if (checkCameraPermission()) {
            pickerPresenter.takePicture()
        }
    }

    override fun onDeselect() {
        pickerPresenter.getPickerListItem()
    }

    override fun onClickImage(position: Int) {
        pickerPresenter.onClickImage(position)
    }

    override fun onClickThumbCount(position: Int) {
        pickerPresenter.onClickThumbCount(position)
    }

    override fun onCheckStateChange(position: Int, item: PickerListItem.Item) {
        adapter?.updatePickerListItem(position, item)
    }

    override fun showDetailView(position: Int) {
        val i = getDetailImageActivity(
            this,
            position
        )
        startActivityForResult(i, ENTER_DETAIL_REQUEST_CODE)
    }

    override fun showLimitReachedMessage(messageLimitReached: String) {
        recyclerView?.let {
            it.post {
                Snackbar.make(it, messageLimitReached, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionCheck.checkStoragePermission(PERMISSION_STORAGE)) return true
        } else return true
        return false
    }

    private fun checkCameraPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionCheck.checkCameraPermission(PERMISSION_CAMERA)) return true
        } else return true
        return false
    }

    override fun finishActivity() {
        val i = Intent()
        setResult(Activity.RESULT_OK, i)
        finish()
    }

    /**
     * when use startInAllView
     */
    override fun finishActivityWithResult(selectedImages: List<Uri>) {
        val i = Intent()
        setResult(Activity.RESULT_OK, i)
        i.putParcelableArrayListExtra(FishBun.INTENT_PATH, ArrayList(selectedImages))
        finish()
    }

    private fun setImageList(
        pickerList: List<PickerListItem>,
        imageAdapter: ImageAdapter
    ) {
        if (adapter == null) {
            adapter = PickerAdapter(imageAdapter, this)
            recyclerView?.adapter = adapter
        }
        adapter?.setPickerList(pickerList)
    }

    companion object {
        private const val TAG = "PickerActivity"
        fun getPickerActivityIntent(
            context: Context?,
            albumId: Long?,
            albumName: String?,
            albumPosition: Int
        ): Intent {
            val intent = Intent(context, PickerActivity::class.java)
            intent.putExtra(PickerIntentDataSourceImpl.ARG_ALBUM_ID, albumId)
            intent.putExtra(PickerIntentDataSourceImpl.ARG_ALBUM_NAME, albumName)
            intent.putExtra(PickerIntentDataSourceImpl.ARG_ALBUM_POSITION, albumPosition)
            return intent
        }
    }
}