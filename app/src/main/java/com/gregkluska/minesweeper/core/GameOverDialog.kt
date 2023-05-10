package com.gregkluska.minesweeper.core

import androidx.annotation.DrawableRes
import com.gregkluska.minesweeper.R

data class GameOverDialog(
    @DrawableRes val icon: Int,
    val time: Long?,
    val highScore: Long?
) {
    companion object {
        fun win(time: Long, highScore: Long?): GameOverDialog {
            return GameOverDialog(
                icon = R.drawable.win_emoji,
                time = time,
                highScore = highScore
            )
        }

        fun lose(highScore: Long?): GameOverDialog {
            return GameOverDialog(
                icon = R.drawable.lose_emoji,
                time = null,
                highScore = highScore
            )
        }
    }
}
