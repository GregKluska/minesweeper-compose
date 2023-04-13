package com.gregkluska.minesweeper

import kotlin.random.Random

fun main() {
    val game = Minesweeper(10, 10, 30)

    while(!game.gameOver) {
        game.printGame()
        print("X: ")
        val x = readLine()!!
        println()
        print("Y: ")
        val y = readLine()!!
        game.reveal(x.toInt(), y.toInt())
    }
}

class Minesweeper(
    val width: Int,
    val height: Int,
    val mines: Int,
) {

    val board = List(height) { MutableList(width) { 0 } }
    val flags = mutableListOf<Pair<Int, Int>>()
    val revealed = mutableListOf<Pair<Int, Int>>()
    var gameOver = false

    init {
        require(width > 0 && height > 0 && mines > 0 && width * height > mines)

        val mineCoords = List(width * height) { if (it < mines) -1 else 0 }.shuffled()
        for (i in mineCoords.indices) {
            if (mineCoords[i] == 0) continue
            val y = i.div(width)
            val x = i - (y * width)
            board[y][x] = -1
            updateAdjacentMines(x, y)
        }

//        printBoard()
    }

    fun reveal(x: Int, y: Int) {
        if (gameOver) return
        if ((x to y) in flags || (x to y) in revealed) return
        if (board[y][x] == -1) {
            gameOver = true
            return
        }

        revealed.add((x to y))

        if (board[y][x] == 0) {
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
                    board[y + fy][x + fx] > -1
                ) {
                    board[y + fy][x + fx]++
                }
            }
        }
    }

    fun printGame() {
        board.forEachIndexed { y, row ->
            row.forEachIndexed { x, num ->
                if((x to y) in revealed) {
                    print(if (num < 0) " X " else " $num ")
                } else {
                    print(" * ")
                }

            }
            println()
        }
    }

    private fun printBoard() {
        board.forEach {
            it.forEach {
                print(if (it < 0) " X " else " $it ")
            }
            println()
        }
    }
}