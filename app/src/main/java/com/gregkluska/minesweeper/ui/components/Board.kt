package com.gregkluska.minesweeper.ui.components

import android.graphics.Paint
import android.text.TextPaint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import com.gregkluska.minesweeper.Field
import com.gregkluska.minesweeper.Field.Companion.DETONATED_BY_MINE
import com.gregkluska.minesweeper.Field.Companion.DETONATED_BY_PLAYER
import com.gregkluska.minesweeper.ui.canvas.drawFlag
import com.gregkluska.minesweeper.ui.theme.Orange
import java.lang.Float.min


@Composable
fun Board(
    modifier: Modifier = Modifier,
    fields: List<List<Field>>,
    fieldColors: FieldColors = fieldColors(),
    detonatedColor: Color = MaterialTheme.colorScheme.error,
    detonatedColorAlt: Color = MaterialTheme.colorScheme.errorContainer,
    onClick: (row: Int, col: Int) -> Unit = { _, _ -> },
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        val rows = fields.size.toFloat()
        val cols = fields[0].size.toFloat()

        val fieldSize: Float = with(LocalDensity.current) { 40.dp.toPx() }

        val boardWidth = fieldSize * cols
        val boardWidthDp = with(LocalDensity.current) { (fieldSize * cols).toDp() }
        val boardHeight = fieldSize * rows
        val boardHeightDp = with(LocalDensity.current) { (fieldSize * rows).toDp() }

        val scale = remember { mutableStateOf(1f) }
        val offset = remember { mutableStateOf(Offset.Zero) }

//        = boardWidth * scale.value

        val transformableState = rememberTransformableState { zoomChange, offsetChange, _ ->

            scale.value *= zoomChange

            val bw = boardWidth * scale.value
            val sw = min(constraints.maxWidth.toFloat(),boardWidth)
            val bh = boardHeight * scale.value
            val sh = min(constraints.maxHeight.toFloat(),boardHeight)

            val xMax = -sw*(1-scale.value)/2 //984*1-s /2
            val xMin = -bw+sw+xMax
            val xRan = if(xMin<xMax) xMin..xMax else xMax..xMin

            val yMax = -sh*(1-scale.value)/2 //984*1-s /2
            val yMin = -bh+sh+yMax
            val yRan = if(yMin<yMax) yMin..yMax else yMax..yMin

            offset.value = Offset(
                x = (offset.value.x+offsetChange.x).coerceIn(xRan),
                y = (offset.value.y+offsetChange.y).coerceIn(yRan)
            )
        }

        Canvas(
            modifier = Modifier
                .size(
                    width = boardWidthDp,
                    height = boardHeightDp
                )
                .transformable(transformableState)
                .graphicsLayer(
                    scaleX = scale.value,
                    scaleY = scale.value,
                    translationX = offset.value.x,
                    translationY = offset.value.y
                )
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { offset ->
                            val x = offset.x.div(fieldSize)
                            val y = offset.y.div(fieldSize)
                            onClick(y.toInt(), x.toInt())
                        }
                    )
                }
        ) {
            var h = 0
            for (r in fields.indices) {
                for (c in fields[r].indices) {
                    drawRect(
                        color = fieldColors.getColor(
                            light = h.mod(2) == 0,
                            revealed = true
                        ),
                        topLeft = Offset(
                            x = c * fieldSize,
                            y = r * fieldSize
                        ),
                        size = Size(fieldSize, fieldSize)
                    )

                    if (fields[r][c].value > 0) {
                        drawIntoCanvas { canvas ->

                            val textPaint = TextPaint()
                            textPaint.color = Orange.toArgb()
                            textPaint.textSize = fieldSize * 0.6F
                            textPaint.textAlign = Paint.Align.CENTER

                            val textHeight = textPaint.descent() - textPaint.ascent()
                            val textOffset = textHeight / 2 - textPaint.descent()

                            canvas.nativeCanvas.drawText(
                                fields[r][c].value.toString(),
                                c * fieldSize + (fieldSize / 2),
                                r * fieldSize + (fieldSize / 2) + textOffset,
                                textPaint
                            )
                        }
                    }

                    h++
                }
                if (fields[0].size.mod(2) == 0) {
                    h--
                }
            }

            // Outline
            for (r in fields.indices) {
                for (c in fields[r].indices) {
                    if (fields[r][c].isRevealed) continue

                    val outlineSize = 4

                    // This is to not draw outline at right and bottom sides
                    val sw = if (c + 1 == fields[0].size) outlineSize / 2 else outlineSize
                    val sh = if (r + 1 == fields.size) outlineSize / 2 else outlineSize

                    drawRect(
                        color = fieldColors.outlineColor,
                        topLeft = Offset(
                            x = c * fieldSize - outlineSize,
                            y = r * fieldSize - outlineSize
                        ),
                        size = Size(fieldSize + (sw * 2), fieldSize + (sh * 2))
                    )
                }
            }

            h = 0
            for (r in fields.indices) {
                for (c in fields[r].indices) {
                    if (!fields[r][c].isRevealed) {
                        drawRect(
                            color = fieldColors.getColor(
                                light = h.mod(2) == 0,
                                revealed = false
                            ),
                            topLeft = Offset(
                                x = c * fieldSize,
                                y = r * fieldSize
                            ),
                            size = Size(fieldSize, fieldSize)
                        )

                        if (fields[r][c].isFlagged) {
                            // flag
                            drawFlag(
                                topLeft = Offset(
                                    x = c * fieldSize,
                                    y = r * fieldSize
                                ),
                                size = Size(fieldSize, fieldSize)
                            )

                        }
                    }

                    if (fields[r][c].value == DETONATED_BY_PLAYER) {
                        drawRect(
                            color = detonatedColor,
                            topLeft = Offset(
                                x = c * fieldSize,
                                y = r * fieldSize
                            ),
                            size = Size(fieldSize, fieldSize)
                        )
                        drawCircle(
                            color = detonatedColorAlt,
                            radius = fieldSize / 4,
                            center = Offset(
                                x = c * fieldSize + fieldSize / 2,
                                y = r * fieldSize + fieldSize / 2
                            )
                        )
                    } else if (fields[r][c].value == DETONATED_BY_MINE) {
                        drawRect(
                            color = detonatedColorAlt,
                            topLeft = Offset(
                                x = c * fieldSize,
                                y = r * fieldSize
                            ),
                            size = Size(fieldSize, fieldSize)
                        )
                        drawCircle(
                            color = detonatedColor,
                            radius = fieldSize / 4,
                            center = Offset(
                                x = c * fieldSize + fieldSize / 2,
                                y = r * fieldSize + fieldSize / 2
                            )
                        )
                    }
                    h++
                }
                if (fields[0].size.mod(2) == 0) {
                    h--
                }
            }
        }
    }
}

data class FieldColors(
    val darkColor: Color,
    val lightColor: Color,
    val darkRevealedColor: Color,
    val lightRevealedColor: Color,
    val outlineColor: Color
) {
    fun getColor(
        light: Boolean,
        revealed: Boolean
    ): Color = when {
        !light && !revealed -> darkColor
        light && !revealed -> lightColor
        !light && revealed -> darkRevealedColor
        else -> lightRevealedColor
    }
}

@Composable
private fun fieldColors(
    dark: Color = MaterialTheme.colorScheme.inversePrimary,
    light: Color = MaterialTheme.colorScheme.primaryContainer,
    darkRevealed: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
    lightRevealed: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
    outlineColor: Color = MaterialTheme.colorScheme.primary
): FieldColors {
    return FieldColors(
        darkColor = dark,
        lightColor = light,
        darkRevealedColor = darkRevealed,
        lightRevealedColor = lightRevealed,
        outlineColor = outlineColor
    )
}

@Preview
@Preview(wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE)
@Preview(wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE)
@Preview(wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE)
@Preview(wallpaper = Wallpapers.YELLOW_DOMINATED_EXAMPLE)
@Composable
fun BoardPreview() {
    Board(
        fields = List(10) { List(10) { Field() } }
    )
}

@Preview
@Preview(wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE)
@Preview(wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE)
@Preview(wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE)
@Preview(wallpaper = Wallpapers.YELLOW_DOMINATED_EXAMPLE)
@Composable
fun BoardPreview2() {
    Board(
        fields = List(11) { List(11) { Field() } }
    )
}