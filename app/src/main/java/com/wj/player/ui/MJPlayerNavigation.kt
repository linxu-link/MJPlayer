package com.wj.player.ui

/**
 * Screens used in [MJPlayerDestinations]
 */
private object MJPlayerScreens {
    const val HOME_SCREEN = "home_screen"
    const val VIDEO_LIST_SCREEN = "video_list_screen"
    const val PLAYER_SCREEN = "player_screen"
    const val SEARCH_SCREEN = "search_screen"
    const val SETTINGS_SCREEN = "settings_screen"
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
    const val PLAYER_ROUTE = "${MJPlayerScreens.PLAYER_SCREEN}/{${MJPlayerDestinationsArgs.VIDEO_ID_ARG}}"
    const val SEARCH_ROUTE = MJPlayerScreens.SEARCH_SCREEN
    const val SETTINGS_ROUTE = MJPlayerScreens.SETTINGS_SCREEN
}
