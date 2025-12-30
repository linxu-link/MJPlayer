package com.wj.media.server

import androidx.media3.common.Player
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.wujia.toolkit.utils.HiLog

class MediaPlaybackService : MediaSessionService() {

    private lateinit var mediaSession: MediaSession
    private lateinit var player: Player

    override fun onCreate() {
        super.onCreate()
        HiLog.d("MediaPlaybackService onCreate")
        mediaSession = MediaSession.Builder(this, player).build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession {
        return mediaSession
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
        mediaSession.release()
    }
}
