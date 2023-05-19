package com.gregkluska.minesweeper.presentation.ui.homescreen

import android.util.Size
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gregkluska.minesweeper.R
import com.gregkluska.minesweeper.presentation.component.LevelButton

@Composable
fun HomeScreen(
    modifier: Modifier,
    onEvent: (HomeUiEvent) -> Unit,
) {

    Column(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.minesweeper),
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.Center
            )
            LevelButton(
                text = "Beginner",
                gameSize = Size(10, 10),
                gameMines = 10,
                onClick = { w, h, m ->
                    onEvent(HomeUiEvent.GameStart(width = w, height = h, mines = m))
                }
            )

            LevelButton(
                text = "Intermediate",
                gameSize = Size(16, 16),
                gameMines = 40,
                onClick = { w, h, m ->
                    onEvent(HomeUiEvent.GameStart(width = w, height = h, mines = m))
                }
            )

            LevelButton(
                text = "Expert",
                gameSize = Size(16, 30),
                gameMines = 99,
                onClick = { w, h, m ->
                    onEvent(HomeUiEvent.GameStart(width = w, height = h, mines = m))
                }
            )
        }

    }

}