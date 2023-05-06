package com.gregkluska.minesweeper.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gregkluska.minesweeper.presentation.ui.gamescreen.GameEvent
import com.gregkluska.minesweeper.R
import com.gregkluska.minesweeper.core.DialogState
import com.gregkluska.minesweeper.presentation.theme.MinesweeperTheme

@Composable
fun GameOverDialog(
    icon: Painter,
    time: Int? = null,
    highScore: Int? = null,
    onEvent: (GameEvent) -> Unit,
) {

    val timeString = time?.let { s ->
        "${s / 60}:${s % 60}"
    } ?: "-"

    val highScoreString = highScore?.let { s ->
        "${s / 60}:${s % 60}"
    } ?: "-"


    AlertDialog(
        onDismissRequest = { onEvent(GameEvent.DismissDialog) },
        icon = {
            Image(
                painter = icon,
                contentDescription = null
            )
        },
        tonalElevation = 32.dp,
        text = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(end = 4.dp),
                        imageVector = Icons.Default.Timer,
                        contentDescription = stringResource(R.string.time)
                    )
                    Text(
                        style = MaterialTheme.typography.titleLarge,
                        text = timeString
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(end = 4.dp),
                        imageVector = Icons.Default.Star,
                        contentDescription = stringResource(R.string.best_time)
                    )
                    Text(
                        style = MaterialTheme.typography.titleLarge,
                        text = highScoreString
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onEvent(GameEvent.TryAgain) }
            ) {
                Text(text = stringResource(R.string.play_again))
            }
        }
    )
}

@Preview
@Composable
private fun GameDialogPreviewWin() {
    val dialog = DialogState.GameWon(95, null)

    MinesweeperTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            GameOverDialog(
                icon = painterResource(id = dialog.icon),
                time = dialog.time,
                highScore = dialog.highScore,
                onEvent = {}
            )
        }
    }
}

@Preview
@Composable
private fun GameDialogPreviewLose() {
    val dialog = DialogState.GameLost(55)

    MinesweeperTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            GameOverDialog(
                icon = painterResource(id = dialog.icon),
                time = dialog.time,
                highScore = dialog.highScore,
                onEvent = {}
            )
        }
    }
}