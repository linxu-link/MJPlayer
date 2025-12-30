package com.wj.player.ui.view.player

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@Composable
fun TikTokStylePlayer(
    videos: List<VideoItem>,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { videos.size }
    )

    // 当前可见页面
    val currentPage by remember { derivedStateOf { pagerState.currentPage } }

    VerticalPager(
        state = pagerState,
        modifier = modifier.fillMaxSize(),
        userScrollEnabled = true
    ) { page ->
//        VideoPage(
//            video = videos[page],
//            isActive = page == currentPage,
//            key = videos[page].id // 保证重组时正确复用
//        )
    }
}
