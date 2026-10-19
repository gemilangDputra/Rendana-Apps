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
public val Wallet: ImageVector
  get() {
    if (_Wallet != null) {
      return _Wallet!!
    }
    _Wallet =
      ImageVector.Builder(
          name = "Wallet",
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
            moveTo(6f, 20f)
            quadTo(4.35f, 20f, 3.18f, 18.83f)
            reflectiveQuadTo(2f, 16f)
            verticalLineTo(8f)
            quadTo(2f, 6.35f, 3.18f, 5.18f)
            reflectiveQuadTo(6f, 4f)
            horizontalLineTo(18f)
            quadToRelative(1.65f, 0f, 2.83f, 1.18f)
            reflectiveQuadTo(22f, 8f)
            verticalLineToRelative(8f)
            quadToRelative(0f, 1.65f, -1.17f, 2.82f)
            reflectiveQuadTo(18f, 20f)
            horizontalLineTo(6f)
            close()
            moveTo(6f, 8f)
            horizontalLineTo(18f)
            quadToRelative(0.55f, 0f, 1.05f, 0.13f)
            reflectiveQuadTo(20f, 8.52f)
            verticalLineTo(8f)
            quadTo(20f, 7.18f, 19.41f, 6.59f)
            reflectiveQuadTo(18f, 6f)
            horizontalLineTo(6f)
            quadTo(5.18f, 6f, 4.59f, 6.59f)
            quadTo(4f, 7.18f, 4f, 8f)
            verticalLineTo(8.52f)
            quadTo(4.45f, 8.25f, 4.95f, 8.13f)
            reflectiveQuadTo(6f, 8f)
            close()
            moveTo(4.15f, 11.25f)
            lineToRelative(11.13f, 2.7f)
            quadToRelative(0.22f, 0.05f, 0.45f, 0f)
            reflectiveQuadToRelative(0.43f, -0.2f)
            lineToRelative(3.47f, -2.9f)
            quadToRelative(-0.27f, -0.38f, -0.7f, -0.61f)
            reflectiveQuadTo(18f, 10f)
            horizontalLineTo(6f)
            quadTo(5.35f, 10f, 4.86f, 10.34f)
            quadTo(4.38f, 10.68f, 4.15f, 11.25f)
            close()
          }
        }
        .build()
    return _Wallet!!
  }

private var _Wallet: ImageVector? = null