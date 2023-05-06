package com.gregkluska.minesweeper.presentation.canvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

fun DrawScope.drawFlag(
    topLeft: Offset,
    size: Size
) {
    // We're drawing on 1x1 and then scale it to match size
    fun x(x: Double): Float = (x * size.width).toFloat() + topLeft.x
    fun y(y: Double): Float = (y * size.height).toFloat() + topLeft.y

    val stickPath = androidx.compose.ui.graphics.Path().apply {
        // M 20 15
        moveTo(x(0.2), y(0.15))
        // L 20 84
        lineTo(x(0.2), y(0.84))
        // C 20 85.6569 21.3431 87 23 87
        cubicTo(x(0.20), y(0.856569), x(0.213431), y(0.87), x(0.23), y(0.87))
        // C 24.6569 87 26 85.6569 26 84
        cubicTo(x(0.246569), y(0.87), x(0.26), y(0.856569), x(0.26), y(0.84))
        // L 26 15
        lineTo(x(0.26), y(0.15))
        // Z
        close()
    }

    val flagPath = androidx.compose.ui.graphics.Path().apply {
        // M78 35.5
        moveTo(x(0.78), y(0.355))
        // L26 15
        lineTo(x(0.26), y(0.15))
        // V54
        lineTo(x(0.26), y(0.54))
        // L78 35.5
        lineTo(x(0.78), y(0.355))
        // Z
        close()
    }
    drawPath(
        path = stickPath,
        color = Color.Black,
    )

    drawPath(
        path = flagPath,
        color = Color.Red
    )
}