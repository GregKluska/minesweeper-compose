package com.gregkluska.minesweeper.presentation.ui.homescreen

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import com.gregkluska.minesweeper.core.Screen

@Composable
fun HomeScreen(
    modifier: Modifier,
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
) {

    Box(
        modifier = modifier,
    ) {
        Button(
            onClick = { onEvent(HomeEvent.ButtonClick) }
        ) {
            Text(text = state.text)
        }
    }

}