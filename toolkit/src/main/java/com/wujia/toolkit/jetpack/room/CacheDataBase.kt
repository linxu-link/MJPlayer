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

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.wujia.toolkit.HiAppGlobal
import com.wujia.toolkit.utils.HiLog


@Database(entities = [CacheEntity::class], version = 1)
abstract class CacheDatabase : RoomDatabase() {
    abstract fun cacheDao(): CacheDao

    companion object {
        private const val TAG = "CacheDatabase"
        private const val DB_NAME = "um_db_name"

        @Volatile
        private var INSTANCE: CacheDatabase? = null

        fun getInstance(): CacheDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    HiAppGlobal.getApplication(),
                    CacheDatabase::class.java,
                    DB_NAME
                )
                    .allowMainThreadQueries()
                    .addCallback(DatabaseCallback())
                    .setJournalMode(JournalMode.AUTOMATIC)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            HiLog.i(TAG, "db on create")
        }

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            HiLog.i(TAG, "db on open")
        }

        override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
            super.onDestructiveMigration(db)
            HiLog.i(TAG, "db on destructiveMigration")
        }
    }
}
