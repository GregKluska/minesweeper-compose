package com.gregkluska.minesweeper

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.gregkluska.minesweeper.Field.Companion.DETONATED_BY_MINE
import com.gregkluska.minesweeper.Field.Companion.DETONATED_BY_PLAYER
import com.gregkluska.minesweeper.Field.Companion.MINE

sealed class UserEvent {
    data class Reveal(val x: Int, val y: Int) : UserEvent()
    data class ToggleFlag(val x: Int, val y: Int) : UserEvent()
    object NewGame : UserEvent()
}

sealed class GameEvent {
    object GameLose : GameEvent()
    object GameWin : GameEvent()
}

/**
 * Field date class
 *
 * The param [value] means:
 * - if between 0 and 8 - shows how many mines is next to the field
 * - if -1 - the field has a mine
 * - if -2 - the mine was detonated by a player
 * - if -3 - the mine was detonated by another mine
 */
data class Field(
    val value: Int = 0,
    val isRevealed: Boolean = false,
    val isFlagged: Boolean = false,
) {
    companion object {
        const val MINE = -1
        const val DETONATED_BY_PLAYER = -2
        const val DETONATED_BY_MINE = -3
    }
}

class Minesweeper(
    private val width: Int,
    private val height: Int,
    val mines: Int,
    private val onGameEvent: (GameEvent) -> Unit
) {

    /**
     * TODO: Docs
     */
    val board: List<List<State<Field>>>
        get() = _board

    /**
     * TODO: Docs
     */
    val flags: State<Int>
        get() = _flags


    private val _board = List(height) { List(width) { mutableStateOf(Field()) } }
    private val _flags = mutableStateOf(0)
    private val minesCoords = mutableListOf<Pair<Int, Int>>()
    private var initialised: Boolean = false
    private var gameOver = false
    private var revealed = 0

    init {
        require(width > 0 && height > 0 && mines > 0 && width * height > mines)
    }

    fun handleEvent(userEvent: UserEvent) {
        when (userEvent) {
            is UserEvent.Reveal -> {
                reveal(
                    x = userEvent.x.coerceIn(0 until width),
                    y = userEvent.y.coerceIn(0 until height)
                )
            }

            is UserEvent.ToggleFlag -> {
                toggleFlag(
                    x = userEvent.x.coerceIn(0 until width),
                    y = userEvent.y.coerceIn(0 until height)
                )
            }

            UserEvent.NewGame -> reset() // Test it
        }
    }

    /**
     * Initialise the board. If both [cx] and [cy] are provided, the mine won't be spawned there
     */
    private fun initialise(
        cx: Int? = null,
        cy: Int? = null
    ) {
//        reset()

        var mineShuffle = List(width * height) { if (it < mines) -1 else 0 }.shuffled()

        val cIdx: Int? = if (cx != null && cy != null) {
            height * cy + cx
        } else {
            null
        }

        // If provided coords is mine, then reshuffle until it isn't
        cIdx?.let {
            while (mineShuffle[it] == -1) {
                mineShuffle = mineShuffle.shuffled()
            }
        }

        for (i in mineShuffle.indices) {
            if (mineShuffle[i] == 0) continue
            val y = i.div(width)
            val x = i - (y * width)
            _board[y][x].value = _board[y][x].value.copy(value = MINE)
            minesCoords.add(x to y)
            updateAdjacentMines(x, y)
        }
        minesCoords.shuffle()

        initialised = true
    }

    private fun toggleFlag(x: Int, y: Int) {
        if (gameOver) return
        val field = _board[y][x].value
        if (field.isRevealed) return

        val isFlagged = field.isFlagged

        when {
            isFlagged -> {
                _board[y][x].value = field.copy(isFlagged = false)
                _flags.value = _flags.value - 1
            }

            !isFlagged && _flags.value < mines -> {
                _board[y][x].value = field.copy(isFlagged = true)
                _flags.value = _flags.value + 1
            }

            else -> {}
        }
    }

    private fun reveal(x: Int, y: Int) {
        if (!initialised) initialise(x, y)
        if (gameOver) return

        val field = _board[y][x].value
        if (field.isRevealed || field.isFlagged) return
        if (field.value == -1) {
            _board[y][x].value = field.copy(value = DETONATED_BY_PLAYER)
            for (mc in minesCoords) {
                if (mc.first == x && mc.second == y) continue
                val mine = _board[mc.second][mc.first].value
                _board[mc.second][mc.first].value = mine.copy(value = DETONATED_BY_MINE)
            }
            gameOver = true
            onGameEvent(GameEvent.GameLose)
            return
        }

        _board[y][x].value = field.copy(isRevealed = true)
        revealed++
        println("AppDebug: revealed $revealed")

        if (revealed == (width * height) - mines) {
            gameOver = true
            onGameEvent(GameEvent.GameWin)
        }

        // Reveal all the fields around, because there's no mine there
        if (field.value == 0) {
            for (zx in -1..1) {
                for (zy in -1..1) {
                    if (zx == 0 && zy == 0) continue
                    if (y + zy in 0 until height &&
                        x + zx in 0 until width
                    ) {
                        reveal(x + zx, y + zy)
                    }
                }
            }
        }
    }

    /**
     * Update fields that are around the mine.
     */
    private fun updateAdjacentMines(x: Int, y: Int) {
        for (fx in -1..1) {
            for (fy in -1..1) {
                if (fx == 0 && fy == 0) continue
                if (y + fy in 0 until height &&
                    x + fx in 0 until width &&
                    _board[y + fy][x + fx].value.value > -1
                ) {
                    val field = _board[y + fy][x + fx].value
                    _board[y + fy][x + fx].value = field.copy(value = field.value + 1)
                }
            }
        }
    }

    private fun reset() {
        for (r in _board.indices) {
            for (c in _board[0].indices) {
                _board[r][c].value = Field()
            }
        }
        minesCoords.clear()
        _flags.value = 0
        revealed = 0
        initialised = false
        gameOver = false
    }
}
