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
public val Store: ImageVector
  get() {
    if (_Store != null) {
      return _Store!!
    }
    _Store =
      ImageVector.Builder(
          name = "Store",
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
            moveTo(4f, 6f)
            verticalLineTo(4f)
            horizontalLineTo(20f)
            verticalLineTo(6f)
            horizontalLineTo(4f)
            close()
            moveTo(4f, 20f)
            verticalLineTo(14f)
            horizontalLineTo(3f)
            verticalLineTo(12f)
            lineTo(4f, 7f)
            horizontalLineTo(20f)
            lineToRelative(1f, 5f)
            verticalLineToRelative(2f)
            horizontalLineTo(20f)
            verticalLineToRelative(6f)
            horizontalLineTo(18f)
            verticalLineTo(14f)
            horizontalLineTo(14f)
            verticalLineToRelative(6f)
            horizontalLineTo(4f)
            close()
            moveTo(6f, 18f)
            horizontalLineToRelative(6f)
            verticalLineTo(14f)
            horizontalLineTo(6f)
            verticalLineToRelative(4f)
            close()
            moveTo(5.05f, 12f)
            horizontalLineToRelative(13.9f)
            horizontalLineTo(5.05f)
            close()
            moveToRelative(0f, 0f)
            horizontalLineToRelative(13.9f)
            lineTo(18.35f, 9f)
            horizontalLineTo(5.65f)
            lineToRelative(-0.6f, 3f)
            close()
          }
        }
        .build()
    return _Store!!
  }

private var _Store: ImageVector? = null