package com.gdproduct.rendana.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Suppress("CheckReturnValue")
public val Receipt: ImageVector
  get() {
    if (_Receipt != null) {
      return _Receipt!!
    }
    _Receipt =
      ImageVector.Builder(
          name = "Receipt",
          defaultWidth = 24.dp,
          defaultHeight = 24.dp,
          viewportWidth = 24f,
          viewportHeight = 24f,
        )
        .apply {
          path(
            fill = SolidColor(Color.Black),
            fillAlpha = 1f,
            stroke = null,
            strokeAlpha = 1f,
            strokeLineWidth = 1f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Bevel,
            strokeLineMiter = 1f,
            pathFillType = PathFillType.Companion.NonZero,
          ) {
            moveTo(3f, 22f)
            verticalLineTo(2f)
            lineTo(4.5f, 3.5f)
            lineTo(6f, 2f)
            lineTo(7.5f, 3.5f)
            lineTo(9f, 2f)
            lineToRelative(1.5f, 1.5f)
            lineTo(12f, 2f)
            lineToRelative(1.5f, 1.5f)
            lineTo(15f, 2f)
            lineToRelative(1.5f, 1.5f)
            lineTo(18f, 2f)
            lineToRelative(1.5f, 1.5f)
            lineTo(21f, 2f)
            verticalLineTo(22f)
            lineTo(19.5f, 20.5f)
            lineTo(18f, 22f)
            lineTo(16.5f, 20.5f)
            lineTo(15f, 22f)
            lineTo(13.5f, 20.5f)
            lineTo(12f, 22f)
            lineTo(10.5f, 20.5f)
            lineTo(9f, 22f)
            lineTo(7.5f, 20.5f)
            lineTo(6f, 22f)
            lineTo(4.5f, 20.5f)
            lineTo(3f, 22f)
            close()
            moveTo(6f, 17f)
            horizontalLineTo(18f)
            verticalLineTo(15f)
            horizontalLineTo(6f)
            verticalLineToRelative(2f)
            close()
            moveTo(6f, 13f)
            horizontalLineTo(18f)
            verticalLineTo(11f)
            horizontalLineTo(6f)
            verticalLineToRelative(2f)
            close()
            moveTo(6f, 9f)
            horizontalLineTo(18f)
            verticalLineTo(7f)
            horizontalLineTo(6f)
            verticalLineTo(9f)
            close()
            moveTo(5f, 19.1f)
            horizontalLineTo(19f)
            verticalLineTo(4.9f)
            horizontalLineTo(5f)
            verticalLineTo(19.1f)
            close()
            moveTo(5f, 4.9f)
            verticalLineTo(19.1f)
            verticalLineTo(4.9f)
            close()
          }
        }
        .build()
    return _Receipt!!
  }

private var _Receipt: ImageVector? = null