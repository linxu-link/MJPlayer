package com.wj.player.ui.pager.player

import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import com.wj.player.data.entity.Video
import com.wj.player.ui.theme.configuration.LocalIsLandscape
import com.wj.player.ui.theme.configuration.LocalOrientationController
import com.wj.player.ui.theme.configuration.LocalSystemUiControl
import com.wj.player.ui.view.player.ExoplayerControllerImpl
import com.wj.player.ui.view.player.SampleVideoPlayer
import com.wj.player.ui.view.player.ui.PlayerUiConfig

@Composable
fun PlayerScreen(
    itemId: Long,
    viewModel: PlayerViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onBackClick: () -> Unit = {},
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // 处理用户消息
    uiState.userMessage?.let { messageResId ->
        val snackbarText = stringResource(id = messageResId)
        LaunchedEffect(snackBarHostState, messageResId) {
            snackBarHostState.showSnackbar(snackbarText)
            viewModel.clearUserMessage() // 显示后清除消息
            // 返回上一页
            if (uiState.video == null) {
                onBackClick()
            }
        }
    }

    LaunchedEffect(itemId) {
        viewModel.playVideo(itemId)
    }

    Scaffold(
        modifier = Modifier
            .background(
                Color.Black,
            )
            .fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        topBar = {},
        floatingActionButton = {},
        floatingActionButtonPosition = FabPosition.End,
    ) { innerPadding ->
        val video = uiState.video
        if (video != null) {
            VideoPlayerContent(
                video = video,
                isLoading = uiState.isLoading,
                onBackClick = onBackClick,
            )
        }
    }

}

@OptIn(UnstableApi::class)
@Composable
private fun VideoPlayerContent(
    video: Video,
    isLoading: Boolean,
    onBackClick: () -> Unit,
) {
    val context = LocalContext.current
    val playerUiConfig = remember {
        PlayerUiConfig.Builder()
            .showSpeedControl(true)
            .showBottomSeekBar(true)
            .showBufferingIndicator(true)
            .setTitle(video.title)
            .build()
    }

    val playerController = remember {
        ExoplayerControllerImpl.Builder(context)
            .setRepeatMode(Player.REPEAT_MODE_OFF)
            .setSeekForwardIncrementMs(15_000)
            .setSeekBackIncrementMs(5_000)
            .enableCache(100 * 1024 * 1024, "exoplayer_cache")
            .build()
    }
    playerController.loadMedia(video.path.toUri())

    val toggleOrientation = LocalOrientationController.current
    val isLandscape = LocalIsLandscape.current

     BackHandler(enabled = true) {
        if (isLandscape()) {
            toggleOrientation()
        } else {
            onBackClick()
        }
    }

    SampleVideoPlayer(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color.Black,
            ),
        controller = playerController,
        uiConfig = playerUiConfig,
        onBackClick = {
            if (isLandscape()) {
                toggleOrientation()
            } else {
                onBackClick()
            }
        },
        onRotateToggle = toggleOrientation,
    )

}
