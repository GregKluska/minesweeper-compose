package com.gregkluska.minesweeper.core

import androidx.annotation.DrawableRes

sealed interface DialogState {

    data class GameOver(
        @DrawableRes val icon: Int,
        val time: Long?,
        val highScore: Long?
    ): DialogState

}