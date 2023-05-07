package com.gregkluska.minesweeper.presentation.ui.gamescreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.gregkluska.minesweeper.R
import com.gregkluska.minesweeper.core.GameDialogState
import com.gregkluska.minesweeper.core.Minesweeper
import com.gregkluska.minesweeper.presentation.animation.Animatable
import com.gregkluska.minesweeper.presentation.animation.shakeKeyframes
import com.gregkluska.minesweeper.presentation.component.Board
import com.gregkluska.minesweeper.presentation.component.BottomBar
import com.gregkluska.minesweeper.presentation.component.GameOverDialog

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    state: GameUiState,
    onEvent: (GameUiEvent) -> Unit,
) {
    val game = state.game
    val fields = game.boardState
    val shakeOffset = remember { Animatable(Offset.Zero) }

    LaunchedEffect(game.state.collectAsState().value) {
        game.state.value.let { gameState ->
            if (gameState is Minesweeper.GameState.Start || gameState is Minesweeper.GameState.Lose) {
                onEvent(GameUiEvent.Vibrate)
                shakeOffset.animateTo(Offset.Zero, shakeKeyframes)
            }

            if (gameState is Minesweeper.GameState.GameOver) {
                when (gameState) {
                    Minesweeper.GameState.Lose -> onEvent(GameUiEvent.PlaySound(R.raw.lose))
                    is Minesweeper.GameState.Win -> onEvent(GameUiEvent.PlaySound(R.raw.win))
                }
                onEvent(GameUiEvent.ShowGameOverDialog(gameState))
            }
        }
    }


    // TODO: Refactor
    state.dialogQueue.firstOrNull()?.let { dialog ->
        when (dialog) {
            is GameDialogState -> {
                GameOverDialog(
                    icon = painterResource(id = dialog.icon),
                    time = dialog.time,
                    highScore = dialog.highScore,
                    onEvent = onEvent
                )
            }
        }
    }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column {
            Board(
                modifier = Modifier
                    .weight(1f)
                    .offset(
                        x = with(LocalDensity.current) { shakeOffset.value.x.toDp() },
                        y = with(LocalDensity.current) { shakeOffset.value.y.toDp() },
                    ),
                fields = fields.map { it.map { it.collectAsState().value } },
                onClick = onEvent
            )

            BottomBar(
                flags = game.flags.collectAsState().value,
                mines = game.mines,
                flagMode = state.flagMode,
                setMode = onEvent
            )
        }
    }
}