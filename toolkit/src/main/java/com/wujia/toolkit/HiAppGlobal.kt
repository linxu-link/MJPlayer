package com.wujia.toolkit

import android.annotation.SuppressLint
import android.content.Context

class HiAppGlobal {

    companion object {
        // 使用反射获取全局Context
        @SuppressLint("PrivateApi")
        fun getApplication(): Context {
            try {
                val activityThreadClass = Class.forName("android.app.ActivityThread")
                val currentApplicationMethod =
                    activityThreadClass.getDeclaredMethod("currentApplication")
                return currentApplicationMethod.invoke(null) as Context
            } catch (e: Exception) {
                e.printStackTrace()
            }
            throw NullPointerException("Reflect application failed.")
        }
    }
}
