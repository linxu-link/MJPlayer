package com.wj.player.ui.pager.settings.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wj.player.R
import com.wj.player.data.entity.ThemeEntity
import com.wj.player.data.entity.getColor
import com.wj.player.data.entity.getGradientColors
import com.wj.player.data.entity.isGradient
import com.wj.player.ui.theme.ThemeType
import com.wj.player.ui.theme.colors.Colors
import com.wj.player.ui.theme.dimens.LocalAppDimensions
import com.wj.player.ui.theme.resource.LocalImageScheme
import com.wj.player.ui.view.TextBody
import com.wj.player.ui.view.TextCaption
import com.wj.player.ui.view.TextSmall
import com.wj.player.ui.view.header.CommonTopAppBar
import com.wj.player.ui.view.noRippleClickable
import kotlin.math.ceil


@Composable
fun ThemeScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        CommonTopAppBar(
            title = R.string.menu_settings_theme,
            onBack = onNavigateBack,
        )

        ThemeContent(
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
private fun ThemeContent(
    androidThemes: List<ThemeEntity>,
    classicThemes: List<ThemeEntity>,
    otherThemes: List<ThemeEntity>,
    onThemeSelected: (ThemeEntity) -> Unit,
    onClassicThemeSelected: (ThemeEntity) -> Unit,
    onOtherThemeSelected: (ThemeEntity) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        // 顶部主题切换栏
        item {
            TopThemeList(
                androidThemes = androidThemes,
                onThemeSelected = onThemeSelected,
            )
        }

        // 经典主题部分
        item {
            Column {
                TextCaption(text = stringResource(R.string.classic_themes))

                Spacer(modifier = Modifier.height(12.dp))

                ClassicThemeGrid(
                    themes = classicThemes,
                    onThemeSelected = onClassicThemeSelected,
                )
            }
        }

        // 其他主题部分
//        item {
//            Column {
//                TextCaption(text = stringResource(R.string.other_themes))
//
//                Spacer(modifier = Modifier.height(12.dp))
//
//                OtherThemeGrid(
//                    themes = otherThemes,
//                    onThemeSelected = onOtherThemeSelected,
//                )
//            }
//        }
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
        modifier = modifier
            .height(70.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        androidThemes.forEach { theme ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .noRippleClickable { onThemeSelected(theme) },
            ) {
                Box(
                    modifier = Modifier
                        .padding(3.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.CenterStart,
                ) {

                    Image(
                        modifier = Modifier.matchParentSize().run {
                            when (theme.themeType) {
                                ThemeType.ADAPTIVE -> Modifier.background(Colors.adaptiveThemeBg)
                                ThemeType.LIGHT -> Modifier.background(Colors.lightThemeBg)
                                ThemeType.DARK -> Modifier.background(Colors.darkThemeBg)
                                else -> Modifier
                            }
                        },
                        painter = when (theme.themeType) {
                            ThemeType.ADAPTIVE -> painterResource(LocalImageScheme.current.adaptiveThemeMask)
                            ThemeType.LIGHT -> painterResource(LocalImageScheme.current.lightThemeMask)
                            ThemeType.DARK -> painterResource(LocalImageScheme.current.darkThemeMask)
                            else -> painterResource(LocalImageScheme.current.adaptiveThemeMask)
                        },
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                    )


                    TextBody(
                        text = theme.name,
                        color = when (theme.themeType) {
                            ThemeType.ADAPTIVE -> Colors.black
                            ThemeType.LIGHT -> Colors.black
                            ThemeType.DARK -> Colors.white
                            else -> Colors.white
                        },
                        modifier = Modifier.padding(horizontal = 8.dp),
                    )
                }

                if (theme.isSelected) {
                    TextSmall(
                        text = "Using",
                        textAlign = TextAlign.Center,
                        color = Colors.white,
                        modifier = Modifier
                            .background(color = Colors.themeMark, shape = RoundedCornerShape(4.dp))
                            .align(Alignment.TopEnd)
                            .padding(horizontal = 4.dp, vertical = 2.dp),
                    )
                }
            }
        }
    }
}


// 替换 ClassicThemeItem，使用非惰性布局
@Composable
private fun ClassicThemeGrid(
    modifier: Modifier = Modifier,
    themes: List<ThemeEntity>,
    onThemeSelected: (ThemeEntity) -> Unit,
) {
    // 计算列数（6列）
    val columns = 6
    val rows = ceil(themes.size.toDouble() / columns).toInt()

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        for (row in 0 until rows) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                for (col in 0 until columns) {
                    val index = row * columns + col
                    if (index < themes.size) {
                        val theme = themes[index]
                        ClassicThemeItem(
                            theme = theme,
                            onThemeSelected = onThemeSelected,
                            modifier = Modifier.weight(1f),
                        )
                    } else {
                        // 空项填充
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

// 单个经典主题项
@Composable
private fun ClassicThemeItem(
    theme: ThemeEntity,
    onThemeSelected: (ThemeEntity) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .noRippleClickable { onThemeSelected(theme) },
    ) {
        if (theme.isGradient()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp)
                    .clip(RoundedCornerShape(8.dp)),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(LocalAppDimensions.current.colorBlockSize / 2)
                        .align(Alignment.TopCenter)
                        .background(theme.getGradientColors()[0]),
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(LocalAppDimensions.current.colorBlockSize / 2)
                        .align(Alignment.BottomCenter)
                        .background(theme.getGradientColors()[1]),
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(theme.getColor()),
            )
        }

        if (theme.isSelected) {
            TextSmall(
                text = "Using",
                textAlign = TextAlign.Center,
                color = Colors.white,
                modifier = Modifier
                    .background(color = Colors.themeMark, shape = RoundedCornerShape(4.dp))
                    .align(Alignment.TopEnd)
                    .padding(horizontal = 4.dp, vertical = 2.dp),
            )
        }
    }
}

// 替换 OtherThemeItem，使用非惰性布局
@Composable
private fun OtherThemeGrid(
    modifier: Modifier = Modifier,
    themes: List<ThemeEntity>,
    onThemeSelected: (ThemeEntity) -> Unit,
) {
    // 计算列数（3列）
    val columns = 3
    val rows = ceil(themes.size.toDouble() / columns).toInt()

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        for (row in 0 until rows) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                for (col in 0 until columns) {
                    val index = row * columns + col
                    if (index < themes.size) {
                        val theme = themes[index]
                        OtherThemeCard(
                            theme = theme,
                            onThemeSelected = onThemeSelected,
                            modifier = Modifier.weight(1f),
                        )
                    } else {
                        // 空项填充
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

// 单个其他主题卡片
@Composable
private fun OtherThemeCard(
    theme: ThemeEntity,
    onThemeSelected: (ThemeEntity) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .noRippleClickable { onThemeSelected(theme) },
    ) {
        // 封面图
        Image(
            painter = painterResource(theme.iconRes),
            contentDescription = theme.name,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
        )
        // 主题类型文字
        TextCaption(
            text = theme.name,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .padding(4.dp)
                .align(Alignment.CenterHorizontally),
        )
    }
}

@Preview
@Composable
private fun TopThemeListPreview() {
    TopThemeList(
        androidThemes = listOf(
            ThemeEntity(
                "Adaptive",
                R.drawable.theme_list_dark_mask,
                themeType = ThemeType.ADAPTIVE,
                isSelected = true,
            ),
            ThemeEntity(
                "Light Theme",
                R.drawable.theme_list_dark_mask,
                themeType = ThemeType.LIGHT,
            ),
            ThemeEntity("Dark Theme", R.drawable.theme_list_dark_mask, themeType = ThemeType.DARK),
        ),
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

    OtherThemeGrid(
        themes = otherThemes,
        onThemeSelected = {},
    )
}




