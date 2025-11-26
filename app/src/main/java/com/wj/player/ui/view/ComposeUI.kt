package com.wj.player.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.semantics.Role

/**
 * 全局扩展：无涟漪效果的点击Modifier
 * @param enabled 是否启用点击（默认true）
 * @param onClickLabel 无障碍标签（可选）
 * @param role 语义角色（可选）
 * @param onClick 点击回调
 */
fun Modifier.noRippleClickable(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit
): Modifier = composed(
    // composed确保每次调用创建独立的状态（如interactionSource）
    inspectorInfo = {
        name = "noRippleClickable"
        properties["enabled"] = enabled
        properties["onClickLabel"] = onClickLabel
        properties["role"] = role
        properties["onClick"] = onClick
    }
) {
    // 每个组件独立的交互源（用remember避免重组重复创建）
    val interactionSource = remember { MutableInteractionSource() }

    this.clickable(
        interactionSource = interactionSource,
        indication = null, // 禁用涟漪效果
        enabled = enabled,
        onClickLabel = onClickLabel,
        role = role,
        onClick = onClick,
    )
}

