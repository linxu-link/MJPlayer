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
import androidx.compose.material.icons.outlined.DoNotDisturb
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
import com.wj.player.ui.pager.videolist.VideoItemDesc
import com.wj.player.ui.theme.colors.Colors
import com.wj.player.ui.theme.colors.LocalColorScheme
import com.wj.player.ui.view.ImageVideo
import com.wj.player.ui.view.TextBody
import com.wj.player.ui.view.TextCaption
import com.wj.player.ui.view.header.SearchTopAppBar
import com.wj.player.ui.view.noRippleClickable
import com.wj.player.ui.view.text.HighlightedText
import com.wj.player.utils.VideoTimeUtils
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onVideoClick: (Video) -> Unit,
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
                            onVideoClick = onVideoClick,
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
            if (inSearch) {
                if (histories.isNotEmpty()) {
                    TextCaption(
                        text = if (inSearch) stringResource(R.string.search_result) else stringResource(
                            R.string.search_history,
                        ),
                        color = LocalColorScheme.current.textPrimaryInverse,
                    )
                }
            }
            if (histories.isNotEmpty() && inSearch.not()) {
                TextCaption(
                    text = stringResource(R.string.clear_all),
                    color = LocalColorScheme.current.accent,
                    modifier = Modifier.noRippleClickable { onClearAllClick() },
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
    onVideoClick: (Video) -> Unit,
) {
    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
    ) {
        items(videos, key = { it.id }) { video ->
            VideoThumbnailListItem(
                video = video,
                keyword = keyword,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                onVideoClick = onVideoClick,
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
            .noRippleClickable { onVideoClick(video) },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(8.dp))
                .weight(1f)
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
                    .padding(horizontal = 2.dp, vertical = 2.dp),
            )
        }

        Column(modifier = Modifier.weight(3f)) {
            HighlightedText(
                text = video.title,
                keyword = keyword,
            )
            Spacer(modifier = Modifier.size(14.dp))
            VideoItemDesc(
                videoSize = video.size,
                videoUpdateTime = video.updateTime,
            )
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
            .background(Colors.lightGrey, RoundedCornerShape(4.dp))
            .noRippleClickable { onItemClick() },
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextCaption(
                color = Colors.dark,
                text = keyword,
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
            imageVector = Icons.Outlined.DoNotDisturb,
            contentDescription = null,
            tint = LocalColorScheme.current.textPrimary.copy(alpha = 0.5f),
            modifier = Modifier.size(64.dp),
        )

        // 提示文本
        TextBody(
            text = stringResource(R.string.empty_history_hint), // 资源文件定义："暂无搜索历史"
            color = LocalColorScheme.current.textPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
        )
    }
}
