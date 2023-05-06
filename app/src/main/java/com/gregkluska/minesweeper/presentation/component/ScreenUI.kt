package com.gregkluska.minesweeper.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import com.gregkluska.minesweeper.presentation.ui.gamescreen.GameEvent
import com.gregkluska.minesweeper.presentation.theme.MinesweeperTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenUI(
    flags: Int = 0,
    mines: Int = 10,
    flagMode: Boolean = true,
    setMode: (GameEvent.FlagMode) -> Unit = {},
    onMenuClick: () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {

    MinesweeperTheme {
        Scaffold(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            topBar = { TopBar(onMenuClick = onMenuClick) },
            bottomBar = {
                BottomBar(
                    flags = flags,
                    mines = mines,
                    flagMode = flagMode,
                    setMode = setMode
                )
            },
            floatingActionButton = {},
            content = content
        )
    }

}

@Preview(wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE)
@Composable
fun ScreenUIPreview() {
    ScreenUI {
        Box(
            modifier = Modifier.size(300.dp)
        )
    }
}