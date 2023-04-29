package com.gregkluska.minesweeper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.gregkluska.minesweeper.ui.components.ScreenUI
import com.gregkluska.minesweeper.ui.theme.MinesweeperTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val game = remember {
                Minesweeper(
                    width = 40,
                    height = 10,
                    mines = 10,
                    onGameEvent = {
                        when(it) {
                            GameEvent.GameLose -> println("AppDebug: Lost")
                            GameEvent.GameWin -> println("AppDebug: Win")
                        }
                    }
                )
            }
            val board = game.board
            val flagMode = remember { mutableStateOf(false) }
            ScreenUI(
                flags = game.flags.value,
                mines = game.mines,
                flagMode = flagMode.value,
                setFlagMode = { flagMode.value = it },
                onMenuClick = { game.handleEvent(UserEvent.NewGame) }
            ) { paddingValues ->
                GameScreen(
                    modifier = Modifier.padding(paddingValues = paddingValues),
                    fields = board.map { it.map { it.value } },
                    onClick = { row, col ->
                        when(flagMode.value) {
                            true -> (game::handleEvent)(UserEvent.ToggleFlag(x = col, y = row))
                            false -> (game::handleEvent)(UserEvent.Reveal(x = col, y = row))
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MinesweeperTheme {
        Greeting("Android")
    }
}