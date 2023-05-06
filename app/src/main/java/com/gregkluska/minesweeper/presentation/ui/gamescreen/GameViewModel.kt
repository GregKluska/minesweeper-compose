package com.gregkluska.minesweeper.presentation.ui.gamescreen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.gregkluska.minesweeper.core.DialogState
import com.gregkluska.minesweeper.core.Minesweeper
import java.util.LinkedList
import java.util.Queue

data class GameState(
    val game: Minesweeper,
    val flagMode: Boolean,
    val dialogQueue: Queue<DialogState>
)

sealed interface GameEvent {
    data class Click(val row: Int, val col: Int) : GameEvent
    data class ShowGameOverDialog(val state: Minesweeper.State.GameOver) : GameEvent
    data class FlagMode(val enable: Boolean) : GameEvent
    object TryAgain : GameEvent
    object DismissDialog : GameEvent
}

class GameViewModel() : ViewModel() {

    private val viewModelState = mutableStateOf(
        GameState(
            game = Minesweeper(
                width = 10,
                height = 10,
                mines = 10,
            ),
            flagMode = false,
            dialogQueue = LinkedList()
        )
    )

    val gameState: State<GameState>
        get() = viewModelState

    fun handleEvent(event: GameEvent) {
        println("AppDebug: handleEvent($event)")
        when (event) {
            is GameEvent.Click -> handleClick(event.row, event.col)
            is GameEvent.ShowGameOverDialog -> addGameOverDialog(event.state)
            is GameEvent.FlagMode -> setFlagMode(event.enable)
            GameEvent.DismissDialog -> removeHeadDialog()
            GameEvent.TryAgain -> tryAgain()
        }

    }

    private fun setFlagMode(flagMode: Boolean) {
        viewModelState.value = viewModelState.value.copy(flagMode = flagMode)
    }

    private fun handleClick(row: Int, col: Int) {
        when (viewModelState.value.flagMode) {
            true -> (viewModelState.value.game::handleEvent)(
                Minesweeper.Event.ToggleFlag(x = col, y = row)
            )

            false -> (viewModelState.value.game::handleEvent)(
                Minesweeper.Event.Reveal(x = col, y = row)
            )
        }
    }

    private fun tryAgain() {
        viewModelState.value.game.handleEvent(Minesweeper.Event.NewGame)
        removeHeadDialog()
    }

    private fun addGameOverDialog(state: Minesweeper.State.GameOver) {
        val dialogQueue = viewModelState.value.dialogQueue

        val dialog = if (state is Minesweeper.State.Win) {
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