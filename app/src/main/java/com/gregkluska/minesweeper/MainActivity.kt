package com.gregkluska.minesweeper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import com.gregkluska.minesweeper.ui.component.ScreenUI

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state = viewModel.state
            val game = state.value.game
            val flagMode = state.value.flagMode
            val board = game.board

            ScreenUI(
                flags = game.flags.value,
                mines = game.mines,
                flagMode = flagMode,
                setFlagMode = viewModel::setFlagMode,
                onMenuClick = { game.handleEvent(UserEvent.NewGame) }
            ) { paddingValues ->
                GameScreen(
                    modifier = Modifier.padding(paddingValues = paddingValues),
                    fields = board.map { it.map { it.value } },
                    onClick = viewModel::onClick
                )
            }
        }
    }
}