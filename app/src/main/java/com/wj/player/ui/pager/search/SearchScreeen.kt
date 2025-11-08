package com.wj.player.ui.pager.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.wj.player.R
import com.wj.player.data.entity.SearchHistory
import com.wj.player.data.entity.Video
import com.wj.player.data.source.local.video.room.VideoEntity
import com.wj.player.ui.view.header.SearchTopAppBar
import com.wj.player.ui.view.text.HighlightedText
import com.wj.player.utils.VideoTimeUtils
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
) {
    // 仅观察一个状态流（符合单一来源原则）
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val uiStateHolder = rememberSearchUiStateHolder() // 界面逻辑状态容器

    // 状态驱动搜索框输入（关键词变化时同步输入框）
    LaunchedEffect(uiState) {
        uiStateHolder.updateSearchText(uiState.keyword)
    }

    // 输入框变化时触发搜索（事件向上传递）
    val searchText = uiStateHolder.searchText
    LaunchedEffect(searchText) {
        // 避免重复触发（仅当输入与状态中的关键词不一致时）
        if (uiState.isLoading.not()) {
            val currentKeyword = uiState.keyword
            if (searchText != currentKeyword) {
                viewModel.onSearchKeywordChanged(searchText)
            }
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        // 搜索框
        SearchTopAppBar(
            text = uiState.keyword,
            onSearchTextChange = uiStateHolder::updateSearchText,
            onBack = { onNavigateBack() },
        )

        if (uiState.isLoading) {
            LoadingIndicator() // 加载状态
        } else {
            if (uiState.keyword.isNotEmpty()) {
                if (uiState.searchResults.isNotEmpty()) {
                    Column {
                        SearchHistoryList(
                            histories = emptyList(),
                            inSearch = uiState.keyword.isNotEmpty(),
                            onHistoryClick = {},
                            onDeleteClick = {},
                            onClearAllClick = {},
                        )

                        SearchResultList(
                            videos = uiState.searchResults,
                            listState = uiStateHolder.listState,
                            keyword = uiState.keyword,
                        )
                    }
                } else {
                    EmptyResultHint(keyword = uiState.keyword)
                }
            } else {
                SearchHistoryList(
                    histories = uiState.searchHistories,
                    inSearch = uiState.keyword.isNotEmpty(),
                    onHistoryClick = {
                        uiStateHolder.onHistoryKeywordClicked(it.keyword) {
                            viewModel.onSearchKeywordChanged(it)
                        }
                    },
                    onDeleteClick = viewModel::onDeleteHistory,
                    onClearAllClick = viewModel::onClearAllHistory,
                )
            }
        }
    }
}

@Composable
private fun SearchHistoryList(
    histories: List<SearchHistory>,
    inSearch: Boolean,
    onHistoryClick: (SearchHistory) -> Unit,
    onDeleteClick: (String) -> Unit,
    onClearAllClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.wrapContentSize()) {
        // 标题栏
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = if (inSearch) "搜索结果" else "搜索历史",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray,
            )
            if (histories.isNotEmpty() && inSearch.not()) {
                Text(
                    text = "清空全部",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onClearAllClick() },
                )
            }
        }
        if (inSearch.not()) {
            if (histories.isEmpty()) {
                EmptyHistoryHint()
            } else {
                // 历史列表
                HistoryGrid(
                    histories = histories,
                    onHistoryClick = onHistoryClick,
                    onDeleteClick = onDeleteClick,
                )
            }
        }
    }
}

@Composable
private fun HistoryGrid(
    histories: List<SearchHistory>,
    onHistoryClick: (SearchHistory) -> Unit,
    onDeleteClick: (String) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 64.dp), // 自适应列数，每列最小128dp
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 2.dp), // 网格内边距
        horizontalArrangement = Arrangement.spacedBy(8.dp), // 水平间距
        verticalArrangement = Arrangement.spacedBy(8.dp), // 垂直间距
    ) {
        items(items = histories, key = { it.keyword }) { history ->
            SearchHistoryItem(
                keyword = history.keyword,
                onItemClick = { onHistoryClick(history) },
                onDeleteClick = { onDeleteClick(history.keyword) },
            )
        }
    }
}

@Composable
private fun SearchResultList(
    videos: List<Video>,
    listState: LazyListState,
    keyword: String,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        state = listState, // 绑定界面逻辑容器的滚动状态
        modifier = modifier.fillMaxSize(),
    ) {
        items(videos, key = { it.id }) { video ->
            VideoThumbnailListItem(
                video = video,
                keyword = keyword,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                onVideoClick = { },
            )
        }
    }
}

/**
 * 列表布局 - 单个视频缩略图
 * @param onVideoClick 视频点击事件
 */
@Composable
private fun VideoThumbnailListItem(
    video: Video,
    keyword: String,
    onVideoClick: (Video) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onVideoClick(video) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // 视频缩略图（固定宽高比 16:9，宽度占比1/4）
        Image(
            painter = rememberAsyncImagePainter(
                model = if (video.thumbnailPath.isNullOrEmpty()) video.path else video.thumbnailPath,
                error = painterResource(id = R.drawable.ic_error),
                contentScale = ContentScale.Crop,
            ),
            contentDescription = video.title,
            modifier = Modifier
                .weight(1f)
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop,
        )

        // 视频信息（标题 + 时长，占比3/4）
        Column(
            modifier = Modifier.weight(3f),
            verticalArrangement = Arrangement.Top,
        ) {
            HighlightedText(
                text = video.title,
                keyword = keyword,
                style = TextStyle(
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                ),
            )

            // 关键：用权重Spacer占满中间空白区域，将底部Row推到最底部
            Spacer(modifier = Modifier.size(10.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp), // 优化分隔符间距
            ) {
                Text(
                    text = VideoTimeUtils.formatVideoSize(video.size),
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier,
                )

                Text(
                    text = "|",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    modifier = Modifier,
                )

                Text(
                    text = VideoTimeUtils.formatVideoSimpleDate(video.updateTime),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier,
                )
            }
        }
    }
}

// 修改后的单个历史项，适配流式布局
@Composable
private fun SearchHistoryItem(
    keyword: String,
    onItemClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray, RoundedCornerShape(12.dp))
            .clickable { onItemClick() },
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = keyword,
                fontSize = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.size(14.dp),
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "删除历史",
                    modifier = Modifier.size(14.dp),
                )
            }
        }
    }
}

@Composable
private fun LoadingIndicator(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // 圆形进度条（使用主题强调色）
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(48.dp),
        )

        // 加载提示文本
        Text(
            text = stringResource(R.string.loading_hint), // 资源文件定义："正在搜索..."
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 16.dp),
        )
    }
}

@Composable
private fun EmptyResultHint(
    keyword: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // 无结果图标
        Icon(
            imageVector = Icons.Outlined.Close,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.size(64.dp),
        )

        // 提示文本（包含搜索关键词）
        Text(
            text = stringResource(
                id = R.string.empty_result_hint, // 资源文件定义："未找到与「%1$s」相关的视频"
                keyword,
            ),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
        )

        // 辅助建议文本
        Text(
            text = stringResource(R.string.empty_result_suggestion), // 资源文件定义："请尝试其他关键词"
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}

@Composable
private fun EmptyHistoryHint(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // 历史图标（灰色，半透明）
        Icon(
            imageVector = Icons.Outlined.AccountBox,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.size(64.dp),
        )

        // 提示文本
        Text(
            text = stringResource(R.string.empty_history_hint), // 资源文件定义："暂无搜索历史"
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
        )
    }
}
