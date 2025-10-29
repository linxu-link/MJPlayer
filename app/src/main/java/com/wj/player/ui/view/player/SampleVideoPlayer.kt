package com.wj.player.ui.view.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.ui.PlayerView
import com.wj.player.ui.view.player.base.IVideoPlayerController
import com.wj.player.ui.view.player.base.PlaybackState
import com.wj.player.ui.view.player.base.PlayerState
import com.wj.player.ui.view.player.ui.PlayerUiConfig

@Composable
fun SampleVideoPlayer(
    modifier: Modifier,
    controller: IVideoPlayerController,
    config: PlayerUiConfig = PlayerUiConfig.Builder().build(),
    isFullscreen: Boolean = false,
    onFullscreenToggle: () -> Unit = {},
) {
    val context = LocalContext.current
    val playerState by controller.playerState.collectAsState()

    Box(modifier = modifier) {
        // 视频渲染表面
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = controller.getPlayer()
                    useController = false
                }
            },
            modifier = Modifier.fillMaxSize(),
        )

        // 控制层
        if (config.showControls) {
            PlayerControls(
                state = playerState,
                config = config,
                isFullscreen = isFullscreen,
                onPlayPause = controller::playPause,
                onSeek = controller::seekTo,
                onSpeedChange = controller::setPlaybackSpeed,
                onFullscreenToggle = onFullscreenToggle,
                modifier = Modifier
                    .fillMaxSize()
                    .background(config.controlsBackgroundColor),
            )
        }

        // 缓冲指示器
        if (playerState.playbackState == PlaybackState.BUFFERING) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.Center),
                color = config.controlsContentColor,
            )
        }
    }
}

@Composable
private fun PlayerControls(
    state: PlayerState,
    config: PlayerUiConfig,
    isFullscreen: Boolean,
    onPlayPause: () -> Unit,
    onSeek: (Long) -> Unit,
    onSpeedChange: (Float) -> Unit,
    onFullscreenToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        // 顶部控制栏
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .align(Alignment.TopEnd),
            horizontalArrangement = Arrangement.End,
        ) {
            // 速度控制
            if (config.showSpeedControl) {
                var expanded by remember { mutableStateOf(false) }
                val speeds = listOf(0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 2.0f)

                Box {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = "Speed",
                            tint = config.controlsContentColor,
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        speeds.forEach { speed ->
                            DropdownMenuItem(
                                text = { Text("${speed}x", color = Color.Black) },
                                onClick = {
                                    onSpeedChange(speed)
                                    expanded = false
                                },
                            )
                        }
                    }
                }
            }

            // 全屏按钮
            if (config.showFullscreen) {
                IconButton(onClick = onFullscreenToggle) {
                    if (isFullscreen) {
                        config.exitFullscreenIcon()
                    } else {
                        config.fullscreenIcon()
                    }
                }
            }
        }

        // 中央播放/暂停按钮
        if (config.showPlayPause) {
            IconButton(
                onClick = onPlayPause,
                modifier = Modifier.align(Alignment.Center),
            ) {
                if (state.isPlaying) {
                    config.pauseIcon()
                } else {
                    config.playIcon()
                }
            }
        }

        // 底部进度条
        if (config.showSeekBar) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
            ) {
                // 时间显示
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = formatTime(state.currentPosition),
                        color = config.controlsContentColor,
                    )
                    Text(
                        text = formatTime(state.duration),
                        color = config.controlsContentColor,
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // 进度条
                Slider(
                    value = state.currentPosition.toFloat(),
                    onValueChange = { onSeek(it.toLong()) },
                    valueRange = 0f..state.duration.toFloat(),
                    colors = SliderDefaults.colors(
                        thumbColor = config.controlsContentColor,
                        activeTrackColor = config.controlsContentColor.copy(alpha = 0.7f),
                        inactiveTrackColor = config.controlsContentColor.copy(alpha = 0.3f),
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

// 辅助函数：格式化时间
private fun formatTime(millis: Long): String {
    val totalSeconds = millis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}
