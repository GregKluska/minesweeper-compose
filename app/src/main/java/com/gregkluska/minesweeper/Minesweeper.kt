package com.gregkluska.minesweeper

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.gregkluska.minesweeper.Field.Companion.DETONATED_BY_MINE
import com.gregkluska.minesweeper.Field.Companion.DETONATED_BY_PLAYER
import com.gregkluska.minesweeper.Field.Companion.MINE

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
    val isFlagged: Boolean = false
) {
    companion object {
        const val MINE = -1
        const val DETONATED_BY_PLAYER = -2
        const val DETONATED_BY_MINE = -3
    }
}

class Minesweeper(
    val width: Int,
    val height: Int,
    val mines: Int,
) {
    val board = List(height) { List(width) { mutableStateOf(Field()) } }
    var flagMode by mutableStateOf(false)
    var flags by mutableStateOf(0)
        private set

    private var initialised: Boolean = false

    /**
     * Mine coords, X to Y
     */
    private val minesCoords = mutableListOf<Pair<Int, Int>>()

    var gameOver by mutableStateOf(false)
        private set

    init {
        require(width > 0 && height > 0 && mines > 0 && width * height > mines)
    }

    private fun reset() {
        for (r in board.indices) {
            for (c in board[0].indices) {
                board[r][c].value = Field()
            }
        }
        minesCoords.clear()
        flags = 0
        initialised = false
    }

    /**
     * Initialise the board. If both [cx] and [cy] are provided, the mine won't be spawned there
     */
    private fun initialise(
        cx: Int? = null,
        cy: Int? = null
    ) {
        reset()

        var mineShuffle = List(width * height) { if (it < mines) -1 else 0 }.shuffled()

        val cIdx: Int? = if(cx != null && cy != null) {
            height * cy + cx
        } else {
            null
        }

        cIdx?.let {
            while(mineShuffle[it] == -1){
                mineShuffle = mineShuffle.shuffled()
            }
        }

        for (i in mineShuffle.indices) {
            if (mineShuffle[i] == 0) continue
            val y = i.div(width)
            val x = i - (y * width)
            board[y][x].value = board[y][x].value.copy(value = MINE)
            minesCoords.add(x to y)
            updateAdjacentMines(x, y)
        }
        minesCoords.shuffle()

        initialised = true
    }

    fun click(row: Int, col: Int) {
        val x = col.coerceIn(0 until width)
        val y = row.coerceIn(0 until height)
        if (!flagMode) {
            reveal(x, y)
        } else {
            flag(x, y)
        }
    }

    private fun flag(x: Int, y: Int) {
        if (gameOver) return
        val field = board[y][x].value
        if (field.isRevealed) return

        val isFlagged = field.isFlagged

        when {
            isFlagged -> {
                board[y][x].value = field.copy(isFlagged = false)
                flags--
            }

            !isFlagged && flags < mines -> {
                board[y][x].value = field.copy(isFlagged = true)
                flags++
            }

            else -> {}
        }
    }

    private fun reveal(x: Int, y: Int) {
        if (!initialised) initialise(x, y)
        if (gameOver) return
        val field = board[y][x].value
        if (field.isRevealed || field.isFlagged) return
        if (field.value == -1) {
            board[y][x].value = field.copy(value = DETONATED_BY_PLAYER)
            for (mc in minesCoords) {
                if (mc.first == x && mc.second == y) continue
                val mine = board[mc.second][mc.first].value
                board[mc.second][mc.first].value = mine.copy(value = DETONATED_BY_MINE)
            }
            gameOver = true
            return
        }

        board[y][x].value = field.copy(isRevealed = true)

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
                    board[y + fy][x + fx].value.value > -1
                ) {
                    val field = board[y + fy][x + fx].value
                    board[y + fy][x + fx].value = field.copy(value = field.value + 1)
                }
            }
        }
    }
}
