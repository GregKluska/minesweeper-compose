package com.gregkluska.minesweeper.core

sealed class Screen(val route: String) {

    object Home: Screen("home")
    object Game: Screen("game")

}
