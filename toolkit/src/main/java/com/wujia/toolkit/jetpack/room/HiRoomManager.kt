/*
 * Copyright 2025 WuJia
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
