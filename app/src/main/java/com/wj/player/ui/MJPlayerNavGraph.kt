package com.wj.player.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
    navActions: MJPlayerNavigationActions = remember(navController) {
        MJPlayerNavigationActions(navController)
    },
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
                    navActions.navigateToSearch()
                },
                onVideoClick = { videoEntity ->
                    navActions.navigateToPlayer(videoEntity.id)
                },
                onNavigateToThemeSettings = {
                    navActions.navigateToThemeSettings()
                },
                onNavigateToVideoSettings = {
                    navActions.navigateToVideoSettings()
                },
                onFloatingBarClick = {
                    navActions.navigateToPlayer(1L)
                },
            )
        }

        composable(MJPlayerDestinations.SEARCH_ROUTE) { backStackEntry ->
            SearchScreen(
                modifier = modifier,
                onNavigateBack = { navController.popBackStack() },
            )
        }

        composable(
            MJPlayerDestinations.PLAYER_ROUTE,
            arguments = listOf(
                navArgument(MJPlayerDestinationsArgs.VIDEO_ID_ARG) {
                    type = NavType.LongType; defaultValue = 0L
                },
            ),
        ) { entry ->
            // 获取参数
            val videoId = entry.arguments?.getLong(MJPlayerDestinationsArgs.VIDEO_ID_ARG) ?: 0L
            PlayerScreen(
                itemId = videoId,
                onBackClick = { navController.popBackStack() },
            )
        }

        composable(MJPlayerDestinations.THEME_SETTINGS_ROUTE) {
            ThemeSettingsScreen(
                modifier = modifier,
                onNavigateBack = { navController.popBackStack() },
            )
        }

        composable(MJPlayerDestinations.VIDEO_SETTINGS_ROUTE) {
            VideoSettingsScreen(
                modifier = modifier,
                onNavigateBack = { navController.popBackStack() },
            )
        }
    }
}
