package com.wj.player.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import com.wj.player.ui.pager.settings.theme.ThemeScreen
import com.wj.player.ui.pager.settings.video.VideoScreen
import com.wj.player.ui.pager.videolist.VideoListScreen
import kotlinx.coroutines.CoroutineScope

const val NAVIGATION_ANIMATION_DURATION = 450
const val NAVIGATION_ANIMATION_FADE_DURATION = 400

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

        // [A] --(navigate to B)--> [A, B]
        //- A 执行: exitTransition
        //- B 执行: enterTransition

        // [A, B] --(popBackStack)--> [A]
        // - B 执行: popExitTransition
        // - A 执行: popEnterTransition

        composable(
            route = MJPlayerDestinations.VIDEO_LIST_ROUTE,
            enterTransition = {
                slideInHorizontally(
                    animationSpec = tween(NAVIGATION_ANIMATION_DURATION),
                    initialOffsetX = { it },
                ) + fadeIn(animationSpec = tween(NAVIGATION_ANIMATION_FADE_DURATION))
            },
            exitTransition = null,
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -it }) + fadeIn()
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
            },
        ) {
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

        composable(
            route = MJPlayerDestinations.SEARCH_ROUTE,
            enterTransition = {
                slideInHorizontally(
                    animationSpec = tween(NAVIGATION_ANIMATION_DURATION),
                    initialOffsetX = { it },
                ) + fadeIn(animationSpec = tween(NAVIGATION_ANIMATION_FADE_DURATION))
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(NAVIGATION_ANIMATION_DURATION),
                    targetOffsetX = { -it },
                ) + fadeOut(animationSpec = tween(NAVIGATION_ANIMATION_FADE_DURATION))
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -it }) + fadeIn()
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
            },
        ) { backStackEntry ->
            SearchScreen(
                modifier = modifier,
                onNavigateBack = { navController.popBackStack() },
                onVideoClick = { video ->
                    navActions.navigateToPlayer(video.id)
                },
            )
        }

        composable(
            route = MJPlayerDestinations.PLAYER_ROUTE,
            enterTransition = null,
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(NAVIGATION_ANIMATION_DURATION),
                    targetOffsetX = { -it },
                ) + fadeOut(animationSpec = tween(NAVIGATION_ANIMATION_FADE_DURATION))
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -it }) + fadeIn()
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
            },
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

        composable(
            route = MJPlayerDestinations.THEME_SETTINGS_ROUTE,
            enterTransition = {
                slideInHorizontally(
                    animationSpec = tween(NAVIGATION_ANIMATION_DURATION),
                    initialOffsetX = { it },
                ) + fadeIn(animationSpec = tween(NAVIGATION_ANIMATION_FADE_DURATION))
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(NAVIGATION_ANIMATION_DURATION),
                    targetOffsetX = { -it },
                ) + fadeOut(animationSpec = tween(NAVIGATION_ANIMATION_FADE_DURATION))
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -it }) + fadeIn()
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
            },
        ) {
            ThemeScreen(
                modifier = modifier,
                onNavigateBack = { navController.popBackStack() },
            )
        }

        composable(
            route = MJPlayerDestinations.VIDEO_SETTINGS_ROUTE,
            enterTransition = {
                slideInHorizontally(
                    animationSpec = tween(NAVIGATION_ANIMATION_DURATION),
                    initialOffsetX = { it },
                ) + fadeIn(animationSpec = tween(NAVIGATION_ANIMATION_FADE_DURATION))
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(NAVIGATION_ANIMATION_DURATION),
                    targetOffsetX = { -it },
                ) + fadeOut(animationSpec = tween(NAVIGATION_ANIMATION_FADE_DURATION))
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -it }) + fadeIn()
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
            },
        ) {
            VideoScreen(
                modifier = modifier,
                onNavigateBack = { navController.popBackStack() },
            )
        }
    }
}
