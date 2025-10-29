@file:OptIn(ExperimentalMaterial3Api::class)

package com.wj.player.ui.view.header

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.wj.player.R
import com.wj.player.ui.MJPlayerTheme

@Composable
fun VideoListTopAppBar(
    onSearch: () -> Unit,
    onFilterAllTasks: () -> Unit,
    onFilterActiveTasks: () -> Unit,
    onFilterCompletedTasks: () -> Unit,
    onClearCompletedTasks: () -> Unit,
    onRefresh: () -> Unit,
) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = onSearch) {
                Icon(Icons.Filled.Search, stringResource(id = R.string.search))
            }
        },
        actions = {
            FilterTasksMenu(
                onFilterAllTasks,
                onFilterActiveTasks,
                onFilterCompletedTasks,
            )
            MoreTasksMenu(
                onClearCompletedTasks,
                onRefresh,
            )
        },
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
fun AddEditTaskTopAppBar(@StringRes title: Int, onBack: () -> Unit) {
    TopAppBar(
        title = { Text(text = stringResource(title)) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(id = R.string.menu_back))
            }
        },
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun FilterTasksMenu(
    onFilterAllTasks: () -> Unit,
    onFilterActiveTasks: () -> Unit,
    onFilterCompletedTasks: () -> Unit,
) {
    TopAppBarDropdownMenu(
        iconContent = {
            Icon(
                painterResource(id = R.drawable.ic_filter_list),
                stringResource(id = R.string.menu_filter),
            )
        },
    ) { closeMenu ->
        DropdownMenuItem(
            onClick = {
                onFilterAllTasks()
                closeMenu()
            },
            text = { Text(text = stringResource(id = R.string.nav_all)) },
        )
        DropdownMenuItem(
            onClick = {
                onFilterActiveTasks()
                closeMenu()
            },
            text = { Text(text = stringResource(id = R.string.nav_active)) },
        )
        DropdownMenuItem(
            onClick = {
                onFilterCompletedTasks()
                closeMenu()
            },
            text = { Text(text = stringResource(id = R.string.nav_completed)) },
        )
    }
}

@Composable
private fun MoreTasksMenu(
    onClearCompletedTasks: () -> Unit,
    onRefresh: () -> Unit,
) {
    TopAppBarDropdownMenu(
        iconContent = {
            Icon(Icons.Filled.MoreVert, stringResource(id = R.string.menu_more))
        },
    ) { closeMenu ->
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.menu_clear)) },
            onClick = {
                onClearCompletedTasks()
                closeMenu()
            },
        )
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.refresh)) },
            onClick = {
                onRefresh()
                closeMenu()
            },
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
            VideoListTopAppBar({ }, { }, {}, {}, {}, {})
        }
    }
}

@Preview
@Composable
private fun AddEditTaskTopAppBarPreview() {
    MJPlayerTheme {
        Surface {
            AddEditTaskTopAppBar(R.string.app_name) { }
        }
    }
}
