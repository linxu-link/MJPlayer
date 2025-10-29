package com.wujia.toolkit.jetpack.room

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wujia.toolkit.utils.HiLog

private const val TAG = "HiRoomManager"

object HiRoomManager {
    private val cacheDao = CacheDatabase.getInstance().cacheDao()

    fun save(key: String, value: String) {
        cacheDao.save(CacheEntity(key, value))
    }

    fun get(key: String): String? {
        return cacheDao.get(key)?.value
    }

    fun delete(key: String) {
        HiLog.i(TAG, "[delete] $key")
        cacheDao.delete(key)
    }

    fun clear() {
        HiLog.i(TAG, "[clear]")
        cacheDao.clear()
    }

    fun toJson(value: Any): String {
        return Gson().toJson(value)
    }

    inline fun <reified T> toObject(json: String): List<T> {
        val type = object : TypeToken<ArrayList<T>>() {}.type
        return Gson().fromJson(json, type)
    }
}
