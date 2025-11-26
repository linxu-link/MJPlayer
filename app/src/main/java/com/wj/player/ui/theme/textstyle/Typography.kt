package com.wj.player.ui.theme.textstyle

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

data class Typography(
    val title: TextStyle,
    val body: TextStyle,
    val caption: TextStyle,
    val small: TextStyle,
    val button: TextStyle,
)

val LocalTypography = compositionLocalOf<Typography> {
    Typography(
        title = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium),
        body = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium),
        button = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium),
        caption = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal),
        small = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Normal),
    )
}
