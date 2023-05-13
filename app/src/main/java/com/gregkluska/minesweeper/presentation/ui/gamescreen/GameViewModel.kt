package com.gregkluska.minesweeper.presentation.ui.gamescreen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gregkluska.minesweeper.core.DialogState
import com.gregkluska.minesweeper.game.Minesweeper
import com.gregkluska.minesweeper.presentation.animation.Animatable
import com.gregkluska.minesweeper.util.DialogQueue
import com.gregkluska.minesweeper.util.lose
import com.gregkluska.minesweeper.util.win
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

data class GameUiState(
    val game: Minesweeper,
    val flagMode: Boolean,
    val dialogQueue: DialogQueue,
)

sealed interface GameUiEvent {
    data class Click(val row: Int, val col: Int) : GameUiEvent
    data class FlagMode(val enable: Boolean) : GameUiEvent
    data class BackButtonPressed(val action: () -> Unit) : GameUiEvent
}

sealed interface GameUiEffect {
    object Vibrate : GameUiEffect
    object PlayWinSound : GameUiEffect
    object PlayLoseSound : GameUiEffect
}

class GameViewModel : ViewModel() {

    companion object {
        private val VibrateOnState = listOf(
            Minesweeper.GameState.Start::class,
            Minesweeper.GameState.Lose::class
        )
    }

    private val viewModelState = mutableStateOf(
        GameUiState(
            game = Minesweeper(
                width = 10,
                height = 10,
                mines = 10,
            ),
            flagMode = false,
            dialogQueue = DialogQueue()
        )
    )

    val state: State<GameUiState>
        get() = viewModelState

    private val viewModelEffect = MutableSharedFlow<GameUiEffect>(0)
    val effect: SharedFlow<GameUiEffect>
        get() = viewModelEffect

    val shakeOffset = Animatable(Offset.Zero)

    init {
        state.value.game.state.onEach { gameState ->
            if (gameState::class in VibrateOnState) {
                viewModelEffect.emit(GameUiEffect.Vibrate)
            }

            if (gameState is Minesweeper.GameState.GameOver) {
                when (gameState) {
                    is Minesweeper.GameState.Win -> {
                        viewModelEffect.emit(GameUiEffect.PlayWinSound)
                        viewModelState.value.dialogQueue.appendDialog(
                            state = DialogState.GameOver.win(
                                time = gameState.endTime - gameState.startTime,
                                highScore = null,
                                onDismiss = state.value.dialogQueue::removeHeadDialog,
                                onTryAgain = this::onTryAgain
                            )
                        )
                    }

                    is Minesweeper.GameState.Lose -> {
                        viewModelEffect.emit(GameUiEffect.PlayLoseSound)
                        viewModelState.value.dialogQueue.appendDialog(
                            state = DialogState.GameOver.lose(
                                highScore = null,
                                onDismiss = state.value.dialogQueue::removeHeadDialog,
                                onTryAgain = this::onTryAgain
                            )
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun handleEvent(event: GameUiEvent) {
        println("AppDebug: handleEvent($event)")
        when (event) {
            is GameUiEvent.Click -> handleClick(event.row, event.col)
            is GameUiEvent.FlagMode -> setFlagMode(event.enable)
            is GameUiEvent.BackButtonPressed -> onBackDialog(event)
        }
    }

    private fun onBackDialog(event: GameUiEvent.BackButtonPressed) {
        if(viewModelState.value.game.state.value !is Minesweeper.GameState.Start) {
            event.action()
        } else {
            viewModelState.value.dialogQueue.appendDialog(
                state = DialogState.AreYouSure(
                    onConfirm = {
                        viewModelState.value.dialogQueue.removeHeadDialog()
                        event.action()
                    },
                    onDismiss = viewModelState.value.dialogQueue::removeHeadDialog
                )
            )
        }
    }

    private fun setFlagMode(flagMode: Boolean) {
        viewModelState.value = viewModelState.value.copy(flagMode = flagMode)
    }

    private fun handleClick(row: Int, col: Int) {
        when (viewModelState.value.flagMode) {
            true -> viewModelState.value.game.toggleFlag(col, row)
            false -> viewModelState.value.game.reveal(col, row)
        }
    }

    private fun onTryAgain() {
        state.value.dialogQueue.removeHeadDialog()
        state.value.game.reset()
    }
}