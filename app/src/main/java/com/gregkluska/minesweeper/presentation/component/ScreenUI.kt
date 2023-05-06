package com.gregkluska.minesweeper.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.gregkluska.minesweeper.presentation.theme.MinesweeperTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenUI(
    content: @Composable (PaddingValues) -> Unit,
) {
    MinesweeperTheme {
        Scaffold(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            floatingActionButton = {},
            content = content
        )
    }
}