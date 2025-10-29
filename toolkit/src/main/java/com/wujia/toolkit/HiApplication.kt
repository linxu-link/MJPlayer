package com.wujia.toolkit

import android.app.Application
import android.content.Context
import com.wujia.toolkit.utils.HiLog

open class HiApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        initLog()
    }

    private fun initLog() {
        HiLog.setDelegate(object : HiLog.HiLogDelegate {
            override fun e(tag: String, msg: String, vararg obj: Any) {
                android.util.Log.e(tag, msg.format(*obj))
            }

            override fun w(tag: String, msg: String, vararg obj: Any) {
                android.util.Log.w(tag, msg.format(*obj))
            }

            override fun i(tag: String, msg: String, vararg obj: Any) {
                android.util.Log.i(tag, msg.format(*obj))
            }

            override fun d(tag: String, msg: String, vararg obj: Any) {
                android.util.Log.d(tag, msg.format(*obj))
            }

            override fun printErrStackTrace(
                tag: String,
                tr: Throwable,
                format: String,
                vararg obj: Any
            ) {
                android.util.Log.e(tag, String.format(format, *obj), tr)
            }
        })
    }
}