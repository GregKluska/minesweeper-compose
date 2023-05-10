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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.gregkluska.minesweeper.core.AppBarState
import com.gregkluska.minesweeper.util.toDuration
import kotlinx.coroutines.delay

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
    val elapsedTime = remember { mutableStateOf(0L) }

    LaunchedEffect(elapsedTime.value, state.startTime, state.endTime) {
        when {
            state.startTime == 0L -> {
                // Game not started
                elapsedTime.value = 0L
            }
            state.endTime != null -> {
                // Game over, show this value only
                elapsedTime.value = state.endTime - state.startTime
            }
            else -> {
                val diff = elapsedTime.value - (System.currentTimeMillis() - state.startTime)
                delay(1_000L - diff)
                elapsedTime.value = System.currentTimeMillis() - state.startTime
            }
        }
    }

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
                text = elapsedTime.value.toDuration()
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