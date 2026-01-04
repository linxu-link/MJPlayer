package com.wj.player

import android.content.ComponentName
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.media3.ui.PlayerView

@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel,
    onBack: () -> Unit,
) {
    val mediaItems by viewModel.mediaItems.collectAsState()
    val playerState by viewModel.playerState.collectAsState()
    val context = LocalContext.current
    val mediaController = remember { mutableStateOf<MediaController?>(null) }
    val isControllerReady = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val sessionToken =
            SessionToken(context, ComponentName(context, PlaybackService::class.java))
        val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture.addListener(
            {
                try {
                    val controller = controllerFuture.get()
                    mediaController.value = controller
                    // 添加监听器观察播放状态（仅在就绪后添加）
                    controller.addListener(
                        object : Player.Listener {
                            override fun onIsPlayingChanged(playing: Boolean) {
                                Log.e("PlayScreen", "onIsPlayingChanged: $playing")
                            }

                            override fun onPlaybackStateChanged(state: Int) {
                                Log.e("PlayScreen", "onPlaybackStateChanged: $state")
                            }
                        },
                    )
                    isControllerReady.value = true  // 标记就绪
                } catch (e: Exception) {
                    Log.e("PlayerScreen", "MediaController build failed: ${e.message}")
                    // 在这里处理 UI 反馈，如显示错误 Toast
                }
            },
            ContextCompat.getMainExecutor(context),
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaController.value?.release()
            isControllerReady.value = false
        }
    }

    // 仅当控制器就绪时显示 PlayerControlView，否则显示加载 UI
    if (isControllerReady.value && mediaController.value != null) {
        mediaController.value?.let { controller ->
            PlayerControlView(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                mediaPlayer = controller,
                onPlayPause = {
                    if (playerState == PlayerState.Playing) {
                        controller.pause()
                        viewModel.pause()
                    } else {
                        controller.play()
                        viewModel.play(mediaItems.first())
                    }
                },
                onSeek = { positionMs ->
                    controller.seekTo(positionMs)
                },
                currentTime = controller.currentPosition,
                duration = controller.duration,
            )
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
            Text("Loading player...")
        }
    }
}

@Composable
fun PlayerControlView(
    modifier: Modifier = Modifier,
    mediaPlayer: Player? = null,
    onPlayPause: () -> Unit,
    onSeek: (Long) -> Unit,
    currentTime: Long,
    duration: Long,
) {
    val isPlaying by remember { derivedStateOf { mediaPlayer?.isPlaying } }
    val playbackState by remember { derivedStateOf { mediaPlayer?.playbackState } }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
    ) {
        // 顶部状态栏
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            Text(
                text = if (playbackState == Player.STATE_BUFFERING) "Buffering..." else "",
                color = Color.White,
                modifier = Modifier.align(Alignment.TopStart),
            )
        }

        // 中间播放区域
        Box(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            // 视频区域 (使用AndroidView嵌入PlayerView)
            AndroidView(
                modifier = Modifier
                    .fillMaxSize(),
                factory = { context ->
                    PlayerView(context).apply {
                        player = mediaPlayer
                    }
                }
            )
        }

        // 底部控制栏
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = formatTime(currentTime),
                color = Color.White,
            )

            IconButton(onClick = onPlayPause) {
                Icon(
                    imageVector = if (isPlaying == true) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = "Play/Pause",
                    tint = Color.White,
                )
            }

            Text(
                text = formatTime(duration),
                color = Color.White,
            )
        }

        // 进度条
        Slider(
            value = (currentTime.toFloat() / duration.toFloat()).coerceIn(0f, 1f),
            onValueChange = { value ->
                onSeek((value * duration).toLong())
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
        )
    }
}

private fun formatTime(milliseconds: Long): String {
    val seconds = (milliseconds / 1000).toInt()
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return "${minutes}:${if (remainingSeconds < 10) "0" else ""}$remainingSeconds"
}

@Preview(showBackground = true)
@Composable
private fun PlayerControlViewPreview() {
    PlayerControlView(
        modifier = Modifier.fillMaxSize(),
        mediaPlayer = null,
        onPlayPause = {},
        onSeek = {},
        currentTime = 0,
        duration = 100,
    )
}
