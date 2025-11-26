package com.wj.player.ui.view.player.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wj.player.MJConstants
import com.wj.player.ui.view.IconTint
import com.wj.player.ui.view.TextCaption
import com.wj.player.ui.view.noRippleClickable
import org.w3c.dom.Text

/**
 * 纯手工实现Android标准ActionBar（不依赖TopAppBar）
 * @param title ActionBar标题文本
 * @param onBackClick 返回按钮点击事件
 * @param rightIcon 右侧图标（默认更多选项）
 * @param onRightIconClick 右侧图标点击事件
 * @param leftIcon 自定义返回图标（默认系统箭头）
 * @param contentColor 内容色（默认主题OnPrimaryContainer）
 */
@Composable
fun TopPlayerActionBar(
    title: String,
    onBackClick: () -> Unit,
    leftIcon: @Composable () -> Unit,
    rightIcon: @Composable (() -> Unit)? = null,
    rightIconText: String = "倍速",
    onRightIconClick: () -> Unit = {},
    contentColor: Color = Color.White,
) {
    // 标准ActionBar高度：56.dp（Material Design规范）
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 4.dp), // 左右内边距
        contentAlignment = Alignment.Center,
    ) {
        // 整体横向布局：左侧返回区 + 中间占位 + 右侧图标
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // ========== 左侧：返回按钮 + 标题文本 ==========
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // 返回按钮
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(48.dp),
                ) {
                    leftIcon()
                }

                // 标题文本
                TextCaption(
                    text = title,
                    color = contentColor,
                )
            }

            // ========== 中间：占位（推挤右侧元素到末尾） ==========
            Spacer(modifier = Modifier.weight(1f))

            // 只有当rightIcon不为null时才显示图标按钮
            if (rightIcon != null) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .noRippleClickable {
                            onRightIconClick()
                        },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(
                        onClick = onRightIconClick,
                        modifier = Modifier.size(48.dp),
                    ) {
                        rightIcon()
                    }

                    Text(
                        text = rightIconText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = contentColor,
                        maxLines = 1, // 限制单行显示
                    )
                }
            }
        }
    }
}


// 使用示例
@Preview(showBackground = true)
@Composable
fun ActionBarDemo() {
    TopPlayerActionBar(
        title = "视频播放",
        onBackClick = { /* 返回上一页逻辑 */ },
        contentColor = Color.White,
        leftIcon = { IconTint(MJConstants.Icon.ARROW_BACK, contentDescription = "返回") },
        rightIcon = { IconTint(MJConstants.Icon.PLAYER_SPEED, contentDescription = "速度") },
    )
}
