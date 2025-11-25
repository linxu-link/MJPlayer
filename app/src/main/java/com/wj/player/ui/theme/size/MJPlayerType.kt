package com.wj.player.ui.theme.size

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

data class MJPlayerTypography(
    val title: TextStyle,
    val body: TextStyle,
    val caption: TextStyle,
    val button: TextStyle,
)

val LocalTypography = compositionLocalOf<MJPlayerTypography> {
    MJPlayerTypography(
        title = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium),
        body = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium),
        button = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium),
        caption = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal),
    )
}
