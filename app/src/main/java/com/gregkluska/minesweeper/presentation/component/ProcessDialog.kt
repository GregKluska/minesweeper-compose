package com.gregkluska.minesweeper.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.gregkluska.minesweeper.core.DialogState

@Composable
fun ProcessDialog(state: DialogState) {
    when (state) {
        is DialogState.GameOver -> {
            GameOverDialog(
                icon = painterResource(id = state.icon),
                time = state.time,
                highScore = state.highScore,
                onDismiss = state.onDismiss,
                onTryAgain = state.onTryAgain,
            )
        }
    }
}