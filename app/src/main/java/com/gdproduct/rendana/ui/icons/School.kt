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
public val School: ImageVector
  get() {
    if (_School != null) {
      return _School!!
    }
    _School =
      ImageVector.Builder(
          name = "School",
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
            moveTo(12f, 21f)
            lineTo(5f, 17.2f)
            verticalLineToRelative(-6f)
            lineTo(1f, 9f)
            lineTo(12f, 3f)
            lineTo(23f, 9f)
            verticalLineToRelative(8f)
            horizontalLineTo(21f)
            verticalLineTo(10.1f)
            lineToRelative(-2f, 1.1f)
            verticalLineToRelative(6f)
            lineTo(12f, 21f)
            close()
            moveToRelative(0f, -8.3f)
            lineTo(18.85f, 9f)
            lineTo(12f, 5.3f)
            lineTo(5.15f, 9f)
            lineTo(12f, 12.7f)
            close()
            moveToRelative(0f, 6.03f)
            lineToRelative(5f, -2.7f)
            verticalLineTo(12.25f)
            lineTo(12f, 15f)
            lineTo(7f, 12.25f)
            verticalLineToRelative(3.77f)
            lineToRelative(5f, 2.7f)
            close()
            moveTo(12f, 12.7f)
            close()
            moveToRelative(0f, 2.25f)
            close()
            moveToRelative(0f, 0f)
            close()
          }
        }
        .build()
    return _School!!
  }

private var _School: ImageVector? = null