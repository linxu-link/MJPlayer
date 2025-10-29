package com.wj.player.ui.view.player.base

import android.net.Uri
import androidx.media3.common.Player
import kotlinx.coroutines.flow.StateFlow

interface IVideoPlayerController {
    val playerState: StateFlow<PlayerState>

    fun getPlayer(): Player
    fun loadMedia(uri: Uri)
    fun playPause()
    fun seekTo(position: Long)
    fun setPlaybackSpeed(speed: Float)
    fun release()
}
