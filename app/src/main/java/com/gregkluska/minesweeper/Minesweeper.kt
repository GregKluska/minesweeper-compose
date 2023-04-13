package com.gregkluska.minesweeper

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

//fun main() {
//    val game = Minesweeper(10, 10, 10)
//
//    while(!game.gameOver) {
//        game.printGame()
//        print("X: ")
//        val x = readLine()!!
//        println()
//        print("Y: ")
//        val y = readLine()!!
//        game.reveal(x.toInt(), y.toInt())
//    }
//}

data class Field(
    val value: Int = 0,
    val isRevealed: Boolean = false,
    val isFlagged: Boolean = false
)

class Minesweeper(
    val width: Int,
    val height: Int,
    val mines: Int,
) {
    val board = List(height) { List(width) { mutableStateOf(Field()) } }

    var gameOver by mutableStateOf(false)
        private set

    init {
        require(width > 0 && height > 0 && mines > 0 && width * height > mines)

        val mineCoords = List(width * height) { if (it < mines) -1 else 0 }.shuffled()
        for (i in mineCoords.indices) {
            if (mineCoords[i] == 0) continue
            val y = i.div(width)
            val x = i - (y * width)
            board[y][x].value = board[y][x].value.copy(value = -1)
            updateAdjacentMines(x, y)
        }

//        printBoard()
    }

    fun reveal(x: Int, y: Int) {
        if (gameOver) return
        val field = board[y][x].value
        if (field.isRevealed || field.isFlagged) return
        if (field.value == -1) {
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

//    fun printGame() {
//        board.forEachIndexed { y, row ->
//            row.forEachIndexed { x, num ->
//                if((x to y) in revealed) {
//                    print(if (num.value < 0) " X " else " $num ")
//                } else {
//                    print(" * ")
//                }
//
//            }
//            println()
//        }
//    }
//
//    private fun printBoard() {
//        board.forEach {
//            it.forEach {
//                print(if (it.value < 0) " X " else " ${it.value} ")
//            }
//            println()
//        }
//    }
}