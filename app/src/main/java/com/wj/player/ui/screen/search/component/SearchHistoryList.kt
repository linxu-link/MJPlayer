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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wj.player.entity.SearchHistory

@Composable
fun SearchHistoryList(
    histories: List<SearchHistory>,
    onHistoryClick: (SearchHistory) -> Unit,
    onDeleteClick: (String) -> Unit,
    onClearAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        // 标题栏
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "搜索历史",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            if (histories.isNotEmpty()) {
                Text(
                    text = "清空全部",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onClearAllClick() }
                )
            }
        }

        // 历史列表
        if (histories.isEmpty()) {
            EmptyHistoryHint()
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(histories, key = { it.keyword }) { history ->
                    HistoryItem(
                        keyword = history.keyword,
                        onItemClick = { onHistoryClick(history) },
                        onDeleteClick = { onDeleteClick(history.keyword) }
                    )
                }
            }
        }
    }
}

// 单个历史项
@Composable
private fun HistoryItem(
    keyword: String,
    onItemClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = keyword, fontSize = 15.sp)
        IconButton(onClick = onDeleteClick, modifier = Modifier.size(20.dp)) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "删除历史"
            )
        }
    }
}