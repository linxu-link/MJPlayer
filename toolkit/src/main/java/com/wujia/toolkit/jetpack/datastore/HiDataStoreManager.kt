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

package com.wujia.toolkit.jetpack.datastore

import android.content.Context
import kotlinx.coroutines.flow.Flow

/**
 * 统一的 DataStore 管理类
 */
class HiDataStoreManager private constructor(context: Context) {

    companion object {
        @Volatile
        private var INSTANCE: HiDataStoreManager? = null

        fun getInstance(context: Context): HiDataStoreManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: HiDataStoreManager(context.applicationContext).also {
                    INSTANCE = it
                }
            }
        }
    }

    private val preferencesManager = PreferencesDataStoreManager.getInstance(context)
//    private val protoManager = ProtoDataStoreManager.getInstance(context)

    // Preferences DataStore 方法
    suspend fun <T> putPreference(key: String, value: T) {
        preferencesManager.putValue(key, value)
    }

    fun <T> getPreference(key: String, defaultValue: T): Flow<T> {
        return preferencesManager.getValue(key, defaultValue)
    }

    suspend fun removePreference(key: String) {
        preferencesManager.remove(key)
    }

    suspend fun clearPreferences() {
        preferencesManager.clear()
    }

    suspend fun clearProtoData() {
//        protoManager.clear()
    }

    // Proto DataStore 方法
//    suspend fun updateUsername(username: String) {
//        protoManager.updateUsername(username)
//    }
//
//    fun getUsername(): Flow<String> = protoManager.getUsername()
}