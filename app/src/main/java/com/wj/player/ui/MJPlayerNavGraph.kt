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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.wj.player.ui.screen.player.PlayerScreen
import com.wj.player.ui.screen.settings.SettingsScreen
import com.wj.player.ui.screen.videolist.VideoListScreen
import kotlinx.coroutines.CoroutineScope

@Composable
fun MJNaviGraph(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    startDestination: String = MJPlayerDestinations.VIDEO_LIST_ROUTE,
) {

    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: startDestination

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(MJPlayerDestinations.VIDEO_LIST_ROUTE) {
            VideoListScreen(
                onNavigateToSearch = {
                    navController.navigate(MJPlayerDestinations.SEARCH_ROUTE)
                },
                onNavigateToSettings = { itemId ->
                    navController.navigate(MJPlayerDestinations.SETTINGS_ROUTE)
                },
                onVideoOnclick = {}
            )
        }

        composable(MJPlayerDestinations.SEARCH_ROUTE) { backStackEntry ->
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(MJPlayerDestinations.PLAYER_ROUTE) { backStackEntry ->
            // 获取参数
            val itemId = backStackEntry.arguments?.getString("itemId") ?: "0"

            PlayerScreen(
                itemId = itemId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(MJPlayerDestinations.SETTINGS_ROUTE) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

    }
}