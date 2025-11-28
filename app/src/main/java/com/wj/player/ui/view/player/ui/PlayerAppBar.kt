package com.wj.player.ui.view.player.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.Player
import com.wj.player.MJConstants
import com.wj.player.ui.theme.dimens.LocalAppDimensions
import com.wj.player.ui.view.IconTint
import com.wj.player.ui.view.TextCaption
import com.wj.player.ui.view.noRippleClickable
import com.wj.player.ui.view.player.ExoplayerControllerImpl
import com.wj.player.ui.view.player.base.PlayerState
import com.wujia.toolkit.utils.HiLog
import java.util.Locale

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
fun PlayerActionBar(
    modifier: Modifier = Modifier,
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
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .height(56.dp),
        contentAlignment = Alignment.Center,
    ) {
        // 整体横向布局：左侧返回区 + 中间占位 + 右侧图标
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // ========== 左侧：返回按钮 + 标题文本 ==========
            val maxWidth = LocalConfiguration.current.screenWidthDp.dp / 2

            Row(
                modifier = Modifier.widthIn(maxWidth),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // 返回按钮
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier,
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
                        modifier = Modifier,
                    ) {
                        rightIcon()
                    }

                    TextCaption(
                        text = rightIconText,
                        color = contentColor,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerSeekBar(
    modifier: Modifier = Modifier,
    state: PlayerState,
    config: PlayerUiConfig,
    onPlayPause: () -> Unit,
    onSeek: (Long) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(start = 0.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        // 播放/暂停按钮：左对齐
        IconButton(
            onClick = onPlayPause,
            modifier = Modifier,
        ) {
            if (state.isPlaying) {
                config.pauseIcon()
            } else {
                config.playIcon()
            }
        }
        // 左侧时间文本（当前进度）
        Text(
            text = formatTime(state.currentPosition),
            color = config.controlsContentColor,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 4.dp),
        )
        // Slider：居中占主要宽度
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
                .height(24.dp),
            contentAlignment = Alignment.Center,
        ) {
            Slider(
                value = state.currentPosition.toFloat(),
                onValueChange = { onSeek(it.toLong()) },
                valueRange = 0f..state.duration.toFloat(),
                track = { sliderPositions ->
                    HiLog.e("sliderPositions.value: ${sliderPositions.value}")
                    Box(
                        modifier = Modifier
                            .height(20.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        // 背景轨道（未播放部分）
                        Box(
                            modifier = Modifier
                                .height(4.dp)
                                .fillMaxWidth()
                                .background(
                                    color = config.controlsContentColor.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(2.dp),
                                ),
                        )
                        // 已播放进度条
                        Box(
                            modifier = Modifier
                                .height(4.dp)
                                .fillMaxWidth(fraction = sliderPositions.value / state.duration.toFloat())
                                .background(
                                    color = config.controlsContentColor,
                                    shape = RoundedCornerShape(2.dp),
                                )
                                .align(Alignment.CenterStart),
                        )
                    }
                },
                // 自定义滑块样式
                thumb = {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .background(
                                    color = config.controlsContentColor,
                                    shape = CircleShape,
                                ),
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        // 右侧时间文本（总时长）
        Text(
            text = formatTime(state.duration),
            color = config.controlsContentColor,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 4.dp),
        )
    }

}

// 辅助函数：格式化时间
private fun formatTime(millis: Long): String {
    val totalSeconds = millis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format(Locale.CHINA, "%02d:%02d", minutes, seconds)
}

// 使用示例
@Preview(showBackground = true)
@Composable
fun ActionBarDemo() {
    PlayerActionBar(
        title = "视频播放",
        onBackClick = { /* 返回上一页逻辑 */ },
        contentColor = Color.White,
        leftIcon = { IconTint(MJConstants.Icon.ARROW_BACK, contentDescription = "返回") },
        rightIcon = { IconTint(MJConstants.Icon.PLAYER_SPEED, contentDescription = "速度") },
    )

    Spacer(modifier = Modifier.height(58.dp))


    val playerUiConfig = remember {
        PlayerUiConfig.Builder()
            .showSpeedControl(true)
            .showBottomSeekBar(true)
            .showBufferingIndicator(true)
            .build()
    }
    val context = LocalContext.current

    val playerController = remember {
        ExoplayerControllerImpl.Builder(context)
            .setRepeatMode(Player.REPEAT_MODE_OFF)
            .setSeekForwardIncrementMs(15_000)
            .setSeekBackIncrementMs(5_000)
            .enableCache(100 * 1024 * 1024, "exoplayer_cache")
            .build()
    }

    PlayerSeekBar(
        state = PlayerState(),
        config = playerUiConfig,
        onPlayPause = { },
        onSeek = {},
    )
}
