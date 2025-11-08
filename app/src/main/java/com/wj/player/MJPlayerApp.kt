package com.wj.player

import android.content.Context
import androidx.multidex.BuildConfig
import com.wujia.toolkit.HiApplication
import com.wujia.toolkit.utils.HiLog
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import timber.log.Timber.DebugTree

private const val PREFIX = "MJPlayer_"

@HiltAndroidApp
class MJPlayerApp : HiApplication() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
        HiLog.i("onCreate")
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        initLog()
    }

    private fun initLog() {
        HiLog.setDelegate(
            object : HiLog.HiLogDelegate {
                override fun e(tag: String, msg: String, vararg obj: Any) {
                    android.util.Log.e(PREFIX + tag, msg.format(*obj))
                }

                override fun w(tag: String, msg: String, vararg obj: Any) {
                    android.util.Log.w(PREFIX + tag, msg.format(*obj))
                }

                override fun i(tag: String, msg: String, vararg obj: Any) {
                    android.util.Log.i(PREFIX + tag, msg.format(*obj))
                }

                override fun d(tag: String, msg: String, vararg obj: Any) {
                    android.util.Log.d(PREFIX + tag, msg.format(*obj))
                }

                override fun printErrStackTrace(
                    tag: String,
                    tr: Throwable,
                    format: String,
                    vararg obj: Any,
                ) {
                    android.util.Log.e(PREFIX + tag, String.format(format, *obj), tr)
                }
            },
        )
    }
}
