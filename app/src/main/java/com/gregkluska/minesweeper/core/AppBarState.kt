package com.gregkluska.minesweeper.core

sealed interface AppBarState {

    object Game: AppBarState

    object Settings: AppBarState

}