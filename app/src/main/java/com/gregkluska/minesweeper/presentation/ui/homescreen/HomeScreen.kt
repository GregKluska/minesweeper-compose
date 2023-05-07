package com.gregkluska.minesweeper.presentation.ui.homescreen

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen(
    modifier: Modifier,
    state: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
) {

    Box(
        modifier = modifier,
    ) {
        Button(
            onClick = { onEvent(HomeUiEvent.ButtonClick) }
        ) {
            Text(text = state.text)
        }
    }

}