/*
 * Copyright 2025 WuJia
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wj.player.ui

import android.net.wifi.hotspot2.pps.HomeSp


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