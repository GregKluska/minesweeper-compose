package com.gregkluska.minesweeper.game

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class Minesweeper(
    private val width: Int,
    private val height: Int,
    val mines: Int,
) {

    sealed interface GameState {
        sealed interface GameOver
        object Initial : GameState
        data class Start(val startTime: Long) : GameState
        data class Win(val time: Long) : GameState, GameOver
        object Lose : GameState, GameOver
    }

    private val _boardState = List(height) { List(width) { MutableStateFlow(Field()) } }
    val boardState: List<List<StateFlow<Field>>>
        get() = _boardState

    private val _state = MutableStateFlow<GameState>(GameState.Initial)
    val state: StateFlow<GameState>
        get() = _state

    private val _flags = MutableStateFlow(0)
    val flags: StateFlow<Int>
        get() = _flags

    private val minesCoords = mutableListOf<Pair<Int, Int>>()
    private var revealed: Int = 0

    /**
     * Initialise the board. If both [cx] and [cy] are provided, the mine won't be spawned there
     */
    private fun initialise(cx: Int? = null, cy: Int? = null) {
        var mineShuffle = List(width * height) { if (it < mines) -1 else 0 }.shuffled()

        val cIdx: Int? = if (cx != null && cy != null) {
            width * cy + cx
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
            _boardState.updateField(x, y) { it.copy(value = Field.MINE) }
            _boardState.updateAdjacentMines(x, y)
            minesCoords.add(x to y)
        }
        _state.value = GameState.Start(startTime = System.currentTimeMillis())
    }

    fun reset() {
        for (r in _boardState.indices) {
            for (c in _boardState[0].indices) {
                _boardState[r][c].value = Field()
            }
        }
        minesCoords.clear()
        _flags.value = 0
        revealed = 0
        _state.value = GameState.Initial
    }

    fun reveal(x: Int, y: Int) {
        println("State: ${state.value}")
        if (_state.value is GameState.Initial) initialise(x, y)
        if (_state.value is GameState.GameOver) return

        val field = _boardState[y][x].value

        if (field.isRevealed || field.isFlagged) return
        if (field.value == -1) {
            _boardState.updateField(x, y) { it.copy(value = Field.DETONATED_BY_PLAYER) }
            for (mc in minesCoords) {
                val (mx, my) = mc
                if (mc.first == x && mc.second == y) continue
                _boardState.updateField(mx, my) { it.copy(value = Field.DETONATED_BY_MINE) }
            }
            _state.value = GameState.Lose
            return
        }

        _boardState.updateField(x, y) { it.copy(isRevealed = true) }
        revealed++

        if (revealed == (width * height) - mines) {
            val startTime = (_state.value as GameState.Start).startTime // TODO: Safe check
            _state.value = GameState.Win((System.currentTimeMillis() - startTime) / 1000)
        }

        // Reveal all the fields around, because there's no mine there
        if (field.value == 0) {
            for (zx in -1..1) {
                for (zy in -1..1) {
                    if (zx == 0 && zy == 0) continue
                    if (y + zy in 0 until height && x + zx in 0 until width) {
                        reveal(x + zx, y + zy)
                    }
                }
            }
        }
    }

    fun toggleFlag(x: Int, y: Int) {
        if (_state.value is GameState.GameOver) return

        val field = _boardState[y][x].value
        if (field.isRevealed) return

        val isFlagged = field.isFlagged

        when {
            isFlagged -> {
                _boardState.updateField(x, y) { it.copy(isFlagged = false) }
                _flags.value = _flags.value - 1
            }

            _flags.value < mines -> {
                _boardState.updateField(x, y) { it.copy(isFlagged = true) }
                _flags.value = _flags.value + 1
            }

            else -> {}
        }
    }

    /**
     * Update fields that are around the mine.
     */
    private fun List<List<MutableStateFlow<Field>>>.updateAdjacentMines(x: Int, y: Int) {
        for (fx in -1..1) {
            for (fy in -1..1) {
                if (fx == 0 && fy == 0) continue
                if (y + fy in 0 until height && x + fx in 0 until width && this[y + fy][x + fx].value.value > -1) {
                    val field = this[y + fy][x + fx].value
                    this[y + fy][x + fx].value = field.copy(value = field.value + 1)
                }
            }
        }
    }

    /**
     * Update fields that are around the mine.
     */
    private fun List<List<MutableStateFlow<Field>>>.updateField(
        x: Int,
        y: Int,
        block: (Field) -> Field
    ) {
        this[y][x].value = block(this[y][x].value)
    }

//    private suspend fun updateField(x: Int, y: Int, block: (Field) -> Field) {
//        val board = boardState.value
//        board[y][x] = block(board[y][x])
//        _boardState.emit(board)
//    }

}