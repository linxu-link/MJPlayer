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

package com.wj.player.ui.screen.search.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.wj.player.entity.Video
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun SearchResultList(
    videos: List<Video>,
    listState: LazyListState,
    keyword: String,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        state = listState, // 绑定界面逻辑容器的滚动状态
        modifier = modifier.fillMaxSize()
    ) {
        items(videos, key = { it.id }) { video ->
            VideoItem(
                video = video,
                keyword = keyword,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

// 视频Item（横向布局，包含缩略图、名称、大小、时间）
@Composable
private fun VideoItem(
    video: Video,
    keyword: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxWidth()) {
        // 缩略图
        Image(
            painter = rememberAsyncImagePainter(video.thumbnailPath ?: video.path),
            contentDescription = video.title,
            modifier = Modifier
                .size(120.dp, 80.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        // 信息区
        Column(modifier = Modifier.weight(1f)) {
            // 标题（关键词高亮）
            HighlightedText(
                text = video.title,
                keyword = keyword,
                style = TextStyle()
            )

            Spacer(modifier = Modifier.height(4.dp))

            // 大小
            Text(
                text = "大小：${video.size / (1024 * 1024)} MB",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            // 创建时间
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)
            Text(
                text = "创建时间：${dateFormat.format(video.updateTime)}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}