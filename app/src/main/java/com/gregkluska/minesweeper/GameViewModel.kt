package com.gregkluska.minesweeper

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.gregkluska.minesweeper.core.DialogState
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
    data class FlagMode(val enable: Boolean): GameEvent
    object TryAgain : GameEvent
    object DismissDialog : GameEvent
}

class GameViewModel() : ViewModel() {

    private val viewModelGameState = mutableStateOf(
        GameState(
            game = Minesweeper(
                width = 20,
                height = 40,
                mines = 10,
            ),
            flagMode = false,
            dialogQueue = LinkedList()
        )
    )

    val gameState: State<GameState>
        get() = viewModelGameState

    fun handleEvent(event: GameEvent) {
        when (event) {
            is GameEvent.Click -> handleClick(event.row, event.col)
            is GameEvent.ShowGameOverDialog -> addGameOverDialog(event.state)
            is GameEvent.FlagMode -> setFlagMode(event.enable)
            GameEvent.DismissDialog -> removeHeadDialog()
            GameEvent.TryAgain -> tryAgain()
        }

    }

    private fun setFlagMode(flagMode: Boolean) {
        viewModelGameState.value = viewModelGameState.value.copy(flagMode = flagMode)
    }

    private fun handleClick(row: Int, col: Int) {
        when (viewModelGameState.value.flagMode) {
            true -> (viewModelGameState.value.game::handleEvent)(
                Minesweeper.Event.ToggleFlag(x = col, y = row)
            )

            false -> (viewModelGameState.value.game::handleEvent)(
                Minesweeper.Event.Reveal(x = col, y = row)
            )
        }
    }

    private fun addGameOverDialog(state: Minesweeper.State.GameOver) {
        val dialogQueue = viewModelGameState.value.dialogQueue

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
        viewModelGameState.value = viewModelGameState.value.copy(dialogQueue = LinkedList())
        viewModelGameState.value = viewModelGameState.value.copy(dialogQueue = dialogQueue)
    }

    private fun removeHeadDialog() {
        val dialogQueue = viewModelGameState.value.dialogQueue
        dialogQueue.poll()

        //Force recomposition
        viewModelGameState.value = viewModelGameState.value.copy(dialogQueue = LinkedList())
        viewModelGameState.value = viewModelGameState.value.copy(dialogQueue = dialogQueue)
    }

    private fun tryAgain() {
        viewModelGameState.value.game.handleEvent(Minesweeper.Event.NewGame)
        removeHeadDialog()
    }

}