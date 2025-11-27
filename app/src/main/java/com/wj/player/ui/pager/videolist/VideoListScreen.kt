package com.wj.player.ui.pager.videolist

import android.Manifest
import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.wj.player.MJConstants
import com.wj.player.data.entity.LayoutType
import com.wj.player.data.source.local.video.room.VideoEntity
import com.wj.player.ui.theme.colors.Colors
import com.wj.player.ui.theme.colors.LocalColorScheme
import com.wj.player.ui.view.IconTint
import com.wj.player.ui.view.ImageVideo
import com.wj.player.ui.view.TextBody
import com.wj.player.ui.view.TextCaption
import com.wj.player.ui.view.header.VideoListTopAppBar
import com.wj.player.ui.view.noRippleClickable
import com.wj.player.utils.MultiplePermissionsRequest
import com.wj.player.utils.VideoTimeUtils
import com.wujia.toolkit.utils.HiLog
import kotlinx.coroutines.flow.Flow

const val VIDEO_ROW_COUNT = 3

@Composable
fun VideoListScreen(
    onNavigateToSearch: () -> Unit,
    onVideoClick: (VideoEntity) -> Unit,
    onNavigateToThemeSettings: () -> Unit,
    onNavigateToVideoSettings: () -> Unit,
    onFloatingBarClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: VideoViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {

    // 权限请求
    val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) listOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.READ_MEDIA_VIDEO,
    ) else listOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
    )

    MultiplePermissionsRequest(
        permissions = permissions,
        onAllGranted = {
            viewModel.syncVideos()
        },
        onDenied = { deniedPermissions ->
            HiLog.e("VideoListScreen onDenied $deniedPermissions")
        },
    )

    // 主页
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        topBar = {
            VideoListTopAppBar(
                layoutType = uiState.layoutType,
                onSearch = onNavigateToSearch,
                onFilterList = {
                    viewModel.setLayoutType(LayoutType.LIST)
                },
                onFilterGrid = {
                    viewModel.setLayoutType(LayoutType.GRID)
                },
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

        VideoListContent(
            items = uiState.videos,
            layoutType = uiState.layoutType,
            onVideoOnclick = onVideoClick,
            isLoading = uiState.isLoading, // 传递加载状态
            onRefresh = { viewModel.syncVideos() }, // 调用ViewModel的加载方法
            modifier = modifier
                .padding(innerPadding),
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
    layoutType: LayoutType,
    isLoading: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
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
                    VideoTimeUtils.formatVideoDetailDate(video.updateTime)
                },
        )
    }

    // 下拉刷新状态
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isLoading || pagingVideos.loadState.refresh is LoadState.Loading,
        onRefresh = { onRefresh() },
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState),
    ) {
        // 列表样式
        GridColumn(
            pagingVideos = pagingVideos,
            groupedVideos = groupedVideos,
            groupExpandStates = groupExpandStates,
            isLoading = isLoading,
            layoutType = layoutType,
            onVideoOnclick = onVideoOnclick,
        )

        // 下拉刷新指示器
        PullRefreshIndicator(
            refreshing = isLoading || pagingVideos.loadState.refresh is LoadState.Loading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            contentColor = LocalColorScheme.current.accent,
            backgroundColor = LocalColorScheme.current.surface,
        )
    }
}

@Composable
private fun GridColumn(
    pagingVideos: LazyPagingItems<VideoEntity>,
    groupedVideos: Map<String, List<VideoEntity>>,
    groupExpandStates: MutableMap<String, Boolean>,
    isLoading: Boolean,
    layoutType: LayoutType,
    onVideoOnclick: (VideoEntity) -> Unit,
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
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "刷新失败：${error.error.message}\n可再次下拉重试",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp,
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
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "未检测到本地视频",
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }
        }

        // 分组视频列表
        if (groupedVideos.isNotEmpty()) {
            val sortedDates = groupedVideos.keys.sortedDescending()
            items(
                count = sortedDates.size,
                key = { index -> sortedDates[index] }, // 使用索引获取日期作为key
            ) { index ->
                val date = sortedDates[index] // 通过索引获取日期
                val videos = groupedVideos[date] ?: emptyList()
                val isExpanded = groupExpandStates[date] ?: true
                VideoGroupItem(
                    date = date,
                    videos = videos,
                    isExpanded = isExpanded,
                    layoutType = layoutType,
                    onToggleExpand = {
                        groupExpandStates[date] = !isExpanded
                    },
                    onVideoClick = onVideoOnclick,
                    modifier = Modifier.padding(bottom = 8.dp),
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
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(32.dp),
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 2.dp,
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
                            .noRippleClickable { pagingVideos.retry() },
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "加载更多失败，点击重试",
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }

            else -> {}
        }
    }
}

/**
 * 单组视频布局
 */
@Composable
private fun VideoGroupItem(
    date: String,
    videos: List<VideoEntity>,
    isExpanded: Boolean,
    layoutType: LayoutType,
    onToggleExpand: () -> Unit,
    onVideoClick: (VideoEntity) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // 分组标题栏
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            VideoGroupItemTitle(
                modifier = Modifier.weight(1f),
                videoSize = videos.size,
                date = date,
            )

            IconButton(onClick = onToggleExpand) {
                IconTint(
                    imageVector = if (isExpanded) MJConstants.Icon.ARROW_UP else MJConstants.Icon.ARROW_DOWN,
                    contentDescription = if (isExpanded) "收起" else "展开",
                    tint = LocalColorScheme.current.textSecondary,
                )
            }
        }

        // 列表布局 + 增强动画（滑入滑出 + 淡入淡出）
        AnimatedVisibility(
            visible = isExpanded,
            // 展开动画：从顶部滑入 + 淡入（偏移量为负，向上滑入）
            enter = slideInVertically(
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
                initialOffsetY = { -it / 4 }, // 初始偏移为自身高度的1/4，动画更自然
            ) + fadeIn(animationSpec = tween(durationMillis = 300)),
            // 收起动画：向顶部滑出 + 淡出（偏移量为负，向上滑出）
            exit = slideOutVertically(
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
                targetOffsetY = { -it / 4 },
            ) + fadeOut(animationSpec = tween(durationMillis = 300)),
            // 关键：添加内容大小动画，避免布局跳动
            modifier = Modifier.animateContentSize(animationSpec = tween(durationMillis = 300)),
        ) {
            if (layoutType == LayoutType.GRID) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(VIDEO_ROW_COUNT),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        // 限制最大高度（根据需求调整，示例：最多显示 3 行，每行 16:9 比例）
                        .heightIn(
                            max = calculateGridMaxHeight(
                                rowCount = 3,
                                itemAspectRatio = 16f / 9f,
                            ),
                        ),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(videos.size, key = { index -> videos[index].id }) { index ->
                        VideoThumbnailGroupItem(
                            video = videos[index],
                            onVideoClick = onVideoClick,
                            modifier = Modifier.aspectRatio(16f / 9f),
                        )
                    }
                }
            } else {
                // 单列列表布局
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp), // 列表项间距8dp
                ) {
                    videos.forEachIndexed { index, video ->
                        // 单个视频列表项（横向布局：缩略图 + 标题 + 时长）
                        VideoThumbnailListItem(
                            video = video,
                            onVideoClick = onVideoClick,
                            // 给每个列表项添加延迟动画，实现依次展开/收起效果
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateEnterExit(
                                    enter = fadeIn(
                                        animationSpec = tween(
                                            durationMillis = 200,
                                            delayMillis = index * 50, // 每个项延迟50ms，依次出现
                                        ),
                                    ) + slideInVertically(
                                        animationSpec = tween(
                                            durationMillis = 200,
                                            delayMillis = index * 50,
                                            easing = FastOutSlowInEasing,
                                        ),
                                        initialOffsetY = { 20 }, // 初始向下偏移20dp
                                    ),
                                    exit = fadeOut(
                                        animationSpec = tween(
                                            durationMillis = 200,
                                            delayMillis = (videos.size - 1 - index) * 50, // 反向延迟，依次消失
                                        ),
                                    ) + slideOutVertically(
                                        animationSpec = tween(
                                            durationMillis = 200,
                                            delayMillis = (videos.size - 1 - index) * 50,
                                            easing = FastOutSlowInEasing,
                                        ),
                                        targetOffsetY = { 20 }, // 目标向下偏移20dp
                                    ),
                                ),
                        )
                    }
                }
            }
        }

    }

}

/**
 * 宫格布局 - 单个视频缩略图
 * @param onVideoClick 视频点击事件
 */
@Composable
private fun VideoThumbnailGroupItem(
    video: VideoEntity,
    onVideoClick: (VideoEntity) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .noRippleClickable { onVideoClick(video) }, // 处理点击事件
    ) {
        ImageVideo(
            videoTitle = video.title,
            videoId = video.id,
            videoPath = video.path,
            thumbnailPath = video.thumbnailPath ?: "",
        )

        TextCaption(
            text = VideoTimeUtils.formatVideoDuration(video.duration),
            color = Colors.white,
            modifier = Modifier
                .padding(4.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.Black.copy(alpha = 0.5f))
                .align(Alignment.BottomEnd)
                .padding(horizontal = 4.dp, vertical = 2.dp),
        )
    }
}

/**
 * 列表布局 - 单个视频缩略图
 * @param onVideoClick 视频点击事件
 */
@Composable
private fun VideoThumbnailListItem(
    video: VideoEntity,
    onVideoClick: (VideoEntity) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .noRippleClickable { onVideoClick(video) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ImageVideo(
            videoTitle = video.title,
            videoId = video.id,
            videoPath = video.path,
            thumbnailPath = video.thumbnailPath ?: "",
            modifier = Modifier.weight(1f),
        )

        Column(
            modifier = Modifier.weight(3f),
        ) {
            TextBody(text = video.title)
            Spacer(modifier = Modifier.size(14.dp))
            VideoItemDesc(video = video)
        }
    }
}


/**
 * 计算网格最大高度：每行高度 = 屏幕宽度 / 列数 / 宽高比
 * @param rowCount 最大显示行数
 * @param itemAspectRatio 视频项宽高比（16:9）
 */
@Composable
private fun calculateGridMaxHeight(rowCount: Int, itemAspectRatio: Float): Dp {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val columnCount = VIDEO_ROW_COUNT // 与网格列数一致
    val horizontalPadding = 16.dp * 2 // 左右各16dp padding
    val columnSpacing = 8.dp * (columnCount - 1) // 列间距（4列3个间距）

    // 单个视频项的宽度 = （屏幕宽度 - 左右padding - 列间距） / 列数
    val itemWidth = (screenWidth - horizontalPadding - columnSpacing) / columnCount
    // 单个视频项的高度 = 宽度 / 宽高比
    val itemHeight = itemWidth / itemAspectRatio
    // 网格总高度 = 每行高度 * 行数 + 行间距（3行2个间距）
    val rowSpacing = 8.dp * (rowCount - 1)
    return itemHeight * rowCount + rowSpacing
}
