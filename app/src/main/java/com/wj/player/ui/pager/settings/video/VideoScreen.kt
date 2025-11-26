package com.wj.player.ui.pager.settings.video

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wj.player.R
import com.wj.player.ui.view.header.CommonTopAppBar

@Composable
fun VideoScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
) {
    // 主内容区域
    Column(modifier = modifier) {
        CommonTopAppBar(
            title = R.string.menu_settings_video,
            onBack = onNavigateBack,
        )
    }
}
