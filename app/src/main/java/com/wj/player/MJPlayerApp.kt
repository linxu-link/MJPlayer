package com.wj.player

import androidx.multidex.BuildConfig
import com.wujia.toolkit.HiApplication
import com.wujia.toolkit.utils.HiLog
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import timber.log.Timber.DebugTree

@HiltAndroidApp
class MJPlayerApp : HiApplication() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
        HiLog.i("onCreate")
    }
}
