package com.wj.player

import android.os.Bundle
import android.util.Log
import androidx.annotation.OptIn
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.CommandButton
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionResult
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.ListenableFuture

const val SESSION_ID = "session_multimedia"

// 定义按钮的 ID
private const val CUSTOM_COMMAND_FAVORITE = "ACTION_FAVORITE"

@OptIn(UnstableApi::class)
class PlaybackService : MediaSessionService() {
    private lateinit var mediaSession: MediaSession
    private lateinit var player: ExoPlayer

    // 创建自定义 SessionCommand
    private val favoriteCommand = SessionCommand(CUSTOM_COMMAND_FAVORITE, Bundle.EMPTY)

    override fun onCreate() {
        super.onCreate()
        Log.e("PlaybackService", "onCreate")
        initPlayer()
        initMediaSession()
        initNotification()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        Log.e("PlaybackService", "onGetSession ${controllerInfo.packageName}")
        return mediaSession
    }


    private fun initPlayer() {
        player = ExoPlayer.Builder(this)
            .build()
    }

    private fun initMediaSession() {
        // 创建自定义按钮
        val favoriteButton = CommandButton.Builder(CommandButton.ICON_HEART_FILLED)
            .setDisplayName("收藏")
            .setSessionCommand(favoriteCommand)
            .build()

        mediaSession = MediaSession.Builder(this, player)
            .setId(SESSION_ID)
            .setCustomLayout(ImmutableList.of(favoriteButton))
            .setCallback(mediaSessionCallback)
            .build()
    }

    private fun initNotification() {
        val mediaItem = MediaItem.Builder()
            .setUri("https://media.w3.org/2010/05/sintel/trailer.mp4".toUri())
            .setMediaId("song_123")
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle("视频标题")
                    .build(),
            )
            .build()
        player.setMediaItem(mediaItem)
        player.prepare() // 👈 必须调用
        player.repeatMode = Player.REPEAT_MODE_ALL // 👈 循环播放
        player.playWhenReady = true // 👈 触发播放，进入 PLAYING 状态
    }

    override fun onDestroy() {
        Log.e("PlaybackService", "onDestroy")
        player.release()
        mediaSession.release()
        super.onDestroy()
    }

    @UnstableApi
    private val mediaSessionCallback = object : MediaSession.Callback {

        override fun onPlaybackResumption(
            mediaSession: MediaSession,
            controller: MediaSession.ControllerInfo,
        ): ListenableFuture<MediaSession.MediaItemsWithStartPosition> {
            return super.onPlaybackResumption(mediaSession, controller)
        }

        override fun onConnect(
            session: MediaSession,
            controller: MediaSession.ControllerInfo,
        ): MediaSession.ConnectionResult {
            Log.e("PlaybackService", "onConnect")
            val sessionCommands = MediaSession.ConnectionResult.DEFAULT_SESSION_COMMANDS.buildUpon()
                .add(favoriteCommand)
                .build()
            return MediaSession.ConnectionResult.AcceptedResultBuilder(session)
                .setAvailableSessionCommands(sessionCommands)
                .build()
        }

        override fun onCustomCommand(
            session: MediaSession,
            controller: MediaSession.ControllerInfo,
            customCommand: SessionCommand,
            args: Bundle,
        ): ListenableFuture<SessionResult> {
            Log.e("PlaybackService", "onCustomCommand ${customCommand.customAction}")
            return super.onCustomCommand(session, controller, customCommand, args)
        }

    }
}
