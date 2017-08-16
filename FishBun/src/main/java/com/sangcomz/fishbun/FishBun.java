package com.sangcomz.fishbun;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.lang.ref.WeakReference;


public final class FishBun {

    protected WeakReference<Activity> activity = null;
    protected WeakReference<Fragment> fragment = null;

    public static FishBun with(Activity activity) {
        return new FishBun(activity, null);
    }

    public static FishBun with(Fragment fragment) {
        return new FishBun(null, fragment);
    }

    FishBun(Activity activity, Fragment fragment) {
        this.activity = new WeakReference<>(activity);
        this.fragment = new WeakReference<>(fragment);
    }

    public FishBunCreator MultiPageMode() {
        Bundle bundle = new Bundle();
        bundle.putAll(initBaseParams());
        bundle.putAll(initCustomizationParams());
        return new FishBunCreator(this, bundle);
    }

    private Bundle initBaseParams() {
        Bundle baseBundle = new Bundle();
        baseBundle.putInt(BaseParams.INT_MAX_COUNT.name(), 10);
        baseBundle.putInt(BaseParams.INT_MIN_COUNT.name(), 1);
        baseBundle.putBoolean(BaseParams.BOOLEAN_EXCEPT_GIF.name(), false);
        return baseBundle;
    }

    private Bundle initCustomizationParams() {
        Bundle customizationBundle = new Bundle();
        customizationBundle.putInt(CustomizationParams.INT_PHOTO_SPAN_COUNT.name(), 3);
        customizationBundle.putInt(CustomizationParams.INT_ALBUM_PORTRAIT_SPAN_COUNT.name(), 1);
        customizationBundle.putInt(CustomizationParams.INT_ALBUM_LANDSCAPE_SPAN_COUNT.name(), 2);
        customizationBundle.putInt(CustomizationParams.INT_COLOR_ACTION_BAR.name(), Color.parseColor("#3F51B5"));
        customizationBundle.putInt(CustomizationParams.INT_COLOR_ACTION_BAR_TITLE_COLOR.name(), Color.parseColor("#ffffff"));
        customizationBundle.putInt(CustomizationParams.INT_COLOR_STATUS_BAR.name(), Color.parseColor("#303F9F"));
        customizationBundle.putBoolean(CustomizationParams.BOOLEAN_IS_AUTOMATIC_CLOSE.name(), false);
        customizationBundle.putBoolean(CustomizationParams.BOOLEAN_IS_BUTTON.name(), false);
        customizationBundle.putBoolean(CustomizationParams.BOOLEAN_STYLE_STATUS_BAR_LIGHT.name(), false);
        customizationBundle.putBoolean(CustomizationParams.BOOLEAN_IS_CAMERA.name(), false);
        customizationBundle.putBoolean(CustomizationParams.BOOLEAN_IS_USE_DETAIL_VIEW.name(), true);
        return customizationBundle;
    }
}
