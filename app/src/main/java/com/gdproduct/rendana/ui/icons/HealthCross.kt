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
public val HealthCross: ImageVector
  get() {
    if (_HealthCross != null) {
      return _HealthCross!!
    }
    _HealthCross =
      ImageVector.Builder(
          name = "HealthCross",
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
            moveTo(8f, 21f)
            verticalLineTo(16f)
            horizontalLineTo(3f)
            verticalLineTo(8f)
            horizontalLineTo(8f)
            verticalLineTo(3f)
            horizontalLineToRelative(8f)
            verticalLineTo(8f)
            horizontalLineToRelative(5f)
            verticalLineToRelative(8f)
            horizontalLineTo(16f)
            verticalLineToRelative(5f)
            horizontalLineTo(8f)
            close()
            moveToRelative(2f, -2f)
            horizontalLineToRelative(4f)
            verticalLineTo(14f)
            horizontalLineToRelative(5f)
            verticalLineTo(10f)
            horizontalLineTo(14f)
            verticalLineTo(5f)
            horizontalLineTo(10f)
            verticalLineToRelative(5f)
            horizontalLineTo(5f)
            verticalLineToRelative(4f)
            horizontalLineToRelative(5f)
            verticalLineToRelative(5f)
            close()
            moveToRelative(2f, -7f)
            close()
          }
        }
        .build()
    return _HealthCross!!
  }

private var _HealthCross: ImageVector? = null