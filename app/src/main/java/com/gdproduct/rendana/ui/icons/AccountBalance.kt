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
public val AccountBalance: ImageVector
  get() {
    if (_AccountBalance != null) {
      return _AccountBalance!!
    }
    _AccountBalance =
      ImageVector.Builder(
          name = "AccountBalance",
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
            moveTo(5f, 17f)
            verticalLineTo(10f)
            horizontalLineTo(7f)
            verticalLineToRelative(7f)
            horizontalLineTo(5f)
            close()
            moveToRelative(6f, 0f)
            verticalLineTo(10f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(7f)
            horizontalLineTo(11f)
            close()
            moveTo(2f, 21f)
            verticalLineTo(19f)
            horizontalLineTo(22f)
            verticalLineToRelative(2f)
            horizontalLineTo(2f)
            close()
            moveTo(17f, 17f)
            verticalLineTo(10f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(7f)
            horizontalLineTo(17f)
            close()
            moveTo(2f, 8f)
            verticalLineTo(6f)
            lineTo(12f, 1f)
            lineTo(22f, 6f)
            verticalLineTo(8f)
            horizontalLineTo(2f)
            close()
            moveTo(6.45f, 6f)
            horizontalLineTo(12f)
            horizontalLineToRelative(5.55f)
            horizontalLineTo(6.45f)
            close()
            moveToRelative(0f, 0f)
            horizontalLineToRelative(11.1f)
            lineTo(12f, 3.25f)
            lineTo(6.45f, 6f)
            close()
          }
        }
        .build()
    return _AccountBalance!!
  }

private var _AccountBalance: ImageVector? = null