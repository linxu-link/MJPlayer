package com.wj.player.ui.theme.dimens

import androidx.compose.ui.unit.Dp

// 比例尺寸（根据屏幕宽度动态计算）
// 技术储备，暂时不使用
object RatioDimensions {
    // 比例系数（占屏幕宽度的百分比）
    const val CONTENT_WIDTH_RATIO = 0.9f // 内容宽度占屏幕90%
    const val CARD_HEIGHT_RATIO = 0.2f // 卡片高度占屏幕20%

    // 动态计算尺寸（需传入屏幕尺寸）
    fun getContentWidth(screenWidth: Dp): Dp = screenWidth * CONTENT_WIDTH_RATIO
    fun getCardHeight(screenHeight: Dp): Dp = screenHeight * CARD_HEIGHT_RATIO

    // 固定比例的间距（如屏幕宽度的2%）
    fun getSpacingMedium(screenWidth: Dp): Dp = screenWidth * 0.02f
}

//BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
//    val screenWidth = maxWidth
//    val screenHeight = maxHeight
//
//    val contentWidth = RatioDimensions.getContentWidth(screenWidth)
//    val cardHeight = RatioDimensions.getCardHeight(screenHeight)
//    val spacing = RatioDimensions.getSpacingMedium(screenWidth)
//
//    Column(modifier = Modifier.width(contentWidth).padding(spacing)) {
//        Card(modifier = Modifier.height(cardHeight)) { /* ... */ }
//    }
//}
