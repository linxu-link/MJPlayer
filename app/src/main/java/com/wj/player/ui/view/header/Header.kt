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

package com.wj.player.ui.view.header

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 自定义头部导航组件
 *
 * @param title 标题文本
 * @param showBackButton 是否显示返回按钮
 * @param onBackClick 返回按钮点击事件
 * @param rightIcon1 右侧第一个图标（可选）
 * @param onRightIcon1Click 右侧第一个图标点击事件（可选）
 * @param rightIcon2 右侧第二个图标（可选）
 * @param onRightIcon2Click 右侧第二个图标点击事件（可选）
 */
@Composable
fun CustomHeader(
    title: String,
    showBackButton: Boolean = false,
    onBackClick: (() -> Unit)? = null,
    rightIcon1: ImageVector? = null,
    onRightIcon1Click: (() -> Unit)? = null,
    rightIcon2: ImageVector? = null,
    onRightIcon2Click: (() -> Unit)? = null
) {
    // 设置与原生ActionBar一致的高度56dp
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 左侧区域 - 返回按钮和标题
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 返回按钮
            if (showBackButton && onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "返回"
                    )
                }
                // 按钮和标题之间的间距
                Spacer(modifier = Modifier.width(8.dp))
            } else {
                // 如果不显示返回按钮，添加左侧内边距与有返回按钮时对齐
                Spacer(modifier = Modifier.width(16.dp))
            }

            // 标题紧挨着返回按钮
            Text(
                text = title,
                fontSize = 20.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // 右侧区域 - 两个图标按钮
        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (rightIcon1 != null && onRightIcon1Click != null) {
                IconButton(onClick = onRightIcon1Click) {
                    Icon(
                        imageVector = rightIcon1,
                        contentDescription = null
                    )
                }
            }

            if (rightIcon2 != null && onRightIcon2Click != null) {
                IconButton(onClick = onRightIcon2Click) {
                    Icon(
                        imageVector = rightIcon2,
                        contentDescription = null
                    )
                }
            }

            // 右侧内边距
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}
