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
public val DocumentScanner: ImageVector
  get() {
    if (_DocumentScanner != null) {
      return _DocumentScanner!!
    }
    _DocumentScanner =
      ImageVector.Builder(
          name = "DocumentScanner",
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
            moveTo(2f, 6f)
            verticalLineTo(1f)
            horizontalLineTo(7f)
            verticalLineTo(3f)
            horizontalLineTo(4f)
            verticalLineTo(6f)
            horizontalLineTo(2f)
            close()
            moveTo(20f, 6f)
            verticalLineTo(3f)
            horizontalLineTo(17f)
            verticalLineTo(1f)
            horizontalLineToRelative(5f)
            verticalLineTo(6f)
            horizontalLineTo(20f)
            close()
            moveTo(2f, 23f)
            verticalLineTo(18f)
            horizontalLineTo(4f)
            verticalLineToRelative(3f)
            horizontalLineTo(7f)
            verticalLineToRelative(2f)
            horizontalLineTo(2f)
            close()
            moveToRelative(15f, 0f)
            verticalLineTo(21f)
            horizontalLineToRelative(3f)
            verticalLineTo(18f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(5f)
            horizontalLineTo(17f)
            close()
            moveTo(7f, 18f)
            horizontalLineTo(17f)
            verticalLineTo(6f)
            horizontalLineTo(7f)
            verticalLineTo(18f)
            close()
            moveToRelative(0f, 2f)
            quadTo(6.18f, 20f, 5.59f, 19.41f)
            reflectiveQuadTo(5f, 18f)
            verticalLineTo(6f)
            quadTo(5f, 5.18f, 5.59f, 4.59f)
            reflectiveQuadTo(7f, 4f)
            horizontalLineTo(17f)
            quadToRelative(0.82f, 0f, 1.41f, 0.59f)
            quadTo(19f, 5.18f, 19f, 6f)
            verticalLineTo(18f)
            quadToRelative(0f, 0.82f, -0.59f, 1.41f)
            reflectiveQuadTo(17f, 20f)
            horizontalLineTo(7f)
            close()
            moveTo(9f, 10f)
            horizontalLineToRelative(6f)
            verticalLineTo(8f)
            horizontalLineTo(9f)
            verticalLineToRelative(2f)
            close()
            moveToRelative(0f, 3f)
            horizontalLineToRelative(6f)
            verticalLineTo(11f)
            horizontalLineTo(9f)
            verticalLineToRelative(2f)
            close()
            moveToRelative(0f, 3f)
            horizontalLineToRelative(6f)
            verticalLineTo(14f)
            horizontalLineTo(9f)
            verticalLineToRelative(2f)
            close()
            moveTo(7f, 18f)
            verticalLineTo(6f)
            verticalLineTo(18f)
            close()
          }
        }
        .build()
    return _DocumentScanner!!
  }

private var _DocumentScanner: ImageVector? = null