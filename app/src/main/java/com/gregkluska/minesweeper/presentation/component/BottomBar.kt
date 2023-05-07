package com.gregkluska.minesweeper.presentation.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.LocationSearching
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import com.gregkluska.minesweeper.presentation.ui.gamescreen.GameUiEvent
import com.gregkluska.minesweeper.presentation.theme.MinesweeperTheme

@Composable
fun BottomBar(
    flags: Int = 0,
    mines: Int = 10,
    flagMode: Boolean = true,
    setMode: (GameUiEvent.FlagMode) -> Unit = {},
) {
    NavigationBar(
        containerColor = Color.Transparent
    )
    {
        NavigationBarItem(
            icon = { Icon(Icons.Default.LocationSearching, contentDescription = "Reveal") },
            label = { Text(text = "Reveal") },
            selected = !flagMode,
            onClick = { setMode(GameUiEvent.FlagMode(false)) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Flag, contentDescription = "Flag") },
            label = { Text(text = "Flag ($flags/$mines)") },
            selected = flagMode,
            onClick = { setMode(GameUiEvent.FlagMode(true)) }
        )
    }
}

@Preview(wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE)
@Preview(wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE)
@Preview(wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE)
@Preview(wallpaper = Wallpapers.YELLOW_DOMINATED_EXAMPLE)
@Composable
fun BottomBarPreviewGreen() {
    MinesweeperTheme {
        BottomBar()
    }
}