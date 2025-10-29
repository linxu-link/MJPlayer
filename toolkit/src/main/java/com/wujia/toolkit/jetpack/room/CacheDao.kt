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
