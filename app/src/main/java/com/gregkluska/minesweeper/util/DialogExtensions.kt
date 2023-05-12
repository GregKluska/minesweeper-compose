package com.gregkluska.minesweeper.util

import com.gregkluska.minesweeper.R
import com.gregkluska.minesweeper.core.DialogState

fun DialogState.GameOver.Companion.win(
    time: Long,
    highScore: Long?,
    onDismiss: () -> Unit,
    onTryAgain: () -> Unit
): DialogState.GameOver = DialogState.GameOver(
    icon = R.drawable.win_emoji,
    time = time,
    highScore = highScore,
    onDismiss = onDismiss,
    onTryAgain = onTryAgain
)

fun DialogState.GameOver.Companion.lose(
    highScore: Long?,
    onDismiss: () -> Unit,
    onTryAgain: () -> Unit
): DialogState.GameOver = DialogState.GameOver(
    icon = R.drawable.lose_emoji,
    time = null,
    highScore = highScore,
    onDismiss = onDismiss,
    onTryAgain = onTryAgain
)