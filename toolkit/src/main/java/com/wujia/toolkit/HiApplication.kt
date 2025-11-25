package com.wujia.toolkit

import android.app.Application
import android.content.Context
import com.wujia.toolkit.utils.HiLog
import com.wujia.toolkit.utils.HiSp

open class HiApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        HiSp.init(this)
    }

}
