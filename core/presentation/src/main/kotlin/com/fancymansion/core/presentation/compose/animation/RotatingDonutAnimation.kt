package com.fancymansion.core.presentation.compose.animation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp

@Composable
fun RotatingDonutAnimation(
    modifier: Modifier = Modifier,
    indicatorSize: Float = 30f,
    strokeWidth: Float = 3f,
    color: Color = MaterialTheme.colorScheme.primary
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing)
        ), label = ""
    )

    Box(
        modifier = modifier
            .size(indicatorSize.dp)
            .drawBehind {
                drawRotatingCircularProgress(rotation, color, strokeWidth)
            }
    )
}

private fun DrawScope.drawRotatingCircularProgress(
    rotation: Float,
    color: Color,
    strokeWidth: Float
) {
    val sweepAngle = 270f
    val center = Offset(size.width / 2, size.height / 2)
    val radius = (size.minDimension - strokeWidth.dp.toPx()) / 2

    rotate(rotation, center) {
        drawArc(
            color = color,
            startAngle = 0f,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
            style = Stroke(width = strokeWidth.dp.toPx(), cap = StrokeCap.Square)
        )
    }
}