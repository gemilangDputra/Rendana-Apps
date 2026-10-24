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
public val Redeem: ImageVector
  get() {
    if (_Redeem != null) {
      return _Redeem!!
    }
    _Redeem =
      ImageVector.Builder(
          name = "Redeem",
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
            moveTo(4f, 17f)
            verticalLineToRelative(2f)
            horizontalLineTo(20f)
            verticalLineTo(17f)
            horizontalLineTo(4f)
            close()
            moveTo(4f, 6f)
            horizontalLineTo(6.2f)
            quadTo(6.08f, 5.77f, 6.04f, 5.52f)
            reflectiveQuadTo(6f, 5f)
            quadTo(6f, 3.75f, 6.88f, 2.88f)
            reflectiveQuadTo(9f, 2f)
            quadToRelative(0.75f, 0f, 1.39f, 0.39f)
            reflectiveQuadTo(11.5f, 3.35f)
            lineTo(12f, 4f)
            lineTo(12.5f, 3.35f)
            quadToRelative(0.45f, -0.6f, 1.1f, -0.97f)
            reflectiveQuadTo(15f, 2f)
            quadToRelative(1.25f, 0f, 2.13f, 0.88f)
            reflectiveQuadTo(18f, 5f)
            quadToRelative(0f, 0.27f, -0.04f, 0.52f)
            reflectiveQuadTo(17.8f, 6f)
            horizontalLineTo(20f)
            quadToRelative(0.83f, 0f, 1.41f, 0.59f)
            quadTo(22f, 7.18f, 22f, 8f)
            verticalLineTo(19f)
            quadToRelative(0f, 0.82f, -0.59f, 1.41f)
            reflectiveQuadTo(20f, 21f)
            horizontalLineTo(4f)
            quadTo(3.18f, 21f, 2.59f, 20.41f)
            reflectiveQuadTo(2f, 19f)
            verticalLineTo(8f)
            quadTo(2f, 7.18f, 2.59f, 6.59f)
            reflectiveQuadTo(4f, 6f)
            close()
            moveToRelative(0f, 8f)
            horizontalLineTo(20f)
            verticalLineTo(8f)
            horizontalLineTo(14.9f)
            lineTo(17f, 10.85f)
            lineTo(15.4f, 12f)
            lineTo(12f, 7.4f)
            lineTo(8.6f, 12f)
            lineTo(7f, 10.85f)
            lineTo(9.05f, 8f)
            horizontalLineTo(4f)
            verticalLineToRelative(6f)
            close()
            moveTo(9.71f, 5.71f)
            quadTo(10f, 5.43f, 10f, 5f)
            reflectiveQuadTo(9.71f, 4.29f)
            reflectiveQuadTo(9f, 4f)
            quadTo(8.58f, 4f, 8.29f, 4.29f)
            reflectiveQuadTo(8f, 5f)
            reflectiveQuadTo(8.29f, 5.71f)
            quadTo(8.58f, 6f, 9f, 6f)
            quadTo(9.43f, 6f, 9.71f, 5.71f)
            close()
            moveTo(15f, 6f)
            quadToRelative(0.43f, 0f, 0.71f, -0.29f)
            quadTo(16f, 5.43f, 16f, 5f)
            reflectiveQuadTo(15.71f, 4.29f)
            reflectiveQuadTo(15f, 4f)
            reflectiveQuadTo(14.29f, 4.29f)
            reflectiveQuadTo(14f, 5f)
            reflectiveQuadToRelative(0.29f, 0.71f)
            reflectiveQuadTo(15f, 6f)
            close()
          }
        }
        .build()
    return _Redeem!!
  }

private var _Redeem: ImageVector? = null