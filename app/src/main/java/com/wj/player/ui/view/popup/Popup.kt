package com.wj.player.ui.view.popup

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

/**
 * 自定义 Popup 组件
 *
 * @param isVisible 是否显示 Popup
 * @param onDismiss 关闭 Popup 的回调
 * @param anchorPosition 锚点位置信息（用于定位）
 * @param content Popup 内容
 * @param backgroundColor 背景颜色
 * @param shape 形状
 * @param elevation 阴影高度
 * @param contentPadding 内容内边距
 * @param dismissOnBackPress 按返回键是否关闭
 * @param dismissOnClickOutside 点击外部是否关闭
 */
@Composable
fun HiPopup(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    anchorPosition: AnchorPosition,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    shape: RoundedCornerShape = RoundedCornerShape(8.dp),
    elevation: Int = 4,
    contentPadding: Int = 16,
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    content: @Composable () -> Unit,
) {
    // 动画状态管理
    val transitionState = remember {
        MutableTransitionState(false).apply {
            targetState = isVisible
        }
    }
    val transition = updateTransition(transitionState, label = "popup_animation")

    // 透明度动画
    val alpha by transition.animateFloat(
        transitionSpec = { tween(200) },
        label = "alpha_animation",
    ) { state ->
        if (state) 1f else 0f
    }

    // 缩放动画
    val scale by transition.animateFloat(
        transitionSpec = { tween(200) },
        label = "scale_animation",
    ) { state ->
        if (state) 1f else 0.8f
    }

    // 当动画结束且目标状态为不可见时，触发关闭回调
    LaunchedEffect(transitionState.currentState, transitionState.targetState) {
        if (!transitionState.currentState && !transitionState.targetState) {
            onDismiss()
        }
    }

    if (transitionState.targetState || transitionState.currentState) {
        Popup(
            alignment = anchorPosition.alignment,
            offset = anchorPosition.offset,
            properties = PopupProperties(
                dismissOnBackPress = dismissOnBackPress,
                dismissOnClickOutside = dismissOnClickOutside,
                excludeFromSystemGesture = false,
            ),
            onDismissRequest = {
                if (isVisible) {
                    transitionState.targetState = false
                }
            },
        ) {
            // 半透明背景（点击可关闭）
            if (dismissOnClickOutside) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.0f * alpha))
                        .clickable {
                            transitionState.targetState = false
                        },
                )
            }

            // Popup 内容容器
            Box(
                modifier = Modifier
                    .alpha(alpha)
                    .scale(scale)
                    .background(
                        color = backgroundColor,
                        shape = shape,
                    )
                    .shadow(elevation.dp, shape)
                    .padding(contentPadding.dp),
            ) {
                content()
            }
        }
    }
}

/**
 * 用于定位 Popup 的锚点位置数据类
 *
 * @param alignment 对齐方式
 * @param offset 偏移量（x, y）
 */
data class AnchorPosition(
    val alignment: Alignment = Alignment.TopStart,
    val offset: IntOffset = IntOffset(0, 0),
)

/**
 * 获取组件位置并显示 Popup 的示例用法
 */
@Composable
fun PopupExample() {
    var showPopup by remember { mutableStateOf(false) }
    var anchorPosition by remember { mutableStateOf(AnchorPosition()) }

    Box(modifier = Modifier.fillMaxSize()) {
        // 触发 Popup 显示的按钮
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(100.dp)
                .background(MaterialTheme.colorScheme.primary)
                .clickable { showPopup = true }
                .onGloballyPositioned { coordinates ->
                    // 获取组件在窗口中的位置
                    val position = coordinates.positionInWindow()
                    anchorPosition = AnchorPosition(
                        alignment = Alignment.BottomStart,
                        offset = IntOffset(
                            position.x.toInt(),
                            position.y.toInt() + coordinates.size.height,
                        ),
                    )
                },
        )

        // 显示自定义 Popup
        if (showPopup) {
            HiPopup(
                isVisible = showPopup,
                onDismiss = { showPopup = false },
                anchorPosition = anchorPosition,
            ) {
                // Popup 内容
                Box(modifier = Modifier.size(200.dp)) {
                    androidx.compose.material3.Text(
                        text = "这是一个自定义 Popup",
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }
}
