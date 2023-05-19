package com.gregkluska.minesweeper.presentation.ui.gamescreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.gregkluska.minesweeper.presentation.component.Board
import com.gregkluska.minesweeper.presentation.component.BottomBar

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    state: GameUiState,
    shakeOffset: Offset,
    onEvent: (GameUiEvent) -> Unit,
) {
    val game = state.game
    val fields = game.boardState

    Surface(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        Column {
            Board(
                modifier = Modifier
                    .weight(1f)
                    .offset(
                        x = with(LocalDensity.current) { shakeOffset.x.toDp() },
                        y = with(LocalDensity.current) { shakeOffset.y.toDp() },
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