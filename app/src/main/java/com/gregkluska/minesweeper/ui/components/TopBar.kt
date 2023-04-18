package com.gregkluska.minesweeper.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import com.gregkluska.minesweeper.ui.theme.MinesweeperTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onMenuClick: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
            }
        },
        title = {
            Text(
                text = "Minesweeper" // This will be a timer
            )
        }
    )

}

@Preview(wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE)
@Preview(wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE)
@Preview(wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE)
@Preview(wallpaper = Wallpapers.YELLOW_DOMINATED_EXAMPLE)
@Composable
fun TopBarPreview() {
    MinesweeperTheme {
        TopBar()
    }
}