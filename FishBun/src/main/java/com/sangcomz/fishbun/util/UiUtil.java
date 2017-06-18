package com.sangcomz.fishbun.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by sangc on 2015-11-20.
 */
public class UiUtil {
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setStatusBarColor(Activity activity, int colorStatusBar) {
        if (colorStatusBar == Integer.MAX_VALUE) return;
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(colorStatusBar);
    }

    public boolean isLandscape(Context context) {
        Configuration configuration = context.getResources().getConfiguration();
        return configuration.orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public Drawable getBitmapToDrawable(Resources resources, Bitmap bitmap) {
        if (bitmap == null) return null;
        return new BitmapDrawable(resources, bitmap);
    }
}
