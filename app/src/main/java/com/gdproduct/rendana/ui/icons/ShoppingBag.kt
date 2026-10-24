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
public val ShoppingBag: ImageVector
  get() {
    if (_ShoppingBag != null) {
      return _ShoppingBag!!
    }
    _ShoppingBag =
      ImageVector.Builder(
          name = "ShoppingBag",
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
            moveTo(6f, 22f)
            quadTo(5.18f, 22f, 4.59f, 21.41f)
            reflectiveQuadTo(4f, 20f)
            verticalLineTo(8f)
            quadTo(4f, 7.18f, 4.59f, 6.59f)
            reflectiveQuadTo(6f, 6f)
            horizontalLineTo(8f)
            quadTo(8f, 4.35f, 9.18f, 3.17f)
            reflectiveQuadTo(12f, 2f)
            reflectiveQuadToRelative(2.83f, 1.17f)
            reflectiveQuadTo(16f, 6f)
            horizontalLineToRelative(2f)
            quadToRelative(0.82f, 0f, 1.41f, 0.59f)
            quadTo(20f, 7.18f, 20f, 8f)
            verticalLineTo(20f)
            quadToRelative(0f, 0.82f, -0.59f, 1.41f)
            reflectiveQuadTo(18f, 22f)
            horizontalLineTo(6f)
            close()
            moveTo(6f, 20f)
            horizontalLineTo(18f)
            verticalLineTo(8f)
            horizontalLineTo(16f)
            verticalLineToRelative(2f)
            quadToRelative(0f, 0.42f, -0.29f, 0.71f)
            reflectiveQuadTo(15f, 11f)
            reflectiveQuadTo(14.29f, 10.71f)
            quadTo(14f, 10.43f, 14f, 10f)
            verticalLineTo(8f)
            horizontalLineTo(10f)
            verticalLineToRelative(2f)
            quadToRelative(0f, 0.42f, -0.29f, 0.71f)
            reflectiveQuadTo(9f, 11f)
            quadTo(8.58f, 11f, 8.29f, 10.71f)
            quadTo(8f, 10.43f, 8f, 10f)
            verticalLineTo(8f)
            horizontalLineTo(6f)
            verticalLineTo(20f)
            close()
            moveTo(10f, 6f)
            horizontalLineToRelative(4f)
            quadTo(14f, 5.18f, 13.41f, 4.59f)
            reflectiveQuadTo(12f, 4f)
            reflectiveQuadTo(10.59f, 4.59f)
            quadTo(10f, 5.18f, 10f, 6f)
            close()
            moveTo(6f, 20f)
            verticalLineTo(8f)
            verticalLineTo(20f)
            close()
          }
        }
        .build()
    return _ShoppingBag!!
  }

private var _ShoppingBag: ImageVector? = null