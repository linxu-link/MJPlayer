/*
 * Copyright 2025 WuJia
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wj.player.ui.screen.videolist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.wj.player.R
import com.wj.player.data.source.local.video.room.VideoEntity
import com.wj.player.ui.view.header.VideoListTopAppBar
import com.wj.player.utils.VideoTimeUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun VideoListScreen(
    onNavigateToSearch: () -> Unit,
    onNavigateToSettings: (Int) -> Unit,
    onVideoOnclick: (VideoEntity) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: VideoViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        topBar = {
            VideoListTopAppBar(
                onSearch = onNavigateToSearch,
                onFilterAllTasks = {},
                onFilterActiveTasks = {},
                onFilterCompletedTasks = {},
                onClearCompletedTasks = {},
                onRefresh = { viewModel.loadVideos() } // 调用ViewModel的加载方法
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // 可根据需求实现具体功能，如添加视频等
                },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "添加"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->

        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        VideoListContent(
            items = uiState.videos,
            onVideoOnclick = onVideoOnclick,
            isLoading = uiState.isLoading, // 传递加载状态
            onRefresh = { viewModel.loadVideos() }, // 调用ViewModel的加载方法
            modifier = Modifier.padding(innerPadding),
        )

        // 处理用户消息
        uiState.userMessage?.let { messageResId ->
            val snackbarText = stringResource(id = messageResId)
            LaunchedEffect(snackBarHostState, messageResId) {
                snackBarHostState.showSnackbar(snackbarText)
                viewModel.clearUserMessage() // 显示后清除消息
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun VideoListContent(
    items: Flow<PagingData<VideoEntity>>,
    onVideoOnclick: (VideoEntity) -> Unit,
    isLoading: Boolean, // 新增加载状态参数
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 收集分页数据
    val pagingVideos = items.collectAsLazyPagingItems()

    // 分组展开状态管理
    val groupExpandStates = remember { mutableStateMapOf<String, Boolean>() }

    // 按视频创建时间分组
    val groupedVideos by remember(pagingVideos.itemCount) {
        mutableStateOf(
            pagingVideos.itemSnapshotList.items
                .groupBy { video ->
                    VideoTimeUtils.formatVideoDate(video.updateTime)
                }
        )
    }

    // 下拉刷新状态
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isLoading || pagingVideos.loadState.refresh is LoadState.Loading,
        onRefresh = { onRefresh() } // 调用ViewModel的加载方法
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            // 刷新失败状态
            if (pagingVideos.loadState.refresh is LoadState.Error) {
                val error = pagingVideos.loadState.refresh as LoadState.Error
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "刷新失败：${error.error.message}\n可再次下拉重试",
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // 空数据状态
            if (pagingVideos.itemCount == 0 &&
                pagingVideos.loadState.refresh !is LoadState.Loading &&
                !isLoading
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 80.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "未检测到本地视频",
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }

            // 分组视频列表
            if (groupedVideos.isNotEmpty()) {
                val sortedDates = groupedVideos.keys.sortedDescending()

                items(
                    count = sortedDates.size,
                    key = { index -> sortedDates[index] } // 使用索引获取日期作为key
                ) { index ->
                    val date = sortedDates[index] // 通过索引获取日期
                    val videos = groupedVideos[date] ?: emptyList()
                    val isExpanded = groupExpandStates[date] ?: true

                    VideoGroupItem(
                        date = date,
                        videos = videos,
                        isExpanded = isExpanded,
                        onToggleExpand = {
                            groupExpandStates[date] = !isExpanded
                        },
                        onVideoClick = onVideoOnclick,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }

            // 加载更多状态
            val loadState = pagingVideos.loadState.append
            when (loadState) {
                is LoadState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(32.dp),
                                color = MaterialTheme.colorScheme.primary,
                                strokeWidth = 2.dp
                            )
                        }
                    }
                }

                is LoadState.Error -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .clickable { pagingVideos.retry() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "加载更多失败，点击重试",
                                textAlign = TextAlign.Center,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                else -> {}
            }
        }

        // 下拉刷新指示器
        PullRefreshIndicator(
            refreshing = isLoading || pagingVideos.loadState.refresh is LoadState.Loading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            contentColor = MaterialTheme.colorScheme.primary,
            backgroundColor = MaterialTheme.colorScheme.surface
        )
    }
}

/**
 * 单组视频布局 - 使用手动网格布局替代 LazyVerticalGrid
 */
@Composable
fun VideoGroupItem(
    date: String,
    videos: List<VideoEntity>,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onVideoClick: (VideoEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // 分组标题栏（保持不变）
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = date,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray
            )
            IconButton(onClick = onToggleExpand) {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "收起" else "展开",
                    tint = Color.Gray
                )
            }
        }

        // 多行视频网格 - 使用手动网格布局
        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn(animationSpec = tween(durationMillis = 300)),
            exit = fadeOut(animationSpec = tween(durationMillis = 300))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // 将视频列表按每行4个分组
                val rows = videos.chunked(4)

                rows.forEach { rowVideos ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // 添加当前行的视频
                        rowVideos.forEach { video ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(16f / 9f)
                            ) {
                                VideoThumbnailItem(
                                    video = video,
                                    onVideoClick = onVideoClick,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }

                        // 如果一行不满4个，用空Box填充
                        repeat(4 - rowVideos.size) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(16f / 9f)
                            )
                        }
                    }
                }

                // 分组分隔线
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .size(height = 1.dp, width = 300.dp)
                        .align(Alignment.CenterHorizontally)
                        .background(Color.LightGray.copy(alpha = 0.5f))
                )
            }
        }
    }
}

/**
 * 单个视频缩略图
 * @param onVideoClick 视频点击事件
 */
@Composable
private fun VideoThumbnailItem(
    video: VideoEntity, // 接收VideoEntity类型
    onVideoClick: (VideoEntity) -> Unit, // 视频点击回调
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .clickable { onVideoClick(video) } // 处理点击事件
    ) {
        // 视频缩略图
        Image(
            painter = rememberAsyncImagePainter(
                model = if (video.thumbnailPath.isNullOrEmpty()) video.path else video.thumbnailPath,
                error = painterResource(id = R.drawable.ic_launcher_background),
                contentScale = ContentScale.Crop
            ),
            contentDescription = video.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // 视频时长
        Text(
            text = VideoTimeUtils.formatVideoDuration(video.duration),
            fontSize = 12.sp,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(4.dp)
                .background(Color.Black.copy(alpha = 0.7f))
                .padding(horizontal = 4.dp, vertical = 2.dp)
        )
    }
}

@Preview
@Composable
private fun VideoContentEmptyPreview() {
    MaterialTheme {
        Surface {
            VideoListContent(
                items = flowOf(PagingData.empty()),
                onVideoOnclick = {},
                isLoading = false,
                onRefresh = {}
            )
        }
    }
}