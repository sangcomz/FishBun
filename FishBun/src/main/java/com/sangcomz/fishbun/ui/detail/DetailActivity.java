package com.sangcomz.fishbun.ui.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sangcomz.fishbun.BaseActivity;
import com.sangcomz.fishbun.R;
import com.sangcomz.fishbun.adapter.view.DetailViewPagerAdapter;
import com.sangcomz.fishbun.define.Define;
import com.sangcomz.fishbun.util.RadioWithTextButton;

public class DetailActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private static final String TAG = "DetailActivity";

    private DetailController controller;
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

    private void initController() {
        controller = new DetailController(this);
    }

    private void initView() {
        btnDetailCount = findViewById(R.id.btn_detail_count);
        vpDetailPager = findViewById(R.id.vp_detail_pager);
        btnDetailBack = findViewById(R.id.btn_detail_back);
        btnDetailCount.unselect();
        btnDetailCount.setCircleColor(fishton.colorActionBar);
        btnDetailCount.setTextColor(fishton.colorActionBarTitle);
        btnDetailCount.setStrokeColor(fishton.colorSelectCircleStroke);
        btnDetailCount.setOnClickListener(this);
        btnDetailBack.setOnClickListener(this);
        initToolBar();
    }

    private void initValue() {
        Intent intent = getIntent();
        initPosition = intent.getIntExtra(Define.BUNDLE_NAME.POSITION.name(), -1);
    }

    private void initToolBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            uiUtil.setStatusBarColor(this, fishton.colorStatusBar);
        }
        if (fishton.isStatusBarLight
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            vpDetailPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

    }

    private void initAdapter() {
        if (fishton.pickerImages == null) {
            Toast.makeText(this, R.string.msg_error, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        onCheckStateChange(fishton.pickerImages[initPosition]);

        DetailViewPagerAdapter adapter = new DetailViewPagerAdapter(getLayoutInflater(), fishton.pickerImages);
        vpDetailPager.setAdapter(adapter);
        vpDetailPager.setCurrentItem(initPosition);

        vpDetailPager.addOnPageChangeListener(this);
    }

    public void onCheckStateChange(Uri image) {
        boolean isContained = fishton.selectedImages.contains(image);
        if (isContained) {
            updateRadioButton(btnDetailCount,
                    String.valueOf(fishton.selectedImages.indexOf(image) + 1));
        } else {
            btnDetailCount.unselect();
        }
    }


    public void updateRadioButton(RadioWithTextButton v, String text) {
        if (fishton.maxCount == 1)
            v.setDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.ic_done_white_24dp));
        else
            v.setText(text);
    }

    @Override
    public void onBackPressed() {
        finishActivity();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_detail_count) {
            Uri image = fishton.pickerImages[vpDetailPager.getCurrentItem()];
            if (fishton.selectedImages.contains(image)) {
                fishton.selectedImages.remove(image);
                onCheckStateChange(image);
            } else {
                if (fishton.selectedImages.size() == fishton.maxCount) {
                    Snackbar.make(v, fishton.messageLimitReached, Snackbar.LENGTH_SHORT).show();
                } else {
                    fishton.selectedImages.add(image);
                    onCheckStateChange(image);

                    if (fishton.isAutomaticClose && fishton.selectedImages.size() == fishton.maxCount)
                        finishActivity();
                }
            }

        } else if (id == R.id.btn_detail_back) {
            finishActivity();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        onCheckStateChange(fishton.pickerImages[position]);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    void finishActivity() {
        Intent i = new Intent();
        setResult(RESULT_OK, i);
        finish();
    }
}
