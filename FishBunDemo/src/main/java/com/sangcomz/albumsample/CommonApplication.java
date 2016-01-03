package com.sangcomz.albumsample;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by sangc on 2016-01-03.
 */
public class CommonApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
