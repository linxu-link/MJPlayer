package com.wj.media


import android.os.Bundle
import android.util.Log
import androidx.annotation.OptIn
import androidx.core.net.toUri
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionResult
import com.google.common.util.concurrent.ListenableFuture
import com.wujia.toolkit.utils.HiLog

const val SESSION_ID = "session_multimedia"

private const val TAG = "PlaybackService"

@OptIn(UnstableApi::class)
class PlaybackService : MediaSessionService() {
    private lateinit var mediaSession: MediaSession
    private lateinit var player: ExoPlayer

    override fun onCreate() {
        super.onCreate()
        Log.e(TAG, "[onCreate]")
        initPlayer()
        initMediaSession()
        initMediaItem()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        HiLog.i(TAG, "[onGetSession] ${controllerInfo.packageName}")
        return mediaSession
    }


    private fun initPlayer() {
        player = ExoPlayer.Builder(this)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(C.USAGE_MEDIA) // 用途：媒体播放
                    .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE) // 类型：电影
                    .build(),
                true,// 自动处理音频焦点
            )
            .build()
            .apply {
                repeatMode = Player.REPEAT_MODE_ALL // 循环播放
                playWhenReady = true
            }
    }

    private fun initMediaSession() {
        mediaSession = MediaSession.Builder(this, player)
            .setId(SESSION_ID)
            .setCallback(mediaSessionCallback)
            .build()
    }

    private fun initMediaItem() {
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
        player.prepare()
        player.repeatMode = Player.REPEAT_MODE_ALL
        player.playWhenReady = true
    }

    override fun onDestroy() {
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
            return MediaSession.ConnectionResult.AcceptedResultBuilder(session)
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

