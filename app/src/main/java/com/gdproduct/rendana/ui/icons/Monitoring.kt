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
public val Monitoring: ImageVector
  get() {
    if (_Monitoring != null) {
      return _Monitoring!!
    }
    _Monitoring =
      ImageVector.Builder(
          name = "Monitoring",
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
            moveTo(3f, 21f)
            verticalLineTo(19f)
            lineTo(5f, 17f)
            verticalLineToRelative(4f)
            horizontalLineTo(3f)
            close()
            moveToRelative(4f, 0f)
            verticalLineTo(15f)
            lineTo(9f, 13f)
            verticalLineToRelative(8f)
            horizontalLineTo(7f)
            close()
            moveToRelative(4f, 0f)
            verticalLineTo(13f)
            lineToRelative(2f, 2.03f)
            verticalLineTo(21f)
            horizontalLineTo(11f)
            close()
            moveToRelative(4f, 0f)
            verticalLineTo(15.03f)
            lineToRelative(2f, -2f)
            verticalLineTo(21f)
            horizontalLineTo(15f)
            close()
            moveToRelative(4f, 0f)
            verticalLineTo(11f)
            lineTo(21f, 9f)
            verticalLineTo(21f)
            horizontalLineTo(19f)
            close()
            moveTo(3f, 15.83f)
            verticalLineTo(13f)
            lineTo(10f, 6f)
            lineToRelative(4f, 4f)
            lineTo(21f, 3f)
            verticalLineTo(5.82f)
            lineToRelative(-7f, 7f)
            lineToRelative(-4f, -4f)
            lineToRelative(-7f, 7f)
            close()
          }
        }
        .build()
    return _Monitoring!!
  }

private var _Monitoring: ImageVector? = null