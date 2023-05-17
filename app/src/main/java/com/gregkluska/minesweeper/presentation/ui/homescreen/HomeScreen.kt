package com.gregkluska.minesweeper.presentation.ui.homescreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gregkluska.minesweeper.R

@Composable
fun HomeScreen(
    modifier: Modifier,
    onEvent: (HomeUiEvent) -> Unit,
) {

    Column(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.minesweeper),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
        }

        Column(
            modifier = Modifier.padding(16.dp)
        ){
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onEvent(HomeUiEvent.GameStart(
                    width = 10, height = 10, mines = 10

                )) }
            ) {
                Text(text = "Beginner")
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onEvent(HomeUiEvent.GameStart(
                    width = 16, height = 16, mines = 40

                )) }
            ) {
                Text(text = "Intermediate")
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onEvent(HomeUiEvent.GameStart(
                    width = 16, height = 30, mines = 99

                )) }
            ) {
                Text(text = "Expert")
            }
        }

    }

}