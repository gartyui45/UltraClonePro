package com.ultraclone.pro.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ultraclone.pro.ui.screens.*

object Routes {
    const val HOME = "home"; const val APP_SELECTOR = "app_selector"; const val CLONE_MANAGER = "clone_manager"
    const val CLONE_SETTINGS = "clone_settings/{cloneId}"; const val TERMINAL = "terminal/{cloneId}"
    fun cloneSettings(id: Long) = "clone_settings/$id"; fun terminal(id: Long) = "terminal/$id"
}

@Composable
fun NavGraph() {
    val nav = rememberNavController()
    NavHost(nav, Routes.HOME) {
        composable(Routes.HOME) { HomeScreen({ nav.navigate(Routes.APP_SELECTOR) }, { nav.navigate(Routes.CLONE_MANAGER) }) }
        composable(Routes.APP_SELECTOR) { AppSelectorScreen({ nav.popBackStack() }) }
        composable(Routes.CLONE_MANAGER) { CloneManagerScreen({ id -> nav.navigate(Routes.cloneSettings(id)) }, { nav.popBackStack() }) }
        composable(Routes.CLONE_SETTINGS, listOf(navArgument("cloneId") { type = NavType.LongType })) {
            val id = it.arguments?.getLong("cloneId") ?: return@composable
            CloneSettingsScreen(id, { nav.navigate(Routes.terminal(id)) }, { nav.popBackStack() })
        }
        composable(Routes.TERMINAL, listOf(navArgument("cloneId") { type = NavType.LongType })) {
            TerminalScreen(it.arguments?.getLong("cloneId") ?: return@composable, { nav.popBackStack() })
        }
    }
}
