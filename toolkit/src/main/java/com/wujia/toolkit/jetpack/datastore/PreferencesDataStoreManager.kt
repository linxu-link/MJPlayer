package com.wujia.toolkit.jetpack.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

// DataStore 名称常量
private const val DATA_STORE_NAME = "app_preferences"

// 扩展属性
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)

/**
 * DataStore 工具类 - Preferences 版本
 */
class PreferencesDataStoreManager private constructor(private val context: Context) {

    companion object {
        @Volatile
        private var INSTANCE: PreferencesDataStoreManager? = null

        fun getInstance(context: Context): PreferencesDataStoreManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: PreferencesDataStoreManager(context.applicationContext).also {
                    INSTANCE = it
                }
            }
        }
    }

    private val dataStore: DataStore<Preferences> = context.dataStore

    // 存储各种类型的数据
    suspend fun <T> putValue(key: String, value: T) {
        when (value) {
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            is Double -> putDouble(key, value)
            is Boolean -> putBoolean(key, value)
            is Set<*> -> putStringSet(key, value as Set<String>)
            else -> throw IllegalArgumentException("Unsupported type: ${value?.javaClass?.simpleName}")
        }
    }

    // 获取各种类型的数据
    fun <T> getValue(key: String, defaultValue: T): Flow<T> {
        return when (defaultValue) {
            is String -> getString(key, defaultValue) as Flow<T>
            is Int -> getInt(key, defaultValue) as Flow<T>
            is Long -> getLong(key, defaultValue) as Flow<T>
            is Float -> getFloat(key, defaultValue) as Flow<T>
            is Double -> getDouble(key, defaultValue) as Flow<T>
            is Boolean -> getBoolean(key, defaultValue) as Flow<T>
            is Set<*> -> getStringSet(key, defaultValue as Set<String>) as Flow<T>
            else -> throw IllegalArgumentException("Unsupported type: ${defaultValue?.javaClass?.simpleName}")
        }
    }

    // 具体类型的存储方法
    suspend fun putString(key: String, value: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = value
        }
    }

    suspend fun putInt(key: String, value: Int) {
        dataStore.edit { preferences ->
            preferences[intPreferencesKey(key)] = value
        }
    }

    suspend fun putLong(key: String, value: Long) {
        dataStore.edit { preferences ->
            preferences[longPreferencesKey(key)] = value
        }
    }

    suspend fun putFloat(key: String, value: Float) {
        dataStore.edit { preferences ->
            preferences[floatPreferencesKey(key)] = value
        }
    }

    suspend fun putDouble(key: String, value: Double) {
        dataStore.edit { preferences ->
            preferences[doublePreferencesKey(key)] = value
        }
    }

    suspend fun putBoolean(key: String, value: Boolean) {
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(key)] = value
        }
    }

    suspend fun putStringSet(key: String, value: Set<String>) {
        dataStore.edit { preferences ->
            preferences[stringSetPreferencesKey(key)] = value
        }
    }

    // 具体类型的读取方法
    fun getString(key: String, defaultValue: String): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[stringPreferencesKey(key)] ?: defaultValue
            }
    }

    fun getInt(key: String, defaultValue: Int): Flow<Int> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[intPreferencesKey(key)] ?: defaultValue
            }
    }

    fun getLong(key: String, defaultValue: Long): Flow<Long> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[longPreferencesKey(key)] ?: defaultValue
            }
    }

    fun getFloat(key: String, defaultValue: Float): Flow<Float> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[floatPreferencesKey(key)] ?: defaultValue
            }
    }

    fun getDouble(key: String, defaultValue: Double): Flow<Double> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[doublePreferencesKey(key)] ?: defaultValue
            }
    }

    fun getBoolean(key: String, defaultValue: Boolean): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[booleanPreferencesKey(key)] ?: defaultValue
            }
    }

    fun getStringSet(key: String, defaultValue: Set<String>): Flow<Set<String>> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[stringSetPreferencesKey(key)] ?: defaultValue
            }
    }

    // 删除数据
    suspend fun remove(key: String) {
        dataStore.edit { preferences ->
            preferences.remove(stringPreferencesKey(key))
            preferences.remove(intPreferencesKey(key))
            preferences.remove(longPreferencesKey(key))
            preferences.remove(floatPreferencesKey(key))
            preferences.remove(doublePreferencesKey(key))
            preferences.remove(booleanPreferencesKey(key))
            preferences.remove(stringSetPreferencesKey(key))
        }
    }

    // 清空所有数据
    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}