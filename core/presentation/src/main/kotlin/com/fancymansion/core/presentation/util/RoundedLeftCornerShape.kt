package com.fancymansion.core.presentation.util

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Stable
fun Modifier.borderLine(density: Density, color: Color, top: Dp? = null, bottom: Dp? = null, left: Dp? = null, right: Dp? = null): Modifier {
    var modifier = this
    if (top != null) {
        modifier = modifier.border(
            width = top,
            color = color,
            shape = GenericShape { size, _ ->
                moveTo(0f, 0f)
                lineTo(size.width, 0f)
                lineTo(size.width, with(density) { top.toPx() })
                lineTo(0f, with(density) { top.toPx() })
            }
        )
    }
    if (bottom != null) {
        modifier = modifier.border(
            width = bottom,
            color = color,
            shape = GenericShape { size, _ ->
                moveTo(0f, size.height)
                lineTo(size.width, size.height)
                lineTo(size.width, size.height - with(density) { bottom.toPx() })
                lineTo(0f, size.height - with(density) { bottom.toPx() })
            }
        )
    }
    if (left != null) {
        modifier = modifier.border(
            width = left,
            color = color,
            shape = GenericShape { size, _ ->
                moveTo(0f, 0f)
                lineTo(0f, size.height)
                lineTo(with(density) { left.toPx() }, size.height)
                lineTo(with(density) { left.toPx() }, 0f)
            }
        )
    }
    if (right != null) {
        modifier = modifier.border(
            width = right,
            color = color,
            shape = GenericShape { size, _ ->
                moveTo(size.width, 0f)
                lineTo(size.width, size.height)
                lineTo(size.width - with(density) { right.toPx() }, size.height)
                lineTo(size.width - with(density) { right.toPx() }, 0f)
            }
        )
    }
    return modifier
}

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