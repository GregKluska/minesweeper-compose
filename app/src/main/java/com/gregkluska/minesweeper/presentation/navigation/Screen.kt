package com.gregkluska.minesweeper.presentation.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(val route: String, val arguments: List<NamedNavArgument>) {

    object Home : Screen(
        route = "home",
        arguments = listOf()
    )

    object Game : Screen(
        route = "game",
        arguments = listOf(
            navArgument("width") { type = NavType.IntType },
            navArgument("height") { type = NavType.IntType },
            navArgument("mines") { type = NavType.IntType },
        )
    )
}
