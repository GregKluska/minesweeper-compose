package com.gregkluska.minesweeper.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.gregkluska.minesweeper.core.AppBarState
import com.gregkluska.minesweeper.presentation.theme.MinesweeperTheme

/**
 * Main App component
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MinesweeperApp(
    appBarState: AppBarState,
    content: @Composable (PaddingValues) -> Unit,
) {
    MinesweeperTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
            topBar = { AppBar(appBarState) },
            floatingActionButton = {},
            content = content
        )
    }
}