package com.sangcomz.fishbun.ui.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.sangcomz.fishbun.BaseActivity;
import com.sangcomz.fishbun.R;
import com.sangcomz.fishbun.adapter.view.DetailViewPagerAdapter;
import com.sangcomz.fishbun.define.Define;
import com.sangcomz.fishbun.util.RadioWithTextButton;
import com.sangcomz.fishbun.util.UiUtil;

public class DetailActivity extends BaseActivity
        implements DetailView, View.OnClickListener, ViewPager.OnPageChangeListener {

    private static final String TAG = "DetailActivity";

    private DetailPresenter presenter;
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
        setContentView(R.layout.activity_detail_activity);
        initPresenter();
        initValue();
        initView();
        initPager();
    }

    private void initPresenter() {
        presenter = new DetailPresenter(this);
    }

    private void initView() {
        vpDetailPager = findViewById(R.id.vp_detail_pager);
        btnDetailCount = findViewById(R.id.btn_detail_count);
        btnDetailBack = findViewById(R.id.btn_detail_back);

        initCountButton();
        initBackButton();
        initToolBar();
    }

    private void initValue() {
        Intent intent = getIntent();
        initPosition = intent.getIntExtra(Define.BUNDLE_NAME.POSITION.name(), -1);
    }

    private void initToolBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UiUtil.setStatusBarColor(this, fishton.getColorStatusBar());
        }
        if (fishton.isStatusBarLight()
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            vpDetailPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    private void initCountButton(){
        btnDetailCount.unselect();
        btnDetailCount.setCircleColor(fishton.getColorActionBar());
        btnDetailCount.setTextColor(fishton.getColorActionBarTitle());
        btnDetailCount.setStrokeColor(fishton.getColorSelectCircleStroke());
        btnDetailCount.setOnClickListener(this);
    }

    private void initBackButton(){
        btnDetailBack.setOnClickListener(this);
    }

    private void initPager(){
        if (fishton.getPickerImages().isEmpty()) {
            Toast.makeText(this, R.string.msg_error, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        onCheckStateChange(fishton.getPickerImages().get(initPosition));

        DetailViewPagerAdapter adapter = new DetailViewPagerAdapter(fishton.getPickerImages());

        vpDetailPager.setAdapter(adapter);
        vpDetailPager.setCurrentItem(initPosition);
        vpDetailPager.addOnPageChangeListener(this);
    }

    public void onCheckStateChange(Uri image) {
        boolean isContained = fishton.getSelectedImages().contains(image);
        if (isContained) {
            updateRadioButton(btnDetailCount,
                    String.valueOf(fishton.getSelectedImages().indexOf(image) + 1));
        } else {
            btnDetailCount.unselect();
        }
    }


    public void updateRadioButton(RadioWithTextButton v, String text) {
        if (fishton.getMaxCount() == 1)
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
        if (fishton.getPickerImages().isEmpty()) return;
        int id = v.getId();
        if (id == R.id.btn_detail_count) {
            Uri image = fishton.getPickerImages().get(vpDetailPager.getCurrentItem());
            if (fishton.getSelectedImages().contains(image)) {
                fishton.getSelectedImages().remove(image);
                onCheckStateChange(image);
            } else {
                if (fishton.getSelectedImages().size() == fishton.getMaxCount()) {
                    Snackbar.make(v, fishton.getMessageLimitReached(), Snackbar.LENGTH_SHORT).show();
                } else {
                    fishton.getSelectedImages().add(image);
                    onCheckStateChange(image);

                    if (fishton.isAutomaticClose() && fishton.getSelectedImages().size() == fishton.getMaxCount())
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
        if (!fishton.getPickerImages().isEmpty()) {
            onCheckStateChange(fishton.getPickerImages().get(position));
        }
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
