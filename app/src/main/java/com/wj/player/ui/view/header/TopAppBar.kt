@file:OptIn(ExperimentalMaterial3Api::class)

package com.wj.player.ui.view.header

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wj.player.R
import com.wj.player.ui.theme.MJPlayerTheme
import com.wj.player.ui.theme.colors.LocalColorScheme
import com.wj.player.ui.view.TextTitle
import com.wujia.toolkit.utils.HiLog

@Composable
fun VideoListTopAppBar(
    onSearch: () -> Unit,
    onFilterList: () -> Unit,
    onFilterGrid: () -> Unit,
    onClickThemeSettings: () -> Unit,
    onClickVideoSettings: () -> Unit,
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = LocalColorScheme.current.theme,
        ),
        title = {},
        navigationIcon = {
            IconButton(onClick = onSearch) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(id = R.string.search),
                    tint = LocalColorScheme.current.textPrimary,
                )
            }
        },
        actions = {
            FilterMenu(
                onFilterList,
                onFilterGrid,
            )
            SettingsMenu(
                onClickThemeSettings = onClickThemeSettings,
                onClickVideoSettings = onClickVideoSettings,
            )
        },
    )
}

@Composable
fun CommonTopAppBar(
    @StringRes title: Int,
    onBack: () -> Unit,
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = LocalColorScheme.current.theme,
        ),
        title = {
            TextTitle(text = stringResource(title),)
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.menu_back),
                    tint = LocalColorScheme.current.textPrimary,
                )
            }
        },
    )
}

@Composable
fun SearchTopAppBar(
    text: String = "",
    onSearchTextChange: (String) -> Unit,
    onBack: () -> Unit,
) {
    var searchText by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(text) {
        searchText = text
    }

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = LocalColorScheme.current.theme,
        ),
        title = {},
        navigationIcon = {
            IconButton(onClick = { onBack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "返回",
                    tint = LocalColorScheme.current.textPrimary,
                )
            }
        },
        actions = {
            Row(
                modifier = Modifier.padding(start = 56.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // 搜索框
                BasicTextField(
                    value = searchText,
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusRequester.freeFocus()
                            onSearchTextChange(searchText)
                        },
                    ),
                    onValueChange = {
                        searchText = it
                    },
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                    ),
                    cursorBrush = SolidColor(Color.LightGray), // 明确设置光标颜色
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .height(40.dp)
                        .border(1.dp, Color.Gray, RoundedCornerShape(16.dp))
                        .focusRequester(focusRequester)
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    decorationBox = { innerTextField ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxHeight(), // 确保 Row 填满高度
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "搜索",
                                tint = LocalColorScheme.current.textSecondary,
                                modifier = Modifier.size(20.dp), // 统一图标尺寸
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(Alignment.CenterVertically),
                            ) {
                                if (searchText.isEmpty()) {
                                    Text(
                                        "请输入关键词...",
                                        color = Color.Gray,
                                        fontSize = 16.sp,
                                        modifier = Modifier.align(Alignment.CenterStart),
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .wrapContentHeight(Alignment.CenterVertically),
                                ) {
                                    innerTextField()
                                }
                            }
                        }
                    },
                )
            }
        },
    )
}

@Composable
private fun FilterMenu(
    onFilterList: () -> Unit,
    onFilterPic: () -> Unit,
) {
    TopAppBarDropdownMenu(
        iconContent = {
            Icon(
                painter = painterResource(id = R.drawable.ic_filter_list),
                contentDescription = stringResource(id = R.string.menu_filter),
                tint = LocalColorScheme.current.textPrimary,
            )
        },
    ) { closeMenu ->
        DropdownMenuItem(
            onClick = {
                onFilterList()
                closeMenu()
            },
            text = { Text(text = stringResource(id = R.string.menu_filter_list)) },
        )
        DropdownMenuItem(
            onClick = {
                onFilterPic()
                closeMenu()
            },
            text = { Text(text = stringResource(id = R.string.menu_filter_pic)) },
        )
    }
}

@Composable
private fun SettingsMenu(
    onClickThemeSettings: () -> Unit,
    onClickVideoSettings: () -> Unit,
) {
    TopAppBarDropdownMenu(
        iconContent = {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = stringResource(id = R.string.menu_settings_title),
                tint = LocalColorScheme.current.textPrimary,
            )
        },
    ) { closeMenu ->
        DropdownMenuItem(
            onClick = {
                onClickThemeSettings()
                closeMenu()
            },
            text = { Text(text = stringResource(id = R.string.menu_settings_theme)) },
        )
        DropdownMenuItem(
            onClick = {
                onClickVideoSettings()
                closeMenu()
            },
            text = { Text(text = stringResource(id = R.string.menu_settings_video)) },
        )
    }
}

@Composable
private fun TopAppBarDropdownMenu(
    iconContent: @Composable () -> Unit,
    content: @Composable ColumnScope.(() -> Unit) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
        IconButton(onClick = { expanded = !expanded }) {
            iconContent()
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.wrapContentSize(Alignment.TopEnd),
        ) {
            content { expanded = !expanded }
        }
    }
}

@Preview
@Composable
private fun VideoListTopAppBarPreview() {
    MJPlayerTheme {
        Surface {
            VideoListTopAppBar({ }, { }, {}, {}, {})
        }
    }
}

@Preview
@Composable
private fun AddEditTaskTopAppBarPreview() {
    MJPlayerTheme {
        Surface {
            CommonTopAppBar(R.string.app_name) { }
        }
    }
}

@Preview
@Composable
private fun SearchTopAppBarPreview() {
    MJPlayerTheme {
        Surface {
            SearchTopAppBar(
                onSearchTextChange = {},
                onBack = {},
            )
        }
    }
}
