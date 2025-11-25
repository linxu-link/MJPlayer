package com.wj.player.ui.view.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    modifier: Modifier = Modifier,
    highlightStyle: SpanStyle = SpanStyle(
        color = MaterialTheme.colorScheme.primary,
        fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
    ),
) {
    // 处理空关键词或空文本的特殊情况
    if (keyword.isBlank() || text.isBlank()) {
        Text(
            text = text,
            style = style,
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
    Text(
        modifier = modifier,
        maxLines = 1,
        text = annotatedText,
        style = style,
    )
}
