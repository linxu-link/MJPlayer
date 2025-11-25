package com.wujia.toolkit.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * 精简的SharedPreferences封装类（单例+泛型支持）
 */
object HiSp {
    // 默认SP文件名
    private const val DEFAULT_SP_NAME = "AppSharedPrefs"
    private lateinit var sp: SharedPreferences

    /**
     * 初始化（需在Application中调用，传入Application Context）
     */
    fun init(context: Context) {
        // 使用Application Context避免内存泄漏
        sp = context.applicationContext.getSharedPreferences(DEFAULT_SP_NAME, Context.MODE_PRIVATE)
    }

    /**
     * 存储数据（支持String/Int/Boolean/Float/Long）
     */
    fun <T> put(key: String, value: T) {
        sp.edit {
            when (value) {
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Boolean -> putBoolean(key, value)
                is Float -> putFloat(key, value)
                is Long -> putLong(key, value)
                else -> throw IllegalArgumentException("不支持的类型：${value?.javaClass?.name}")
            }
        }
    }

    /**
     * 获取数据（带默认值）
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> get(key: String, defaultValue: T): T {
        return when (defaultValue) {
            is String -> sp.getString(key, defaultValue) as T
            is Int -> sp.getInt(key, defaultValue) as T
            is Boolean -> sp.getBoolean(key, defaultValue) as T
            is Float -> sp.getFloat(key, defaultValue) as T
            is Long -> sp.getLong(key, defaultValue) as T
            else -> throw IllegalArgumentException("不支持的类型：${defaultValue?.javaClass?.name}")
        }
    }

    /**
     * 删除指定key的数据
     */
    fun remove(key: String) {
        sp.edit { remove(key) }
    }

    /**
     * 清空所有数据
     */
    fun clear() {
        sp.edit { clear() }
    }
}
