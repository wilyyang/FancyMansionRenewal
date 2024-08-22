package com.fancymansion.core.presentation.compose.shape

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class RoundedHorizontalCornerShape(
    private val cornerRadius: Dp = 20.dp,
    private val isLeftDirection : Boolean = true
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val cornerRadiusPx = with(density) { cornerRadius.toPx() }

        val path = Path().apply {
            if(isLeftDirection){
                moveTo(size.width, size.height)
                lineTo(cornerRadiusPx, size.height)
                arcTo(
                    rect = Rect(
                        left = 0f,
                        top = 0f,
                        right = cornerRadiusPx,
                        bottom = size.height
                    ),
                    startAngleDegrees = 90f,
                    sweepAngleDegrees = 180f,
                    forceMoveTo = false
                )
                lineTo(size.width, 0f)
            }else{
                moveTo(0f, size.height)
                lineTo(size.width - cornerRadiusPx, size.height)
                arcTo(
                    rect = Rect(
                        left = size.width - cornerRadiusPx,
                        top = 0f,
                        right = size.width,
                        bottom = size.height
                    ),
                    startAngleDegrees = 90f,
                    sweepAngleDegrees = -180f,
                    forceMoveTo = false
                )
                lineTo(0f, 0f)
            }
        }
        return Outline.Generic(path)
    }
}