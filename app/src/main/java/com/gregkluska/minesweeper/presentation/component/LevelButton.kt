package com.gregkluska.minesweeper.presentation.component

import android.util.Size
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LevelButton(
    text: String,
    gameSize: Size,
    gameMines: Int,
    onClick: (Int, Int, Int) -> Unit,
) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onClick(gameSize.width, gameSize.height, gameMines) }
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = text)
            Text(
                text = "Board: ${gameSize.width}x${gameSize.height}, Mines: $gameMines",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}