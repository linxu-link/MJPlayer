package com.wj.player.ui.pager.videolist

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.wj.player.MJConstants
import com.wj.player.ui.theme.colors.LocalColorScheme
import com.wj.player.ui.view.header.VideoListTopAppBar

const val VIDEO_ROW_COUNT = 3

@Composable
fun VideoListScreen(
    onNavigateToSearch: () -> Unit,
    onVideoClick: () -> Unit,
    onNavigateToThemeSettings: () -> Unit,
    onNavigateToVideoSettings: () -> Unit,
    onFloatingBarClick: () -> Unit,
    modifier: Modifier = Modifier,
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        topBar = {
            VideoListTopAppBar(
                onClickThemeSettings = onNavigateToThemeSettings,
                onClickVideoSettings = onNavigateToVideoSettings,
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onFloatingBarClick,
                shape = CircleShape,
                containerColor = LocalColorScheme.current.accent,
            ) {
                Icon(
                    imageVector = MJConstants.Icon.ARROW_PLAY,
                    contentDescription = "继续播放",
                    tint = LocalColorScheme.current.accentInverse,
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { innerPadding ->

    }
}
