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

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface CacheDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(entity: CacheEntity)

    @Query("SELECT * FROM table_cache WHERE `key` = :key LIMIT 1")
    fun get(key: String): CacheEntity?

    @Query("DELETE FROM table_cache WHERE `key` = :key")
    fun delete(key: String)

    @Query("SELECT * FROM table_cache")
    fun getAll(): List<CacheEntity>

    @Query("DELETE FROM table_cache")
    fun clear()
}
