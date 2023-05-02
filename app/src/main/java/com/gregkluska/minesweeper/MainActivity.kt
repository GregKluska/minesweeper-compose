package com.gregkluska.minesweeper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.gregkluska.minesweeper.core.GameDialogState
import com.gregkluska.minesweeper.ui.component.ScreenUI
import com.gregkluska.minesweeper.ui.component.dialog.GameOverDialog

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
                state.value.dialogQueue.peek()?.let { dialog ->
                    when (dialog) {
                        is GameDialogState -> {
                            GameOverDialog(
                                icon = painterResource(id = dialog.icon),
                                time = dialog.time,
                                highScore = dialog.highScore,
                                onDismissRequest = viewModel::dismissDialog,
                                onTryAgainClick = viewModel::tryAgain
                            )
                        }
                    }
                }

                GameScreen(
                    modifier = Modifier.padding(paddingValues = paddingValues),
                    fields = board.map { it.map { it.value } },
                    onClick = viewModel::onClick
                )
            }
        }
    }
}