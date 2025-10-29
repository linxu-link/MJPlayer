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