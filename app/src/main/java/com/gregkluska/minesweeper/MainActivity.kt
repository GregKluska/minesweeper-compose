package com.gregkluska.minesweeper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gregkluska.minesweeper.ui.components.ScreenUI
import com.gregkluska.minesweeper.ui.theme.MinesweeperTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val game = remember { Minesweeper(10, 10, 10) }
            val board = game.board
            ScreenUI { paddingValues ->
                GameScreen(
                    modifier = Modifier.padding(paddingValues = paddingValues),
                    fields = board.map { it.map { it.value } },
                    onClick = { row, col ->
                        game.reveal(col, row)
                    })
            }
        }
    }
}

@Composable
fun Cell(
    value: String,
    isRevealed: Boolean,
    onClick: () -> Unit
) {
    val bg = if (!isRevealed) Modifier.background(Color.Gray) else Modifier

    Box(
        modifier = Modifier
            .size(32.dp)
            .border(1.dp, Color.Red)
            .clickable(onClick = onClick)
            .then(bg)
    ) {
        if (isRevealed) {
            Text(text = value)
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