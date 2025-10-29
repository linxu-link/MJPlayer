package com.wujia.toolkit.jetpack.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "table_cache")
data class CacheEntity(
    @PrimaryKey
    @ColumnInfo(name = "key")
    val key: String,

    @ColumnInfo(name = "value")
    var value: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as CacheEntity
        return key == other.key && value == other.value
    }

    override fun hashCode(): Int = 31 * key.hashCode() + value.hashCode()

    override fun toString(): String = "CacheEntity(key=$key, value=$value)"
}
