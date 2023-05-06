package com.gregkluska.minesweeper.presentation.ui.gamescreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gregkluska.minesweeper.presentation.component.Board

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    state: GameState,
    onEvent: (GameEvent) -> Unit,
) {
    Surface(
        modifier = Modifier
            .padding(16.dp)
            .then(modifier)
    ) {
        Board(
            modifier = Modifier,
            fields = state.game.board.map { it.map { it.value } },
            onClick = onEvent
        )
    }
}