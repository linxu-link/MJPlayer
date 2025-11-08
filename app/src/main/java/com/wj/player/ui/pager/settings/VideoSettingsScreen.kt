package com.wj.player.ui.pager.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.wj.player.R
import com.wj.player.ui.view.header.CommonTopAppBar

@Composable
fun VideoSettingsScreen(onNavigateBack: () -> Unit) {
    // 主内容区域
    Column {
        CommonTopAppBar(
            title = R.string.menu_settings_video,
            onBack = onNavigateBack,
        )
    }
}
