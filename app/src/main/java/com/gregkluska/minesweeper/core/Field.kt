package com.gregkluska.minesweeper.core

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