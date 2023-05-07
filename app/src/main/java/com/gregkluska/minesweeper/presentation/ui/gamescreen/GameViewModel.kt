package com.gregkluska.minesweeper.presentation.ui.gamescreen

import androidx.annotation.RawRes
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.gregkluska.minesweeper.core.DialogState
import com.gregkluska.minesweeper.core.Minesweeper
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import java.util.LinkedList
import java.util.Queue

data class GameUiState(
    val game: Minesweeper,
    val flagMode: Boolean,
    val dialogQueue: Queue<DialogState>
)

sealed interface GameUiEvent {
    data class Click(val row: Int, val col: Int) : GameUiEvent
    data class ShowGameOverDialog(val state: Minesweeper.GameState.GameOver) : GameUiEvent
    data class FlagMode(val enable: Boolean) : GameUiEvent
    data class PlaySound(@RawRes val sound: Int) : GameUiEvent
    object TryAgain : GameUiEvent
    object DismissDialog : GameUiEvent // TODO: do effects so it's all in viewmodel
    object Vibrate : GameUiEvent
}

sealed interface GameUiEffect {
    object Vibrate : GameUiEffect
    object PlayWinSound : GameUiEffect
    object PlayLoseSound : GameUiEffect
}

class GameViewModel : ViewModel() {

    private val viewModelState = mutableStateOf(
        GameUiState(
            game = Minesweeper(
                width = 10,
                height = 10,
                mines = 10,
            ),
            flagMode = false,
            dialogQueue = LinkedList()
        )
    )

    val state: State<GameUiState>
        get() = viewModelState

    private val viewModelEffect = MutableSharedFlow<GameUiEffect>(0)
    val effect: SharedFlow<GameUiEffect>
        get() = viewModelEffect

    init {

    }

    fun handleEvent(event: GameUiEvent) {
        println("AppDebug: handleEvent($event)")
        when (event) {
            is GameUiEvent.Click -> handleClick(event.row, event.col)
            is GameUiEvent.ShowGameOverDialog -> addGameOverDialog(event.state)
            is GameUiEvent.FlagMode -> setFlagMode(event.enable)
            GameUiEvent.DismissDialog -> removeHeadDialog()
            GameUiEvent.TryAgain -> tryAgain()
            else -> {}
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

    private fun tryAgain() {
        viewModelState.value.game.reset()
        removeHeadDialog()
    }

    private fun addGameOverDialog(state: Minesweeper.GameState.GameOver) {
        val dialogQueue = viewModelState.value.dialogQueue

        val dialog = if (state is Minesweeper.GameState.Win) {
            DialogState.GameWon(
                time = state.time.toInt(), highScore = null
            )
        } else {
            DialogState.GameLost(
                highScore = null
            )
        }
        dialogQueue.add(dialog)

        // Force recomposition
        viewModelState.value = viewModelState.value.copy(dialogQueue = LinkedList())
        viewModelState.value = viewModelState.value.copy(dialogQueue = dialogQueue)
    }

    private fun removeHeadDialog() {
        println("AppDebug: removeHeadDialog called")
        val dialogQueue = viewModelState.value.dialogQueue
        dialogQueue.poll()

        //Force recomposition
        viewModelState.value = viewModelState.value.copy(dialogQueue = LinkedList())
        viewModelState.value = viewModelState.value.copy(dialogQueue = dialogQueue)
        println("AppDebug: queue " + viewModelState.value.dialogQueue)
    }
}