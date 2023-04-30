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

class GameViewModel() : ViewModel() {

    private val viewModelState = mutableStateOf(
        GameState(
            game = Minesweeper(
                width = 10,
                height = 10,
                mines = 10,
                onGameEvent = this::onGameEvent
            ),
            flagMode = false,
            dialogQueue = LinkedList()
        )
    )

    val state: State<GameState>
        get() = viewModelState

    private fun onGameEvent(event: GameEvent) {
        when (event) {
            GameEvent.GameLose -> TODO()
            GameEvent.GameWin -> TODO()
        }
    }

    fun setFlagMode(flagMode: Boolean) {
        println("appdebug: setflagmode to $flagMode")
        viewModelState.value = viewModelState.value.copy(flagMode = flagMode)
        println(viewModelState.value)
        println(state.value)
    }

    fun onClick(row: Int, col: Int) {
        when (viewModelState.value.flagMode) {
            true -> (viewModelState.value.game::handleEvent)(UserEvent.ToggleFlag(x = col, y = row))
            false -> (viewModelState.value.game::handleEvent)(UserEvent.Reveal(x = col, y = row))
        }
    }

}