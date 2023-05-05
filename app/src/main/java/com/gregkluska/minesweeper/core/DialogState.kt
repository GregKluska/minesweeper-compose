package com.gregkluska.minesweeper.core

import androidx.annotation.DrawableRes
import com.gregkluska.minesweeper.R

sealed class GameDialogState(
    @DrawableRes val icon: Int,
    open val time: Int?,
    open val highScore: Int?
) : DialogState

sealed interface DialogState {

    data class GameWon(
        override val time: Int,
        override val highScore: Int?,
    ) : DialogState,
        GameDialogState(icon = R.drawable.win_emoji, time = time, highScore = highScore)

    data class GameLost(
        override val highScore: Int?,
    ) : DialogState,
        GameDialogState(icon = R.drawable.lose_emoji, time = null, highScore = highScore)

}
