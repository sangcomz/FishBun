package com.sangcomz.fishbun.ui.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import com.sangcomz.fishbun.BaseActivity;
import com.sangcomz.fishbun.R;
import com.sangcomz.fishbun.adapter.DetailViewPagerAdapter;
import com.sangcomz.fishbun.define.Define;
import com.sangcomz.fishbun.util.RadioWithTextButton;

import java.util.ArrayList;

public class DetailActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private static final String TAG = "DetailActivity";

    private DetailController controller;
    private Uri[] images;
    private int initPosition;
    private RadioWithTextButton btnDetailCount;
    private ViewPager vpDetailPager;
    private ImageButton btnDetailBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
        setContentView(R.layout.activity_detail_actiivy);
        initController();
        initValue();
        initView();
        initAdapter();
        initToolBar();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        try {
            outState.putParcelableArrayList(define.SAVE_INSTANCE_PICK_IMAGES, controller.getPickedImage());
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(outState);
        // Restore state members from saved instance
        try {
            ArrayList<Uri> pickedImages = outState.getParcelableArrayList(define.SAVE_INSTANCE_PICK_IMAGES);
            controller.setPickedImages(pickedImages);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

    private void initController() {
        ArrayList<Uri> pickedImages = getIntent().getParcelableArrayListExtra(Define.BUNDLE_NAME.PICKED_IMAGES.name());
        controller = new DetailController(this);
        controller.setPickedImages(pickedImages);
    }

    private void initView() {
        btnDetailCount = (RadioWithTextButton) findViewById(R.id.btn_detail_count);
        vpDetailPager = (ViewPager) findViewById(R.id.vp_detail_pager);
        btnDetailBack = (ImageButton) findViewById(R.id.btn_detail_back);
        btnDetailCount.unselect();
        btnDetailCount.setCircleColor(colorActionBar);
        btnDetailCount.setTextColor(colorActionBarTitle);
        btnDetailCount.setOnClickListener(this);
        btnDetailBack.setOnClickListener(this);
        initToolBar();
    }

    private void initValue() {
        Intent intent = getIntent();
        initPosition = intent.getIntExtra(Define.BUNDLE_NAME.POSITION.name(), -1);
        Parcelable[] parcelables = intent.getBundleExtra(Define.BUNDLE_NAME.BUNDLE.name()).getParcelableArray(Define.BUNDLE_NAME.IMAGES.name());
        if (parcelables != null) {
            images = new Uri[parcelables.length];
            System.arraycopy(parcelables, 0, images, 0, parcelables.length);
        }
    }

    private void initToolBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            uiUtil.setStatusBarColor(this, colorStatusBar);
        }
        if (statusBarLight
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            vpDetailPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

    }

    private void initAdapter() {
        onCheckStateChange(images[initPosition]);

        DetailViewPagerAdapter adapter = new DetailViewPagerAdapter(getLayoutInflater(), images);
        vpDetailPager.setAdapter(adapter);
        vpDetailPager.setCurrentItem(initPosition);

        vpDetailPager.addOnPageChangeListener(this);
    }

    public void onCheckStateChange(Uri image) {
        ArrayList<Uri> pickedImages = controller.getPickedImage();
        boolean isContained = pickedImages.contains(image);
        if (isContained) {
            updateRadioButton(btnDetailCount, String.valueOf(pickedImages.indexOf(image) + 1));
        } else {
            btnDetailCount.unselect();
        }
    }


    public void updateRadioButton(RadioWithTextButton v, String text) {
        if (maxCount == 1)
            v.setDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.ic_done_white_24dp));
        else
            v.setText(text);
    }

    @Override
    public void onBackPressed() {
        controller.finishActivity();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_detail_count) {
            Uri image = images[vpDetailPager.getCurrentItem()];
            if (controller.isAdded(image)) {
                controller.removePickedImage(image);
            } else {
                if (controller.getPickedImage().size() == maxCount) {
                    Snackbar.make(v, messageLimitReached, Snackbar.LENGTH_SHORT).show();
                } else {
                    controller.addPickedImage(image);
                    if (isAutomaticClose && controller.getPickedImage().size() == maxCount)
                        controller.finishActivity();
                }
            }

        } else if (id == R.id.btn_detail_back) {
            controller.finishActivity();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        onCheckStateChange(images[position]);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
