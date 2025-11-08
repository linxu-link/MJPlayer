package com.wj.player.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.wj.player.ui.pager.player.PlayerScreen
import com.wj.player.ui.pager.search.SearchScreen
import com.wj.player.ui.pager.settings.ThemeSettingsScreen
import com.wj.player.ui.pager.settings.VideoSettingsScreen
import com.wj.player.ui.pager.videolist.VideoListScreen
import kotlinx.coroutines.CoroutineScope

@Composable
fun MJNaviGraph(
    modifier: Modifier,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    startDestination: String = MJPlayerDestinations.VIDEO_LIST_ROUTE,
) {
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: startDestination

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(MJPlayerDestinations.VIDEO_LIST_ROUTE) {
            VideoListScreen(
                modifier = modifier,
                onNavigateToSearch = {
                    navController.navigate(MJPlayerDestinations.SEARCH_ROUTE)
                },
                onVideoClick = { videoEntity ->
                    navController.navigate(
                        MJPlayerDestinations.PLAYER_ROUTE + "/${videoEntity.id}",
                    )
                },
                onNavigateToThemeSettings = {
                    navController.navigate(MJPlayerDestinations.THEME_SETTINGS_ROUTE)
                },
                onNavigateToVideoSettings = {
                    navController.navigate(MJPlayerDestinations.VIDEO_SETTINGS_ROUTE)
                },
                onFloatingBarClick = {

                },
            )
        }

        composable(MJPlayerDestinations.SEARCH_ROUTE) { backStackEntry ->
            SearchScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }

        composable(MJPlayerDestinations.PLAYER_ROUTE) { backStackEntry ->
            // 获取参数
            val itemId = backStackEntry.arguments?.getString("itemId") ?: "0"

            PlayerScreen(
                itemId = itemId,
                onNavigateBack = { navController.popBackStack() },
            )
        }

        composable(MJPlayerDestinations.THEME_SETTINGS_ROUTE) {
            ThemeSettingsScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }

        composable(MJPlayerDestinations.VIDEO_SETTINGS_ROUTE) {
            VideoSettingsScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }
    }
}
