package com.gregkluska.minesweeper.util

import java.util.concurrent.TimeUnit

/**
 * Convert Long in milliseconds to string in given format
 */
fun Long.toDuration(format: String = "%02d:%02d"): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(this)
    val secondsTotal = TimeUnit.MILLISECONDS.toSeconds(this)
    val seconds = secondsTotal - TimeUnit.MINUTES.toSeconds(minutes)

    return String.format(format, minutes, seconds)
}