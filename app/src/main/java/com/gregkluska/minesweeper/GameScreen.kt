package com.gregkluska.minesweeper

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import com.gregkluska.minesweeper.ui.components.Board
import com.gregkluska.minesweeper.ui.components.ScreenUI

data class GameScreenState(
    val fields: List<List<Field>>
)

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    fields: List<List<Field>>,
    onClick: (row: Int, col: Int) -> Unit,
) {
    Surface(
        shape = MaterialTheme.shapes.large, // TODO: Compare with xLarge
        modifier = Modifier
            .padding(16.dp)
            .then(modifier)
    ) {
        Board(
            modifier = Modifier,
            fields = fields,
            onClick = onClick
        )
    }
}

@Preview()
@Preview(device = Devices.TABLET)
@Preview(wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE)
@Preview(wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE)
@Preview(wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE)
@Preview(wallpaper = Wallpapers.YELLOW_DOMINATED_EXAMPLE)
@Composable
fun GameScreenPreview() {
    val state = GameScreenState(
        fields = List(10) { List(10) { Field() } }
    )

    ScreenUI { paddingValues ->
        GameScreen(
            modifier = Modifier
                .padding(paddingValues),
//            state = state,
            fields = state.fields,
            onClick = { _, _ -> }
        )
    }
}