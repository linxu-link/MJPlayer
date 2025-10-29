package com.wj.player.ui.screen.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wj.player.ui.view.header.CustomHeader

@Composable
fun SettingsScreen(onNavigateBack: () -> Unit) {
    // 主内容区域
    Column {
        // 空白
        Spacer(modifier = Modifier.height(24.dp))
        // 带返回按钮和右侧两个图标的头部
        CustomHeader(
            title = "设置",
            showBackButton = true,
            onBackClick = { onNavigateBack() },
        )
    }
}
