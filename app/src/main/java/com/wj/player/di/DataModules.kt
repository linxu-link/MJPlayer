package com.wj.player.di

import android.content.Context
import com.wj.player.data.repository.video.VideoRepository
import com.wj.player.data.repository.video.VideoRepositoryImpl
import com.wj.player.data.source.local.video.VideoLocalDataSource
import com.wj.player.data.source.local.video.VideoLocalDataSourceImpl
import com.wj.player.data.source.local.video.room.VideoDao
import com.wj.player.data.source.local.video.room.VideoDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): VideoDatabase {
        return VideoDatabase.getInstance(context)
    }

    @Provides
    fun provideVideoDao(videoDatabase: VideoDatabase): VideoDao {
        return videoDatabase.videoDao()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Singleton
    @Binds
    abstract fun bindVideoLocalDataSource(
        videoLocalDataSourceImpl: VideoLocalDataSourceImpl,
    ): VideoLocalDataSource
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindVideoRepository(
        videoRepositoryImpl: VideoRepositoryImpl,
    ): VideoRepository
}
