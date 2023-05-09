package com.gregkluska.minesweeper.core

sealed interface AppBarState {

    /**
     * Don't show the app bar
     */
    object Hide : AppBarState

    /**
     * Empty Appbar (no elements, just for spacing)
     */
    object Empty : AppBarState

    data class Game(
        val onBack: () -> Unit,
        val startTime: Long,
        val onAction: () -> Unit
    ) : AppBarState

    object Settings : AppBarState

}