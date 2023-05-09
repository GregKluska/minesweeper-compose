package com.gregkluska.minesweeper.navigation

sealed class Screen(val route: String) {

    object Home: Screen("home")
    object Game: Screen("game")

}
