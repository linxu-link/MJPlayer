package com.wj.media

import androidx.media3.common.C
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import com.wj.media.controller.PlaybackState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class IExoPlayerListener(private val player: Player) : Player.Listener {

    private var progressJob: Job? = null

    init {
        startProgressTracking()
    }


    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)
        val playbackState = when (playbackState) {
            Player.STATE_READY -> PlaybackState.READY
            Player.STATE_BUFFERING -> PlaybackState.BUFFERING
            Player.STATE_ENDED -> PlaybackState.ENDED
            Player.STATE_IDLE -> PlaybackState.IDLE
            else -> PlaybackState.IDLE
        }
        onPlaybackStateChanged(playbackState)
    }


    override fun onIsPlayingChanged(isPlaying: Boolean) {
        super.onIsPlayingChanged(isPlaying)
    }

    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
    }

    abstract fun onPlaybackStateChanged(state: PlaybackState)

    abstract fun onPlayPositionChanged(position: Long, duration: Long)

    // 启动实时进度监听
    private fun startProgressTracking() {
        stopProgressTracking()
        progressJob = CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                if (player.isPlaying) {
                    val currentPos = getCurrentPosition()
                    val duration = getDuration()
                    onPlayPositionChanged(currentPos, duration)
                }
                delay(16)
            }
        }
    }

    // 停止进度监听（如页面销毁时）
    private fun stopProgressTracking() {
        progressJob?.cancel()
    }

    private fun getCurrentPosition(): Long {
        val currentPosition = player.currentPosition
        if (currentPosition == C.TIME_UNSET) {
            return 0
        }
        return currentPosition
    }

    private fun getDuration(): Long {
        val duration = player.duration
        if (duration == C.TIME_UNSET) {
            return 0
        }
        return duration
    }

}
