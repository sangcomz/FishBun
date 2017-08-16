package com.sangcomz.fishbun;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.sangcomz.fishbun.define.Define;
import com.sangcomz.fishbun.util.UiUtil;

public abstract class BaseActivity extends AppCompatActivity {
    protected Define define = new Define();
    protected UiUtil uiUtil = new UiUtil();

    protected int albumSize;
    protected int albumLandScapeSize;
    protected int albumPortraitSize;
    protected String titleActionBar;
    protected boolean isButton;
    protected String titleAllView;

    protected int photoSpanCount;
    protected int colorActionBar;
    protected int colorActionBarTitle;
    protected int maxCount;
    protected int minCount;
    protected boolean statusBarLight;
    protected Drawable homeAsUpIndicatorDrawable;
    protected Drawable okButtonDrawable;
    protected boolean isCamera;
    protected boolean isAutomaticClose;
    protected boolean isUseDetailView;
    protected String menuText;
    protected int colorMenuText;
    protected String messageNothingSelected;
    protected String messageLimitReached;
    protected Boolean exceptGif;
    protected int colorStatusBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
        initValue();
        super.onCreate(savedInstanceState);
    }

    private void initValue() {
        Intent intent = getIntent();
        albumSize = intent.getIntExtra(CustomizationParams.INT_ALBUM_THUMBNAIL_SIZE.name(),
                (int) getResources().getDimension(R.dimen.album_thum_size));
        albumLandScapeSize = intent.getIntExtra(CustomizationParams.INT_ALBUM_LANDSCAPE_SPAN_COUNT.name(), 2);
        albumPortraitSize = intent.getIntExtra(CustomizationParams.INT_ALBUM_PORTRAIT_SPAN_COUNT.name(), 1);
        colorActionBar = intent.getIntExtra(CustomizationParams.INT_COLOR_ACTION_BAR.name(), Integer.MAX_VALUE);
        colorActionBarTitle = intent.getIntExtra(CustomizationParams.INT_COLOR_ACTION_BAR_TITLE_COLOR.name(), Integer.MAX_VALUE);
        maxCount = intent.getIntExtra(BaseParams.INT_MAX_COUNT.name(), define.DEFAULT_MAX_COUNT);
        minCount = intent.getIntExtra(BaseParams.INT_MIN_COUNT.name(), define.DEFAULT_MIN_COUNT);
        colorStatusBar = intent.getIntExtra(CustomizationParams.INT_COLOR_STATUS_BAR.name(), Integer.MAX_VALUE);
        statusBarLight = intent.getBooleanExtra(CustomizationParams.BOOLEAN_STYLE_STATUS_BAR_LIGHT.name(), false);
        isButton = intent.getBooleanExtra(CustomizationParams.BOOLEAN_IS_BUTTON.name(), false);
        exceptGif = intent.getBooleanExtra(BaseParams.BOOLEAN_EXCEPT_GIF.name(), false);
        colorMenuText = intent.getIntExtra(CustomizationParams.INT_COLOR_MENU_TEXT.name(), Integer.MAX_VALUE);
        titleActionBar = intent.getStringExtra(CustomizationParams.STRING_TITLE_ACTIONBAR.name());
        menuText = intent.getStringExtra(CustomizationParams.STRING_TEXT_MENU.name());
        messageNothingSelected = intent.getStringExtra(CustomizationParams.STRING_MESSAGE_NOTHING_SELECTED.name());
        titleAllView = intent.getStringExtra(CustomizationParams.STRING_TITLE_ALBUM_ALL_VIEW.name());
        homeAsUpIndicatorDrawable = uiUtil.getBitmapToDrawable(getResources(), (Bitmap) intent.getParcelableExtra(CustomizationParams.DRAWABLE_HOME_AS_UP_INDICATOR.name()));
        okButtonDrawable = uiUtil.getBitmapToDrawable(getResources(), (Bitmap) intent.getParcelableExtra(CustomizationParams.DRAWABLE_OK_BUTTON_DRAWABLE.name()));
        photoSpanCount = intent.getIntExtra(CustomizationParams.INT_PHOTO_SPAN_COUNT.name(), 3);
        isCamera = intent.getBooleanExtra(CustomizationParams.BOOLEAN_IS_CAMERA.name(), false);
        isAutomaticClose = intent.getBooleanExtra(CustomizationParams.BOOLEAN_IS_AUTOMATIC_CLOSE.name(), false);
        isUseDetailView = intent.getBooleanExtra(CustomizationParams.BOOLEAN_IS_USE_DETAIL_VIEW.name(), true);
        messageLimitReached = intent.getStringExtra(CustomizationParams.STRING_MESSAGE_LIMIT_REACHED.name());
    }
}
