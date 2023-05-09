package com.gregkluska.minesweeper.presentation.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import com.gregkluska.minesweeper.core.AppBarState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    state: AppBarState
) {
    when (state) {
        is AppBarState.Game -> GameAppBar(state = state)
        is AppBarState.Settings -> TopAppBar(title = {})
        AppBarState.Empty -> TopAppBar(title = {})
        AppBarState.Hide -> {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GameAppBar(
    state: AppBarState.Game
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(
                onClick = state.onBack,
                content = {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            )
        },
        title = {
            Text(
                text = state.startTime.toString()
            )
        }
    )
}

//@Preview(wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE)
//@Preview(wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE)
//@Preview(wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE)
//@Preview(wallpaper = Wallpapers.YELLOW_DOMINATED_EXAMPLE)
//@Composable
//fun TopBarPreview() {
//    MinesweeperTheme {
//        AppBar()
//    }
//}