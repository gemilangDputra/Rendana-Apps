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
public val Transportation: ImageVector
  get() {
    if (_Transportation != null) {
      return _Transportation!!
    }
    _Transportation =
      ImageVector.Builder(
          name = "Transportation",
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
            moveTo(19f, 16f)
            quadToRelative(-1.8f, 0f, -3.17f, -1.13f)
            reflectiveQuadTo(14.1f, 12f)
            horizontalLineTo(11.13f)
            lineTo(9.93f, 10f)
            horizontalLineTo(14.1f)
            quadTo(14.23f, 9.45f, 14.44f, 8.95f)
            reflectiveQuadTo(15f, 8f)
            horizontalLineTo(8.7f)
            lineTo(7.5f, 6f)
            horizontalLineToRelative(8.55f)
            lineTo(14.95f, 3f)
            horizontalLineTo(11f)
            verticalLineTo(1f)
            horizontalLineToRelative(3.95f)
            quadTo(15.6f, 1f, 16.1f, 1.36f)
            reflectiveQuadToRelative(0.72f, 0.96f)
            lineTo(18.18f, 6f)
            horizontalLineTo(19f)
            quadToRelative(2.08f, 0f, 3.54f, 1.46f)
            reflectiveQuadTo(24f, 11f)
            reflectiveQuadToRelative(-1.46f, 3.54f)
            reflectiveQuadTo(19f, 16f)
            close()
            moveToRelative(0f, -2f)
            quadToRelative(1.25f, 0f, 2.13f, -0.88f)
            reflectiveQuadTo(22f, 11f)
            reflectiveQuadTo(21.13f, 8.88f)
            reflectiveQuadTo(19f, 8f)
            quadTo(18.98f, 8f, 18.96f, 8f)
            reflectiveQuadTo(18.93f, 8f)
            lineToRelative(0.98f, 2.67f)
            lineTo(18f, 11.35f)
            lineTo(17.05f, 8.73f)
            quadToRelative(-0.5f, 0.42f, -0.77f, 1.03f)
            reflectiveQuadTo(16f, 11f)
            quadToRelative(0f, 1.25f, 0.88f, 2.13f)
            reflectiveQuadTo(19f, 14f)
            close()
            moveTo(7f, 23f)
            quadTo(5.75f, 23f, 4.88f, 22.13f)
            reflectiveQuadTo(4f, 20f)
            horizontalLineTo(0f)
            verticalLineTo(18f)
            horizontalLineTo(4.78f)
            quadTo(5.15f, 17.63f, 5.73f, 17.31f)
            reflectiveQuadTo(7f, 17f)
            quadToRelative(0.63f, 0f, 1.2f, 0.25f)
            reflectiveQuadTo(9.23f, 18f)
            horizontalLineTo(12f)
            verticalLineTo(16f)
            horizontalLineTo(0f)
            verticalLineTo(14f)
            horizontalLineTo(2f)
            verticalLineTo(11f)
            horizontalLineTo(0f)
            verticalLineTo(9f)
            horizontalLineTo(7f)
            lineToRelative(3f, 5f)
            horizontalLineToRelative(2f)
            quadToRelative(0.83f, 0f, 1.41f, 0.59f)
            reflectiveQuadTo(14f, 16f)
            verticalLineToRelative(2f)
            quadToRelative(0f, 0.82f, -0.59f, 1.41f)
            reflectiveQuadTo(12f, 20f)
            horizontalLineTo(10f)
            quadToRelative(0f, 1.25f, -0.88f, 2.13f)
            reflectiveQuadTo(7f, 23f)
            close()
            moveTo(4f, 14f)
            horizontalLineTo(7.68f)
            lineTo(5.88f, 11f)
            horizontalLineTo(4f)
            verticalLineToRelative(3f)
            close()
            moveToRelative(3f, 7f)
            quadToRelative(0.43f, 0f, 0.71f, -0.29f)
            quadTo(8f, 20.43f, 8f, 20f)
            reflectiveQuadTo(7.71f, 19.29f)
            reflectiveQuadTo(7f, 19f)
            quadTo(6.58f, 19f, 6.29f, 19.29f)
            reflectiveQuadTo(6f, 20f)
            reflectiveQuadToRelative(0.29f, 0.71f)
            reflectiveQuadTo(7f, 21f)
            close()
            moveTo(6f, 17f)
            close()
          }
        }
        .build()
    return _Transportation!!
  }

private var _Transportation: ImageVector? = null