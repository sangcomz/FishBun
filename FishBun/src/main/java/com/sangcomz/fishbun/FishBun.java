package com.sangcomz.fishbun;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.sangcomz.fishbun.adapter.image.ImageAdapter;

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

    public FishBunCreator setImageAdapter(ImageAdapter imageAdapter) {
        Fishton fishton = Fishton.getRefreshInstance();
        fishton.imageAdapter = imageAdapter;
        return new FishBunCreator(this);
    }
}
