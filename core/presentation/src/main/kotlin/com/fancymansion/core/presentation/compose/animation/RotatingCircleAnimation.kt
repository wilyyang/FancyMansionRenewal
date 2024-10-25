package com.fancymansion.core.presentation.compose.animation

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp

class CircleAnim(val easing : CubicBezierEasing, val radius : Float, val color : Color)

@Composable
fun RotatingCircleAnimation() {
    val durationMillis = 1400
    val circleAnims = listOf(
        CircleAnim(easing = CubicBezierEasing(0.44f, 0.0f, 0.9f, 1.0f), radius = 3f, color = MaterialTheme.colorScheme.primary),
        CircleAnim(easing = CubicBezierEasing(0.36f, 0.0f, 0.8f, 1.0f), radius = 5f, color = MaterialTheme.colorScheme.primary),
        CircleAnim(easing = CubicBezierEasing(0.28f, 0.0f, 0.7f, 1.0f), radius = 7f, color = MaterialTheme.colorScheme.primary),
        CircleAnim(easing = CubicBezierEasing(0.2f,  0.0f, 0.6f, 1.0f), radius = 10f, color = MaterialTheme.colorScheme.primary),
        CircleAnim(easing = CubicBezierEasing(0.12f, 0.0f, 0.5f, 1.0f), radius = 13f, color = MaterialTheme.colorScheme.primary),
        CircleAnim(easing = CubicBezierEasing(0.04f, 0.0f, 0.4f, 1.0f), radius = 16f, color = MaterialTheme.colorScheme.primary),
    )

    val infiniteTransition = rememberInfiniteTransition(label = "")
    val animationAngles = circleAnims.map {
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = durationMillis,
                    easing = it.easing
                ),
                repeatMode = RepeatMode.Restart
            ), label = ""
        )
    }

    Spacer(modifier = Modifier
        .size(40.dp)
        .drawBehind {
            circleAnims.forEachIndexed { index, circleAnim ->
                val rotationCenter = Offset(size.width / 2, size.height / 2)
                val circleCenter = Offset(size.width / 2, 0f)
                rotate(animationAngles[index].value, pivot = rotationCenter) {
                    drawCircle(
                        color = circleAnim.color,
                        center = circleCenter,
                        radius = circleAnim.radius
                    )
                }
            }
        }
    )
}