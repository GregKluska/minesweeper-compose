package com.gregkluska.minesweeper.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import com.gregkluska.minesweeper.ui.theme.MinesweeperTheme

@Composable
fun SquareField(
    modifier: Modifier = Modifier,
    light: Boolean = false,
    rev: Boolean = false
) {
    val minSize = 38.dp

    Box(
        modifier = Modifier
            .aspectRatio(1F)
            .background(
                if (light) {
                    if (rev) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                    }
                } else {
                    if (rev) {
                        MaterialTheme.colorScheme.inversePrimary
                    } else {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                    }
                }
            )
            .widthIn(min = minSize)
            .then(modifier)
    )
}

@Preview(wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE)
@Preview(wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE)
@Preview(wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE)
@Preview(wallpaper = Wallpapers.YELLOW_DOMINATED_EXAMPLE)
@Composable
fun SquareFieldPreview() {
    MinesweeperTheme {
        Row {
            for (i in 0 until 10) {
                SquareField(
                    light = i.mod(2) == 0
                )
            }
        }
    }
}