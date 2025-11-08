package com.wujia.toolkit.jetpack.startup

import android.content.Context
import androidx.startup.Initializer
import com.wujia.toolkit.utils.HiLog

open class StartUpInitializer : Initializer<Unit> {

    override fun dependencies(): List<Class<out Initializer<*>?>?> {
        return emptyList()
    }

    override fun create(context: Context) {
        HiLog.d("StartUpInitializer", "create")
        onCreate(context)
    }

    open fun onCreate(context: Context) {}
}
