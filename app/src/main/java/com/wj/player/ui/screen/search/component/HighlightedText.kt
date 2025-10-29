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

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

/**
 * 在文本中高亮显示与关键词匹配的部分（大小写不敏感）
 * @param text 原始文本（如视频标题）
 * @param keyword 搜索关键词（用于匹配高亮）
 * @param style 文本整体样式
 * @param highlightStyle 高亮部分的样式（默认使用主题强调色）
 */
@Composable
fun HighlightedText(
    text: String,
    keyword: String,
    style: TextStyle,
    highlightStyle: SpanStyle = SpanStyle(
        color = MaterialTheme.colorScheme.primary,
        fontWeight = MaterialTheme.typography.bodyMedium.fontWeight
    )
) {
    // 处理空关键词或空文本的特殊情况
    if (keyword.isBlank() || text.isBlank()) {
        androidx.compose.material3.Text(
            text = text,
            style = style
        )
        return
    }

    // 构建带高亮的 AnnotatedString
    val annotatedText = buildAnnotatedString {
        val lowerText = text.lowercase()
        val lowerKeyword = keyword.lowercase()
        var startIndex = 0

        // 循环查找所有匹配的关键词位置
        while (true) {
            val keywordStart = lowerText.indexOf(lowerKeyword, startIndex)
            if (keywordStart == -1) {
                // 没有更多匹配，添加剩余文本
                append(text.substring(startIndex))
                break
            }

            // 添加关键词前的普通文本
            append(text.substring(startIndex, keywordStart))

            // 添加高亮的关键词
            val keywordEnd = keywordStart + lowerKeyword.length
            withStyle(highlightStyle) {
                append(text.substring(keywordStart, keywordEnd))
            }

            // 移动起始位置继续查找
            startIndex = keywordEnd
        }
    }

    // 显示带高亮的文本
    androidx.compose.material3.Text(
        text = annotatedText,
        style = style
    )
}