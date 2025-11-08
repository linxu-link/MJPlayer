package com.wj.player.ui.view.player

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.wj.player.ui.view.player.base.BasePlayerController
import com.wj.player.ui.view.player.base.PlaybackState
import com.wj.player.ui.view.player.base.PlayerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ExoplayerControllerImpl 是 ExoPlayer 的具体实现类，继承自 BasePlayerController。
 */
@UnstableApi
class ExoplayerControllerImpl(
    context: Context,
    private val config: PlayerConfig,
) : BasePlayerController() {

    private val _playerState = MutableStateFlow(PlayerState())
    override val playerState = _playerState.asStateFlow()

    // ExoPlayer 实例
    val exoPlayer: ExoPlayer = ExoPlayer.Builder(context)
        .setSeekForwardIncrementMs(config.seekForwardIncrementMs)
        .setSeekBackIncrementMs(config.seekBackIncrementMs)
        .build()
        .apply {
            repeatMode = config.repeatMode
        }

    init {
        initializePlayer()
    }

    override fun initializePlayer() {
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                val playbackState = when (state) {
                    Player.STATE_READY -> PlaybackState.READY
                    Player.STATE_BUFFERING -> PlaybackState.BUFFERING
                    Player.STATE_ENDED -> PlaybackState.ENDED
                    Player.STATE_IDLE -> PlaybackState.IDLE
                    else -> PlaybackState.IDLE
                }
                handlePlaybackStateChange(exoPlayer.isPlaying, playbackState)
            }

            override fun onPositionDiscontinuity(
                oldPosition: Player.PositionInfo,
                newPosition: Player.PositionInfo,
                reason: Int,
            ) {
                super.onPositionDiscontinuity(oldPosition, newPosition, reason)
                handlePositionUpdate(newPosition.positionMs, exoPlayer.duration)
            }

            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                handleError(error)
            }

            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
                super.onPlaybackParametersChanged(playbackParameters)
                handlePlaybackSpeedUpdate(playbackParameters.speed)
            }

            override fun onEvents(player: Player, events: Player.Events) {
                super.onEvents(player, events)
                // 更新缓冲状态和当前播放位置
                handleBufferingUpdate(player.bufferedPercentage)
            }
        })
    }

    override fun loadMedia(uri: Uri) {
        exoPlayer.setMediaItem(MediaItem.fromUri(uri))
        exoPlayer.prepare()
    }

    override fun playPause() {
        if (exoPlayer.isPlaying) exoPlayer.pause() else exoPlayer.play()
        handlePlaybackStateChange(exoPlayer.isPlaying, PlaybackState.READY)
    }

    override fun seekTo(position: Long) {
        exoPlayer.seekTo(position)
        handlePositionUpdate(position, exoPlayer.duration)
    }

    override fun setPlaybackSpeed(speed: Float) {
        exoPlayer.playbackParameters = PlaybackParameters(speed)
        updateState { copy(playbackSpeed = speed) }
    }

    override fun release() {
        exoPlayer.release()
    }

    override fun getPlayer(): Player {
        return exoPlayer
    }

    override fun updateState(update: PlayerState.() -> PlayerState) {
        CoroutineScope(Dispatchers.Main).launch {
            _playerState.value = _playerState.value.update()
        }
    }
}

// 辅助函数用于复制配置
@UnstableApi
private fun PlayerConfig.copy(
    repeatMode: Int = this.repeatMode,
    seekForwardIncrementMs: Long = this.seekForwardIncrementMs,
    seekBackIncrementMs: Long = this.seekBackIncrementMs,
    cacheConfig: PlayerConfig.CacheConfig? = this.cacheConfig,
) = PlayerConfig(repeatMode, seekForwardIncrementMs, seekBackIncrementMs, cacheConfig)

// 播放器配置类
class PlayerConfig(
    val repeatMode: Int = Player.REPEAT_MODE_OFF,
    val seekForwardIncrementMs: Long = 15_000,
    val seekBackIncrementMs: Long = 5_000,
    val cacheConfig: CacheConfig? = null,
) {

    class CacheConfig(
        val cacheSize: Long = 100 * 1024 * 1024, // 100MB
        val cacheDirectory: String = "exoplayer_cache",
    )
}

// 播放器建造者
@UnstableApi
class Builder(private val context: Context) {
    private var config = PlayerConfig()

    fun setRepeatMode(mode: Int) = apply { config = config.copy(repeatMode = mode) }
    fun setSeekForwardIncrementMs(
        ms: Long,
    ) = apply { config = config.copy(seekForwardIncrementMs = ms) }
    fun setSeekBackIncrementMs(ms: Long) = apply { config = config.copy(seekBackIncrementMs = ms) }
    fun enableCache(cacheSize: Long, cacheDirectory: String) = apply {
        config = config.copy(cacheConfig = PlayerConfig.CacheConfig(cacheSize, cacheDirectory))
    }

    fun build(): ExoplayerControllerImpl {
        return ExoplayerControllerImpl(context, config)
    }
}
