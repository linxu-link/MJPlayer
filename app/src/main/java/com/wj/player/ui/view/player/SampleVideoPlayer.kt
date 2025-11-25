package com.wj.player.ui.view.player

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.ui.PlayerView
import com.wj.player.R
import com.wj.player.ui.theme.MJPlayerTheme
import com.wj.player.ui.view.player.base.IVideoPlayerController
import com.wj.player.ui.view.player.base.PlaybackState
import com.wj.player.ui.view.player.base.PlayerState
import com.wj.player.ui.view.player.ui.BottomSheet
import com.wj.player.ui.view.player.ui.PlayerUiConfig
import com.wj.player.ui.view.player.ui.SheetOption
import com.wj.player.ui.view.player.ui.TopPlayerActionBar
import com.wujia.toolkit.utils.HiLog
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

private const val animationDuration = 350
private val animationEasing = LinearOutSlowInEasing

@Composable
fun SampleVideoPlayer(
    modifier: Modifier,
    controller: IVideoPlayerController,
    uiConfig: PlayerUiConfig,
    onRotateToggle: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onLockClick: () -> Unit = {},
) {
    val context = LocalContext.current
    val playerState by controller.playerState.collectAsState()
    var showVideoControls by remember { mutableStateOf(true) }
    var showSpeedSheet by remember { mutableStateOf(false) }

    // 保存计时协程的Job，用于取消/重置计时
    var autoHideJob by remember { mutableStateOf<Job?>(null) }

    // 自动隐藏逻辑：showVideoControls为true时启动3秒计时
    LaunchedEffect(showVideoControls) {
        if (showVideoControls) {
            // 启动新计时前先取消旧计时（避免多个计时冲突）
            autoHideJob?.cancel()
            autoHideJob = launch {
                delay(5000) // 3秒后隐藏
                showVideoControls = false
            }
        } else {
            // 控制栏隐藏时取消计时
            autoHideJob?.cancel()
        }
    }

    BackHandler(enabled = showSpeedSheet) {
        showSpeedSheet = false
    }

    Box(
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures(
                onTap = {
                    if (showSpeedSheet) {
                        showSpeedSheet = false
                    } else {
                        showVideoControls = !showVideoControls
                        autoHideJob?.cancel()
                    }
                },
                onDoubleTap = {
                    controller.playPause()
                },
            )
        },
    ) {
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
        PlayerControls(
            state = playerState,
            config = uiConfig,
            showControls = showVideoControls,
            onPlayPause = controller::playPause,
            onSeek = controller::seekTo,
            onSpeedChange = {
                controller.setPlaybackSpeed(it)
                showSpeedSheet = false
            },
            onSpeedIconClick = { show ->
                showSpeedSheet = show
            },
            showSpeedSheet = showSpeedSheet,
            onRotateClick = onRotateToggle,
            onBackClick = onBackClick,
            onLockClick = onLockClick,
            modifier = Modifier,
        )

        // 缓冲指示器
        if (playerState.playbackState == PlaybackState.BUFFERING) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.Center),
                color = uiConfig.controlsContentColor,
            )
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            controller.release()
        }
    }
}

@Composable
private fun PlayerControls(
    modifier: Modifier = Modifier,
    state: PlayerState,
    config: PlayerUiConfig,
    showControls: Boolean,
    showSpeedSheet: Boolean,
    onBackClick: () -> Unit,
    onPlayPause: () -> Unit,
    onSeek: (Long) -> Unit,
    onSpeedChange: (Float) -> Unit,
    onRotateClick: () -> Unit,
    onSpeedIconClick: (Boolean) -> Unit,
    onLockClick: () -> Unit,
) {

    val speedOptions = listOf(
        SheetOption(
            title = "3.0x",
            onClick = {
                onSpeedChange(3.0f)
            },
            isSelected = state.playbackSpeed == 3.0f,
        ),
        SheetOption(
            title = "2.0x",
            onClick = {
                onSpeedChange(2.0f)
            },
            isSelected = state.playbackSpeed == 2.0f,
        ),
        SheetOption(
            title = "1.0x",
            onClick = {
                onSpeedChange(1.0f)
            },
            isSelected = state.playbackSpeed == 1.0f,
        ),
        SheetOption(
            title = "0.5x",
            onClick = {
                onSpeedChange(0.5f)
            },
            isSelected = state.playbackSpeed == 0.5f,
        ),
        SheetOption(
            title = "0.25x",
            onClick = {
                onSpeedChange(0.25f)
            },
            isSelected = state.playbackSpeed == 0.25f,
        ),
        SheetOption(
            title = "0.1x",
            onClick = {
                onSpeedChange(0.1f)
            },
            isSelected = state.playbackSpeed == 0.1f,
        ),
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // 顶部控制栏
        PlayerTopBar(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(horizontal = 8.dp, vertical = 8.dp),
            config = config,
            rightIconText =
                if (state.playbackSpeed == 1.0f) {
                    stringResource(R.string.normal_speed)
                } else {
                    "${state.playbackSpeed}x"
                },
            visible = showControls,
            onBackClick = onBackClick,
            onRightIconClick = {
                onSpeedIconClick(true)
            },
        )

        if (config.showCenterBar) {
            // 中央播放/暂停按钮
            PlayerCenterBar(
                modifier = modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                onLockClick = onLockClick,
                onRotateClick = onRotateClick,
                config = config,
                visible = showControls,
            )
        }

        // 底部进度条
        if (config.showBottomSeekBar) {
            PlayerBottomSeekBar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                config = config,
                state = state,
                visible = showControls,
                onSeek = onSeek,
                onPlayPause = onPlayPause,
            )
        }

        BottomSheet(
            modifier = Modifier
                .background(
                    Color.White,
                    RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                )
                .align(Alignment.BottomCenter)
                .padding(vertical = 4.dp),
            visible = showSpeedSheet,
            options = speedOptions,
            onCloseClick = {
                onSpeedIconClick(false)
            },
        )
    }
}

@Composable
private fun PlayerTopBar(
    modifier: Modifier = Modifier,
    config: PlayerUiConfig,
    visible: Boolean,
    onBackClick: () -> Unit,
    onRightIconClick: () -> Unit,
    rightIconText: String = "倍速",
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { -it },
            animationSpec = tween(durationMillis = animationDuration, easing = animationEasing),
        ) + fadeIn(),
        exit = slideOutVertically(
            targetOffsetY = { -it },
            animationSpec = tween(durationMillis = animationDuration, easing = animationEasing),
        ) + fadeOut(),
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically, // 垂直居中
        ) {
            // 速度控制
            TopPlayerActionBar(
                title = config.title,
                onBackClick = onBackClick,
                onRightIconClick = onRightIconClick,
                rightIconText = rightIconText,
                leftIcon = config.backIcon,
                rightIcon = if (config.showSpeedControl) config.speedIcon else null,
            )
        }
    }
}

@Composable
private fun PlayerCenterBar(
    modifier: Modifier = Modifier,
    visible: Boolean,
    onLockClick: () -> Unit,
    onRotateClick: () -> Unit,
    config: PlayerUiConfig,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(durationMillis = animationDuration, easing = animationEasing),
            ) + fadeIn(),
            exit = slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(durationMillis = animationDuration, easing = animationEasing),
            ) + fadeOut(),
            modifier = Modifier.align(Alignment.CenterStart),
        ) {
            IconButton(
                onClick = onLockClick,
                modifier = Modifier
                    .size(48.dp),
            ) {
                config.lockIcon()
            }
        }

        AnimatedVisibility(
            visible = visible,
            enter = slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(durationMillis = animationDuration, easing = animationEasing),
            ) + fadeIn(),
            exit = slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(durationMillis = animationDuration, easing = animationEasing),
            ) + fadeOut(),
            modifier = Modifier.align(Alignment.CenterEnd),
        ) {
            IconButton(
                onClick = onRotateClick,
                modifier = Modifier.size(48.dp),
            ) {
                config.rotateIcon()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlayerBottomSeekBar(
    modifier: Modifier = Modifier,
    config: PlayerUiConfig,
    state: PlayerState,
    visible: Boolean,
    onSeek: (Long) -> Unit,
    onPlayPause: () -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(durationMillis = animationDuration, easing = animationEasing),
        ) + fadeIn(tween(delayMillis = 200)),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(durationMillis = 200, easing = animationEasing),
        ) + fadeOut(tween(delayMillis = 150)),
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 0.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            // 播放/暂停按钮：左对齐
            IconButton(
                onClick = onPlayPause,
                modifier = Modifier.size(48.dp),
            ) {
                if (state.isPlaying) {
                    config.pauseIcon()
                } else {
                    config.playIcon()
                }
            }

            // 左侧时间文本（当前进度）
            Text(
                text = formatTime(state.currentPosition),
                color = config.controlsContentColor,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 4.dp),
            )

            // Slider：居中占主要宽度
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .height(24.dp),
                contentAlignment = Alignment.Center,
            ) {
                Slider(
                    value = state.currentPosition.toFloat(),
                    onValueChange = { onSeek(it.toLong()) },
                    valueRange = 0f..state.duration.toFloat(),
                    track = { sliderPositions ->
                        HiLog.e("sliderPositions.value: ${sliderPositions.value}")
                        Box(
                            modifier = Modifier
                                .height(20.dp)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center,
                        ) {
                            // 背景轨道（未播放部分）
                            Box(
                                modifier = Modifier
                                    .height(4.dp)
                                    .fillMaxWidth()
                                    .background(
                                        color = config.controlsContentColor.copy(alpha = 0.3f),
                                        shape = RoundedCornerShape(2.dp),
                                    ),
                            )
                            // 已播放进度条
                            Box(
                                modifier = Modifier
                                    .height(4.dp)
                                    .fillMaxWidth(fraction = sliderPositions.value / state.duration.toFloat())
                                    .background(
                                        color = config.controlsContentColor,
                                        shape = RoundedCornerShape(2.dp),
                                    )
                                    .align(Alignment.CenterStart),
                            )
                        }
                    },
                    // 自定义滑块样式
                    thumb = {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .background(
                                        color = config.controlsContentColor,
                                        shape = CircleShape,
                                    ),
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            // 右侧时间文本（总时长）
            Text(
                text = formatTime(state.duration),
                color = config.controlsContentColor,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 4.dp),
            )
        }
    }
}

// 辅助函数：格式化时间
private fun formatTime(millis: Long): String {
    val totalSeconds = millis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format(Locale.CHINA, "%02d:%02d", minutes, seconds)
}

@Preview(showBackground = true)
@Composable
private fun PlayerControlsPreview() {
    MJPlayerTheme {
        PlayerControls(
            state = PlayerState(
                isPlaying = true,
                currentPosition = 60000, // 1分钟
                duration = 300000, // 5分钟
                playbackState = PlaybackState.READY,
                playbackSpeed = 1.0f,
            ),
            config = PlayerUiConfig.Builder()
                .showCenterBar(true)
                .showBottomSeekBar(true)
                .showSpeedControl(true)
                .showBufferingIndicator(true)
                .build(),
            onPlayPause = {},
            onSeek = {},
            onSpeedChange = {},
            onRotateClick = {},
            onBackClick = {},
            onLockClick = {},
            showControls = true,
            showSpeedSheet = false,
            onSpeedIconClick = {},
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
        )
    }
}
