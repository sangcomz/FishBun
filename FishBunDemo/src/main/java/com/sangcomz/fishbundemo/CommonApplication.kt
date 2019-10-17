package com.sangcomz.fishbundemo

import android.app.Application
import com.squareup.leakcanary.LeakCanary

class CommonApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        LeakCanary.install(this)
    }
}
