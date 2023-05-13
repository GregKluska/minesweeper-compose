package com.gregkluska.minesweeper.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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

        is DialogState.AreYouSure -> {
            AreYouSureDialog(
                onDismiss = state.onDismiss,
                onConfirm = state.onConfirm,
                title = stringResource(id = state.title),
                text = stringResource(id = state.text)
            )
        }
    }
}