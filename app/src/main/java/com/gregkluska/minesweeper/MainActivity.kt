package com.gregkluska.minesweeper

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import com.gregkluska.minesweeper.core.GameDialogState
import com.gregkluska.minesweeper.ui.animation.Animatable
import com.gregkluska.minesweeper.ui.animation.shakeKeyframes
import com.gregkluska.minesweeper.ui.component.ScreenUI
import com.gregkluska.minesweeper.ui.component.dialog.GameOverDialog

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels()

    // Get Vibrator service
    private val vibrator: Vibrator by lazy {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state = viewModel.gameState
            val game = state.value.game
            val flagMode = state.value.flagMode
            val board = game.board

            ScreenUI(
                flags = game.flags.value,
                mines = game.mines,
                flagMode = flagMode,
                setMode = viewModel::handleEvent,
                onMenuClick = { viewModel.handleEvent(GameEvent.TryAgain) }
            ) { paddingValues ->
                state.value.dialogQueue.peek()?.let { dialog ->
                    when (dialog) {
                        is GameDialogState -> {
                            GameOverDialog(
                                icon = painterResource(id = dialog.icon),
                                time = dialog.time,
                                highScore = dialog.highScore,
                                onEvent = viewModel::handleEvent
                            )
                        }
                    }
                }

                val shakeOffset = remember { Animatable(Offset.Zero) }

                LaunchedEffect(game.state.value) {
                    game.state.value.let { gameState ->
                        if(gameState in setOf(Minesweeper.State::Start, Minesweeper.State.Lose)) {
                            vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE))
                            shakeOffset.animateTo(Offset.Zero, shakeKeyframes)
                        }

                        if(gameState is Minesweeper.State.GameOver) {
                            when(gameState) {
                                Minesweeper.State.Lose -> {
                                    val mp = MediaPlayer.create(this@MainActivity, R.raw.lose)
                                    mp.start()
                                }
                                is Minesweeper.State.Win -> {
                                    val mp = MediaPlayer.create(this@MainActivity, R.raw.win)
                                    mp.start()
                                }
                            }
                            (viewModel::handleEvent)(GameEvent.ShowGameOverDialog(gameState))
                        }
                    }
                }

                GameScreen(
                    modifier = Modifier
                        .padding(paddingValues = paddingValues)
                        .offset(
                            x = with(LocalDensity.current) { shakeOffset.value.x.toDp() },
                            y = with(LocalDensity.current) { shakeOffset.value.y.toDp() },
                        ),
                    fields = board.map { it.map { it.value } },
                    onClick = viewModel::handleEvent
                )
            }
        }
    }
}