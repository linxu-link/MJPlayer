package com.wj.player.ui.view.player.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wj.player.R
import com.wj.player.ui.view.noRippleClickable

data class SheetOption(
    val title: String,
    val isSelected: Boolean = false,
    val onClick: () -> Unit,
)

@Composable
fun BottomSheet(
    modifier: Modifier = Modifier,
    options: List<SheetOption> = emptyList(),
    visible: Boolean = true,
    onCloseClick: () -> Unit,
    leftIcon: @Composable () -> Unit = {
        Icon(
            imageVector = Icons.Default.ArrowBackIosNew,
            modifier = Modifier.rotate(
                degrees = 270f,
            ),
            contentDescription = null,
        )
    },
    contentColor: Color = Color.Black,
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { it }, // 从自身高度位置滑入（底部）
            animationSpec = tween(250, easing = LinearOutSlowInEasing),
        ) + fadeIn(tween(150)),
        exit = slideOutVertically(
            targetOffsetY = { it }, // 滑回自身高度位置（底部隐藏）
            animationSpec = tween(100),
        ) + fadeOut(tween(150)),
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = onCloseClick,
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.CenterStart),
                ) { leftIcon() }

                Text(
                    text = "倍速",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = contentColor,
                    maxLines = 1,
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                itemsIndexed(options) { index, option ->
                    SheetOptionItem(option = option)
                }
            }
        }
    }

}

@Composable
private fun SheetOptionItem(option: SheetOption) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .noRippleClickable(
                onClick = { option.onClick() },
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = option.title + if (option.isSelected) stringResource(R.string.speed_selected) else "",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black,
            fontSize = 18.sp,
        )
    }
}

@Preview
@Composable
fun SheetContentDemo() {
    BottomSheet(
        options = listOf(
            SheetOption("3.0x") { /* 选项1点击逻辑 */ },
            SheetOption("2.0x") { /* 选项3点击逻辑 */ },
            SheetOption("1.5x") { /* 选项3点击逻辑 */ },
            SheetOption("1.25x") { /* 选项3点击逻辑 */ },
            SheetOption("1.0x") { /* 选项3点击逻辑 */ },
            SheetOption("0.75x") { /* 选项3点击逻辑 */ },
            SheetOption("0.5x") { /* 选项3点击逻辑 */ },
        ),
        onCloseClick = { /* 关闭底部弹窗逻辑 */ },
    )
}
