package com.gregkluska.minesweeper.util

import com.gregkluska.minesweeper.R
import com.gregkluska.minesweeper.core.DialogState
import com.gregkluska.minesweeper.core.GameOverDialog

fun DialogState.GameOver.win(
    time: Long,
    highScore: Long?
): GameOverDialog = GameOverDialog(
    icon = R.drawable.win_emoji,
    time = time,
    highScore = highScore
)

fun DialogState.GameOver.lose(
    highScore: Long?
): GameOverDialog = GameOverDialog(
    icon = R.drawable.lose_emoji,
    time = null,
    highScore = highScore
)