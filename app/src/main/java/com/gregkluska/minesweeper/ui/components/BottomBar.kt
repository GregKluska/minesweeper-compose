package com.gregkluska.minesweeper.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.LocationSearching
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import com.gregkluska.minesweeper.ui.theme.MinesweeperTheme

@Composable
fun BottomBar() {
    NavigationBar(
        containerColor = Color.Transparent
    )
    {
        NavigationBarItem(
            icon = { Icon(Icons.Default.LocationSearching, contentDescription = "Reveal") },
            selected = true,
            onClick = { }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Flag, contentDescription = "Flag") },
            selected = false,
            onClick = { }
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