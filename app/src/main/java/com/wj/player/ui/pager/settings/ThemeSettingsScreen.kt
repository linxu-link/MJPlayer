package com.wj.player.ui.pager.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wj.player.R
import com.wj.player.data.entity.ThemeEntity
import com.wj.player.data.entity.getColor
import com.wj.player.ui.theme.colors.LocalColorScheme
import com.wj.player.ui.view.TextCaption
import com.wj.player.ui.view.TextTitle
import com.wj.player.ui.view.header.CommonTopAppBar


@Composable
fun ThemeSettingsScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        CommonTopAppBar(
            title = R.string.menu_settings_theme,
            onBack = onNavigateBack,
        )

        ThemeSettingsContent(
            androidThemes = uiState.value.androidThemes,
            classicThemes = uiState.value.classicThemes,
            otherThemes = uiState.value.otherThemes,
            onThemeSelected = {
                viewModel.selectTheme(it.themeType)
            },
            onClassicThemeSelected = {
                viewModel.selectTheme(it.themeType)
            },
            onOtherThemeSelected = {
                viewModel.selectTheme(it.themeType)
            },
        )
    }
}

@Composable
private fun ThemeSettingsContent(
    androidThemes: List<ThemeEntity>,
    classicThemes: List<ThemeEntity>,
    otherThemes: List<ThemeEntity>,
    onThemeSelected: (ThemeEntity) -> Unit,
    onClassicThemeSelected: (ThemeEntity) -> Unit,
    onOtherThemeSelected: (ThemeEntity) -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        // 顶部主题切换栏（Adaptive/Light/Dark）
        TopThemeList(
            androidThemes = androidThemes,
            onThemeSelected = onThemeSelected,
        )
        Spacer(modifier = Modifier.height(24.dp))

        // 经典主题标题
        Text(text = "Classic Themes", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        ClassicThemeItem(
            themes = classicThemes,
            onThemeSelected = onClassicThemeSelected,
        )
        Spacer(modifier = Modifier.height(24.dp))

        // 其他主题标题
        Text(text = "Other Themes", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        OtherThemeItem(
            themes = otherThemes,
            onThemeSelected = onOtherThemeSelected,
        )
    }
}

// 顶部主题切换栏（Adaptive/Light/Dark）
@Composable
private fun TopThemeList(
    modifier: Modifier = Modifier,
    androidThemes: List<ThemeEntity>,
    onThemeSelected: (ThemeEntity) -> Unit,
) {
    Row(
        modifier = modifier.height(80.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        androidThemes.forEach { theme ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (theme.isSelected) Color(0xFFE3F2FD) else Color.LightGray)
                    .clickable { onThemeSelected(theme) },
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(R.drawable.ic_launcher),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )

                TextTitle(text = theme.name)
                if (theme.isSelected) {
                    TextCaption(
                        text = "Using",
                        textAlign = TextAlign.Center,
                        color = LocalColorScheme.current.white,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black.copy(alpha = 0.5f))
                            .align(Alignment.BottomCenter)
                            .padding(horizontal = 4.dp, vertical = 2.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun ClassicThemeItem(
    modifier: Modifier = Modifier,
    themes: List<ThemeEntity>,
    onThemeSelected: (ThemeEntity) -> Unit,
) {
    // 经典主题色块网格（6列）
    LazyVerticalGrid(
        columns = GridCells.Fixed(6),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        items(themes.size) { index ->
            val theme = themes[index]
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(theme.getColor())
                    .clickable {
                        onThemeSelected(theme)
                    },
            ){
                if (theme.isSelected) {
                    TextCaption(
                        text = "Using",
                        textAlign = TextAlign.Center,
                        color = LocalColorScheme.current.white,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black.copy(alpha = 0.5f))
                            .align(Alignment.BottomCenter)
                            .padding(horizontal = 4.dp, vertical = 2.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun OtherThemeItem(
    modifier: Modifier = Modifier,
    themes: List<ThemeEntity>,
    onThemeSelected: (ThemeEntity) -> Unit,
) {
    // 其他主题卡片（一行3列）
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        itemsIndexed(themes) { index, theme ->
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onThemeSelected(theme) },
            ) {
                // 封面图
                Image(
                    painter = painterResource(theme.iconRes),
                    contentDescription = theme.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                )
                // 主题类型文字
                Text(
                    text = theme.name,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray)
                        .padding(4.dp)
                        .align(Alignment.CenterHorizontally),
                )
            }
        }
    }
}


@Preview
@Composable
private fun TopThemeListPreview() {
    TopThemeList(
        androidThemes = listOf(
            ThemeEntity("Adaptive", isSelected = true),
            ThemeEntity("Light Theme"),
            ThemeEntity("Dark Theme"),
        ),
        onThemeSelected = {},
    )
}

@Preview
@Composable
private fun ClassicThemeItemPreview() {
    val classicThemes = listOf(
        // 单色块
        ThemeEntity(),
        ThemeEntity(),
        ThemeEntity(),
        ThemeEntity(),
        ThemeEntity(),
        ThemeEntity(),
        ThemeEntity(),
        ThemeEntity(),
        ThemeEntity(),
        ThemeEntity(),
        ThemeEntity(),
        ThemeEntity(),
        ThemeEntity(),
        ThemeEntity(),
        ThemeEntity(),
    )
    ClassicThemeItem(
        themes = classicThemes,
        onThemeSelected = {},
    )
}

@Preview
@Composable
private fun OtherThemeItemPreview() {
    val otherThemes = listOf(
        ThemeEntity("Zodiac Aries", R.drawable.ic_launcher),
        ThemeEntity("Zodiac Aries", R.drawable.ic_launcher),
        ThemeEntity("Zodiac Pisces", R.drawable.ic_launcher),
    )

    OtherThemeItem(
        themes = otherThemes,
        onThemeSelected = {},
    )
}





