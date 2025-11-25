package com.wj.player.ui

import androidx.navigation.NavHostController
import com.wj.player.ui.MJPlayerDestinations.PLAYER_ROUTE
import com.wj.player.ui.MJPlayerDestinationsArgs.VIDEO_ID_ARG
import com.wj.player.ui.MJPlayerScreens.PLAYER_SCREEN

/**
 * Screens used in [MJPlayerDestinations]
 */
private object MJPlayerScreens {
    const val HOME_SCREEN = "home_screen"
    const val VIDEO_LIST_SCREEN = "video_list_screen"
    const val PLAYER_SCREEN = "player_screen"
    const val SEARCH_SCREEN = "search_screen"
    const val THEME_SETTINGS_SCREEN = "theme_settings_screen"
    const val VIDEO_SETTINGS_SCREEN = "video_settings_screen"
}

/**
 * Arguments used in [MJPlayerDestinations] routes
 */
object MJPlayerDestinationsArgs {
    const val VIDEO_ID_ARG = "videoId"

    const val USER_MESSAGE_ARG = "userMessage"
    const val TITLE_ARG = "title"
}

object MJPlayerDestinations {
    const val HOME_ROUTE = MJPlayerScreens.HOME_SCREEN
    const val VIDEO_LIST_ROUTE = MJPlayerScreens.VIDEO_LIST_SCREEN
    const val PLAYER_ROUTE = "$PLAYER_SCREEN?$VIDEO_ID_ARG={$VIDEO_ID_ARG}"
    const val SEARCH_ROUTE = MJPlayerScreens.SEARCH_SCREEN
    const val THEME_SETTINGS_ROUTE = MJPlayerScreens.THEME_SETTINGS_SCREEN
    const val VIDEO_SETTINGS_ROUTE = MJPlayerScreens.VIDEO_SETTINGS_SCREEN
}

class MJPlayerNavigationActions(navController: NavHostController) {
    private val _navController = navController

    fun navigateToVideoList() {
        _navController.navigate(MJPlayerDestinations.VIDEO_LIST_ROUTE)
    }

    fun navigateToSearch() {
        _navController.navigate(MJPlayerDestinations.SEARCH_ROUTE)
    }

    fun navigateToPlayer(videoId: Long) {
        _navController.navigate(PLAYER_ROUTE.replace("{$VIDEO_ID_ARG}", videoId.toString()))
    }

    fun navigateToThemeSettings() {
        _navController.navigate(MJPlayerDestinations.THEME_SETTINGS_ROUTE)
    }

    fun navigateToVideoSettings() {
        _navController.navigate(MJPlayerDestinations.VIDEO_SETTINGS_ROUTE)
    }
}
