package com.gregkluska.minesweeper.ui.components

import androidx.annotation.DrawableRes
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
import com.gregkluska.minesweeper.R
import com.gregkluska.minesweeper.ui.components.GameDialogState.Companion.LoseDialog
import com.gregkluska.minesweeper.ui.components.GameDialogState.Companion.WinDialog
import com.gregkluska.minesweeper.ui.theme.MinesweeperTheme

data class GameDialogState(
    @DrawableRes val icon: Int,
    val time: Int? = null,
    val highScore: Int? = null
) {
    companion object {
        val WinDialog = GameDialogState(
            icon = R.drawable.win_emoji,
            time = 50,
            highScore = 10
        )

        val LoseDialog = GameDialogState(
            icon = R.drawable.lose_emoji,
            time = null,
            highScore = 50
        )
    }
}

@Composable
fun GameDialog(
    icon: Painter,
    time: Int? = null,
    highScore: Int? = null,
    onDismissRequest: () -> Unit,
    onTryAgainClick: () -> Unit,
) {

    val timeString = time?.let { s ->
        "${s / 60}:${s % 60}"
    } ?: "-"

    val highScoreString = highScore?.let { s ->
        "${s / 60}:${s % 60}"
    } ?: "-"


    AlertDialog(
        onDismissRequest = onDismissRequest,
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
                onClick = onTryAgainClick
            ) {
                Text(text = stringResource(R.string.play_again))
            }
        }
    )
}

@Preview
@Composable
private fun GameDialogPreviewWin() {
    val dialog = WinDialog

    MinesweeperTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            GameDialog(
                icon = painterResource(id = dialog.icon),
                time = dialog.time,
                highScore = dialog.highScore,
                onDismissRequest = {},
                onTryAgainClick = {})
        }
    }
}

@Preview
@Composable
private fun GameDialogPreviewLose() {
    val dialog = LoseDialog

    MinesweeperTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            GameDialog(
                icon = painterResource(id = dialog.icon),
                time = dialog.time,
                highScore = dialog.highScore,
                onDismissRequest = {},
                onTryAgainClick = {})
        }
    }
}