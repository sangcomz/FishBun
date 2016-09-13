package com.sangcomz.fishbundemo;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

public class CommonApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
