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
val CalendarClock: ImageVector
  get() {
    if (_CalendarClock != null) {
      return _CalendarClock!!
    }
    _CalendarClock =
      ImageVector.Builder(
          name = "CalendarClock",
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
            moveTo(5f, 8f)
            horizontalLineTo(19f)
            verticalLineTo(6f)
            horizontalLineTo(5f)
            verticalLineTo(8f)
            close()
            moveTo(5f, 8f)
            verticalLineTo(6f)
            verticalLineTo(8f)
            close()
            moveTo(5f, 22f)
            quadTo(4.18f, 22f, 3.59f, 21.41f)
            reflectiveQuadTo(3f, 20f)
            verticalLineTo(6f)
            quadTo(3f, 5.18f, 3.59f, 4.59f)
            reflectiveQuadTo(5f, 4f)
            horizontalLineTo(6f)
            verticalLineTo(2f)
            horizontalLineTo(8f)
            verticalLineTo(4f)
            horizontalLineToRelative(8f)
            verticalLineTo(2f)
            horizontalLineToRelative(2f)
            verticalLineTo(4f)
            horizontalLineToRelative(1f)
            quadToRelative(0.83f, 0f, 1.41f, 0.59f)
            quadTo(21f, 5.18f, 21f, 6f)
            verticalLineToRelative(5.67f)
            quadTo(20.53f, 11.45f, 20.03f, 11.3f)
            reflectiveQuadTo(19f, 11.08f)
            verticalLineTo(10f)
            horizontalLineTo(5f)
            verticalLineTo(20f)
            horizontalLineToRelative(6.3f)
            quadToRelative(0.18f, 0.55f, 0.41f, 1.05f)
            reflectiveQuadTo(12.28f, 22f)
            horizontalLineTo(5f)
            close()
            moveToRelative(9.46f, -0.46f)
            quadTo(13f, 20.08f, 13f, 18f)
            reflectiveQuadToRelative(1.46f, -3.54f)
            reflectiveQuadTo(18f, 13f)
            reflectiveQuadToRelative(3.54f, 1.46f)
            reflectiveQuadTo(23f, 18f)
            reflectiveQuadToRelative(-1.46f, 3.54f)
            reflectiveQuadTo(18f, 23f)
            quadToRelative(-2.07f, 0f, -3.54f, -1.46f)
            close()
            moveToRelative(5.21f, -1.16f)
            lineToRelative(0.7f, -0.7f)
            lineTo(18.5f, 17.8f)
            verticalLineTo(15f)
            horizontalLineToRelative(-1f)
            verticalLineToRelative(3.2f)
            lineToRelative(2.18f, 2.18f)
            close()
          }
        }
        .build()
    return _CalendarClock!!
  }

private var _CalendarClock: ImageVector? = null