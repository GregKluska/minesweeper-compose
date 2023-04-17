package com.gregkluska.minesweeper.ui.components

import android.graphics.Paint
import android.text.TextPaint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import com.gregkluska.minesweeper.Field
import com.gregkluska.minesweeper.Field.Companion.DETONATED_BY_MINE
import com.gregkluska.minesweeper.Field.Companion.DETONATED_BY_PLAYER
import com.gregkluska.minesweeper.ui.canvas.drawFlag
import com.gregkluska.minesweeper.ui.theme.Orange


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
        val fieldSize = (constraints.maxWidth / fields[0].size).toFloat()
        val fieldSizeDp = maxWidth / fields[0].size

        Canvas(
            modifier = Modifier
                .size(
                    width = fieldSizeDp * fields[0].size,
                    height = fieldSizeDp * fields.size
                )
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { offset ->
                            val x = offset.x.div(fieldSize)
                            val y = offset.y.div(fieldSize)
                            onClick(y.toInt(), x.toInt())
                        }
                    )
                },
        ) {
            var h = 0
            for (r in fields.indices) {
                for (c in fields.indices) {
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
                for (c in fields.indices) {
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
                for (c in fields.indices) {
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