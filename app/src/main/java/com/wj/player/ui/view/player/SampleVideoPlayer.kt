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
import com.wj.player.ui.view.player.ui.PlayerActionBar
import com.wj.player.ui.view.player.ui.PlayerSeekBar
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
) {
    val playerState by controller.playerState.collectAsState()
    var showVideoControls by remember { mutableStateOf(true) }
    var showSpeedSheet by remember { mutableStateOf(false) }
    var isLocked by remember { mutableStateOf(false) }
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
                    if (isLocked) {
                        return@detectTapGestures
                    }
                    if (showSpeedSheet) {
                        showSpeedSheet = false
                    } else {
                        showVideoControls = !showVideoControls
                        autoHideJob?.cancel()
                    }
                },
                onDoubleTap = {
                    if (isLocked) {
                        return@detectTapGestures
                    }
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
            onLockClick = {
                isLocked = !isLocked
                showVideoControls = !isLocked
            },
            modifier = Modifier,
            isLocked = isLocked,
        )

        // 缓冲指示器
        if (playerState.playbackState == PlaybackState.BUFFERING) {
            CircularProgressIndicator(
                modifier = Modifier
                    .background(
                        color = Color.Black.copy(alpha = 0.3f),
                        shape = CircleShape,
                    )
                    .size(56.dp)
                    .padding(2.dp)
                    .align(Alignment.Center),
                color = uiConfig.controlsContentColor,
            )
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
    isLocked: Boolean,
) {

    val speedOptions = remember {
        listOf(
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
                    onSpeedChange(0.3f)
                },
                isSelected = state.playbackSpeed == 0.3f,
            ),
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // 顶部控制栏
        PlayerTopBar(
            modifier = Modifier.align(Alignment.TopEnd),
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
                modifier = modifier.align(Alignment.Center),
                onLockClick = onLockClick,
                onRotateClick = onRotateClick,
                config = config,
                visible = showControls,
                isLocked = isLocked,
            )
        }

        // 底部进度条
        if (config.showBottomSeekBar) {
            PlayerBottomSeekBar(
                modifier = Modifier.align(Alignment.BottomCenter),
                config = config,
                state = state,
                visible = showControls,
                onSeek = onSeek,
                onPlayPause = onPlayPause,
            )
        }

        BottomSheet(
            modifier = Modifier.align(Alignment.BottomCenter),
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
        // 速度控制
        PlayerActionBar(
            modifier = Modifier.background(color = Color.Black.copy(alpha = 0.3f)),
            title = config.title,
            onBackClick = onBackClick,
            onRightIconClick = onRightIconClick,
            rightIconText = rightIconText,
            leftIcon = config.backIcon,
            rightIcon = if (config.showSpeedControl) config.speedIcon else null,
        )
    }
}

@Composable
private fun PlayerCenterBar(
    modifier: Modifier = Modifier,
    visible: Boolean,
    onLockClick: () -> Unit,
    onRotateClick: () -> Unit,
    config: PlayerUiConfig,
    isLocked: Boolean,
) {
    Box(
        modifier = modifier.fillMaxWidth().padding(horizontal = 12.dp),
    ) {
        AnimatedVisibility(
            visible = if (isLocked) true else visible,
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
                modifier = Modifier.background(
                    color = Color.Black.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(8.dp),
                ),
            ) {
                if (isLocked) {
                    config.lockIcon()
                } else {
                    config.unlockIcon()
                }
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
                modifier = Modifier.background(
                    color = Color.Black.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(8.dp),
                ),
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
        PlayerSeekBar(
            modifier = Modifier.background(color = Color.Black.copy(alpha = 0.3f)),
            state = state,
            config = config,
            onPlayPause = onPlayPause,
            onSeek = onSeek,
        )
    }
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
            isLocked = false,
        )
    }
}
