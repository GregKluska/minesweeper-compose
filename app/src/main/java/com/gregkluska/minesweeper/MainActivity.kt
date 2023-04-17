package com.gregkluska.minesweeper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.gregkluska.minesweeper.ui.components.ScreenUI
import com.gregkluska.minesweeper.ui.theme.MinesweeperTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val game = remember { Minesweeper(10, 10, 15) }
            val board = game.board
            ScreenUI(
                flags = game.flags,
                mines = game.mines,
                flagMode = game.flagMode,
                setFlagMode = { game.flagMode = it }
            ) { paddingValues ->
                GameScreen(
                    modifier = Modifier.padding(paddingValues = paddingValues),
                    fields = board.map { it.map { it.value } },
                    onClick = game::click
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