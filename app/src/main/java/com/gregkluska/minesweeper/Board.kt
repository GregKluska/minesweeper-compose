package com.gregkluska.minesweeper

//@Composable
//fun Board(
//    rows: Int,
//    columns: Int,
//    fields: List<List<Field>>,
//    onClick: (x: Int, y: Int) -> Unit
//) {
//    BoxWithConstraints(
//        modifier = Modifier.fillMaxWidth()
//    ) {
//
//        val maxWidthDp = with(LocalDensity.current) { maxWidth.toPx() }
//
//        val fieldSizeDp: Dp = maxWidth / columns
//        val fieldSize: Float = (maxWidthDp / columns)
//
//        Canvas(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(fieldSizeDp * rows)
//                .pointerInput(Unit) {
//                    detectTapGestures(
//                        onTap = { offset ->
//                            val x = offset.x.div(fieldSize)
//                            val y = offset.y.div(fieldSize)
//                            onClick(x.toInt(), y.toInt())
//                        }
//                    )
//                }
//        ) {
//            var counter = 0
//
//            // Brown Fields
//            for (row in 0 until rows) {
//                for (x in 0 until columns) {
//                    val column = if (row.rem(2) == 0) x else (columns - x - 1)
//                    val field = fields[row][column]
//                    val colorScheme = if (counter.rem(2) == 0) darkFieldColor else lightFieldColor
//
//                    val bounds = getFieldBounds(
//                        column = column,
//                        row = row,
//                        fieldSize = fieldSize
//                    )
//
//                    // Field
//                    if (field.value > -1) {
//                        // Background
//                        drawField(
//                            color = colorScheme.revealed,
//                            left = bounds.left,
//                            top = bounds.top,
//                            size = fieldSize
//                        )
//
//                        // Adjacent mines
//                        if (field.value != 0) {
//                            drawIntoCanvas { canvas ->
//
//                                val textPaint = TextPaint()
//                                textPaint.color = Orange.toArgb()
//                                textPaint.textSize = fieldSize * 0.6F
//                                textPaint.textAlign = Paint.Align.CENTER
//
//                                val textHeight = textPaint.descent() - textPaint.ascent()
//                                val textOffset = textHeight / 2 - textPaint.descent()
//
//                                canvas.nativeCanvas.drawText(
//                                    field.value.toString(),
//                                    bounds.centerX(),
//                                    bounds.centerY() + textOffset,
//                                    textPaint
//                                )
//                            }
//                        }
//                    }
//
//                    counter += 1
//                }
//            }
//
//            // Outline Fields, each field is a bit larger (2F)
//            for (row in 0 until rows) {
//                for (x in 0 until columns) {
//                    val column = if (row.rem(2) == 0) x else (columns - x - 1)
//                    val field = fields[row][column]
//
//                    if (!field.isRevealed || (field.isRevealed && field.value == -1)) {
//                        val strokeSize = 5
//
//                        val bounds = getFieldBounds(
//                            column = column,
//                            row = row,
//                            fieldSize = fieldSize
//                        )
//                        // Field
//                        drawField(
//                            color = GreenBorder,
//                            left = bounds.left - strokeSize,
//                            top = bounds.top - strokeSize,
//                            size = fieldSize + strokeSize*2,
//                        )
//                    }
//                }
//            }
//            // Green Fields
//            counter = 0
//            for (row in 0 until rows) {
//                for (x in 0 until columns) {
//
//                    val column = if (row.rem(2) == 0) x else (columns - x - 1)
//                    val field = fields[row][column]
//                    val colorScheme = if (counter.rem(2) == 0) darkFieldColor else lightFieldColor
//
//                    val bounds = getFieldBounds(
//                        column = column,
//                        row = row,
//                        fieldSize = fieldSize
//                    )
//                    if (!field.isRevealed) {
//                        // Just a field
//                        drawField(
//                            color = colorScheme.default,
//                            left = bounds.left,
//                            top = bounds.top,
//                            size = fieldSize,
//                        )
//                    } else {
//                        if (field.value == -1) {
//                            drawField(
//                                color = getRandomMineColor(),
//                                left = bounds.left,
//                                top = bounds.top,
//                                size = fieldSize,
//                            )
//                            drawCircle(
//                                color = MineColor,
//                                radius = 0.55F * fieldSize / 2,
//                                center = Offset(bounds.centerX(), bounds.centerY())
//                            )
//                        }
//                    }
//
//                    counter += 1
//                }
//            }
//
//        }
//    }
//}
//
//private fun getRandomMineColor(): Color {
//    val colors = listOf(Red, Pink, Purple, Orange, OrangeLight, Blue, BlueLight, GreenAlt)
//    val rand = nextInt(colors.size)
//    return colors[rand]
//}
//
//fun DrawScope.drawField(
//    color: Color,
//    left: Float,
//    top: Float,
//    size: Float,
//) {
//
//
//    drawRect(
//        color = color,
//        topLeft = Offset(x = left, y = top),
//        size = Size(width = size, height = size)
//    )
//}
//
//private fun getFieldBounds(
//    column: Int,
//    row: Int,
//    fieldSize: Float
//): RectF {
//    return RectF(
//        column * fieldSize,
//        row * fieldSize,
//        (column * fieldSize) + fieldSize,
//        (row * fieldSize) + fieldSize
//    )
//}
//
//@Composable
//@Preview
//private fun BoardPreview() {
//    Column(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        Board(
//            rows = 10,
//            columns = 10,
//            listOf(),
//            onClick = { x, y -> println("PRESSED X: $x     Y: $y") })
//    }
//
//}