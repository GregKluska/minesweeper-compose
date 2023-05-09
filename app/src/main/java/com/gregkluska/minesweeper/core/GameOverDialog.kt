package com.gregkluska.minesweeper.core

import androidx.annotation.DrawableRes
import com.gregkluska.minesweeper.R

data class GameOverDialog(
    @DrawableRes val icon: Int,
    val time: Int?,
    val highScore: Int?
) {
    companion object {
        fun win(time: Int, highScore: Int?): GameOverDialog {
            return GameOverDialog(
                icon = R.drawable.win_emoji,
                time = highScore,
                highScore = highScore
            )
        }

        fun lose(highScore: Int?): GameOverDialog {
            return GameOverDialog(
                icon = R.drawable.lose_emoji,
                time = null,
                highScore = highScore
            )
        }
    }
}
