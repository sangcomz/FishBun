package com.sangcomz.fishbun.ui.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import com.sangcomz.fishbun.BaseActivity;
import com.sangcomz.fishbun.R;
import com.sangcomz.fishbun.adapter.DetailViewAdapter;
import com.sangcomz.fishbun.define.Define;
import com.sangcomz.fishbun.util.CustomPagerSnapHelper;
import com.sangcomz.fishbun.util.RadioWithTextButton;

import java.util.ArrayList;

public class DetailActivity extends BaseActivity implements CustomPagerSnapHelper.OnPageChangeListener {
    private ArrayList<Uri> pickedImages = new ArrayList<>();
    private DetailController controller;
    private Uri[] images;
    private int position;
    private RecyclerView recyclerDetailView;
    private RadioWithTextButton btnDetailCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
        setContentView(R.layout.activity_detail_actiivy);
        initValue();
        initView();
        initAdapter();
        initController();
    }

    private void initController() {
        controller = new DetailController(this);
    }

    private void initView() {
        initToolBar();
        recyclerDetailView = (RecyclerView) findViewById(R.id.recycler_detail_view);
        btnDetailCount = (RadioWithTextButton) findViewById(R.id.btn_detail_count);
        btnDetailCount.unselect();
        btnDetailCount.setCircleColor(colorActionBar);
        btnDetailCount.setTextColor(colorActionBarTitle);
    }

    private void initValue() {
        Intent intent = getIntent();
        pickedImages = intent.getParcelableArrayListExtra(Define.BUNDLE_NAME.PICKED_IMAGES.name());
        position = intent.getIntExtra(Define.BUNDLE_NAME.POSITION.name(), -1);
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
    }

    private void initAdapter() {
        DetailViewAdapter detailViewAdapter
                = new DetailViewAdapter(images,
                controller,
                pickedImages,
                maxCount,
                messageLimitReached,
                isAutomaticClose);

        recyclerDetailView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL,
                false));
        recyclerDetailView.scrollToPosition(position);
        recyclerDetailView.setAdapter(detailViewAdapter);
        final CustomPagerSnapHelper snapHelper = new CustomPagerSnapHelper(recyclerDetailView.getLayoutManager());
        snapHelper.attachToRecyclerView(recyclerDetailView);
        snapHelper.setOnPageChangeListener(this);
    }

    private void onCheckStateChange(Uri image) {
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
    public void onPageChanged(int position) {
        onCheckStateChange(images[position]);
    }
}
