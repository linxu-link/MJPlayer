package com.wj.player.ui.view.player.base

// 播放器状态
data class PlayerState(
    val isPlaying: Boolean = false,
    val currentPosition: Long = 0L,
    val duration: Long = 0L,
    val playbackState: PlaybackState = PlaybackState.IDLE,
    val bufferedPercentage: Int = 0,
    val playbackSpeed: Float = 1.0f,
)

enum class PlaybackState {
    IDLE, READY, BUFFERING, ENDED, ERROR
}
