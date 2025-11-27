package com.wj.player.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.wj.player.ui.pager.settings.theme.ThemeScreen
import com.wj.player.ui.pager.settings.video.VideoScreen
import com.wj.player.ui.pager.videolist.VideoListScreen
import kotlinx.coroutines.CoroutineScope

@Composable
fun MJNaviGraph(
    modifier: Modifier,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    startDestination: String = MJPlayerDestinations.THEME_SETTINGS_ROUTE,
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
                onVideoClick = { },
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


        composable(MJPlayerDestinations.THEME_SETTINGS_ROUTE) {
            ThemeScreen(
                modifier = modifier,
                onNavigateBack = { navController.popBackStack() },
            )
        }

        composable(MJPlayerDestinations.VIDEO_SETTINGS_ROUTE) {
            VideoScreen(
                modifier = modifier,
                onNavigateBack = { navController.popBackStack() },
            )
        }
    }
}
