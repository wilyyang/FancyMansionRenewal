package com.fancymansion.core.presentation.compose.dialog

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.fancymansion.core.common.const.DELAY_LOADING_SHOW_MS
import com.fancymansion.core.presentation.compose.theme.ColorSet
import com.fancymansion.core.presentation.compose.theme.DIMMED_ALPHA
import kotlinx.coroutines.delay

@Composable
fun Loading(
    onDismiss: () -> Unit = {},
    loadingMessage : String? = null
) {
    val isShowAnimation = remember{mutableStateOf(false) }
    LaunchedEffect(key1 = Unit, block = {
        delay(DELAY_LOADING_SHOW_MS)
        isShowAnimation.value = true
    })
    Surface(modifier = Modifier.fillMaxSize(), color = Color.Transparent) {
        if(isShowAnimation.value){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = DIMMED_ALPHA))
            ) {
                Popup(
                    onDismissRequest = onDismiss
                ){
                    Box (
                        modifier = Modifier.semantics { contentDescription = "Loading"}.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularLoadingAnimation()
                            loadingMessage?.let {
                                Spacer(Modifier.height(20.dp))
                                Text(text = loadingMessage,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.surface)
                            }
                        }
                    }
                }
            }

        }
    }
}

class CircleAnim(val easing : CubicBezierEasing, val radius : Float, val color : Color)

@Composable
fun CircularLoadingAnimation() {
    val durationMillis = 1400
    val circleAnims = listOf(
        CircleAnim(easing = CubicBezierEasing(0.44f, 0.0f, 0.9f, 1.0f), radius = 3f, color = ColorSet.loading_gray_1),
        CircleAnim(easing = CubicBezierEasing(0.36f, 0.0f, 0.8f, 1.0f), radius = 5f, color = ColorSet.loading_gray_2),
        CircleAnim(easing = CubicBezierEasing(0.28f, 0.0f, 0.7f, 1.0f), radius = 7f, color = ColorSet.loading_gray_3),
        CircleAnim(easing = CubicBezierEasing(0.2f,  0.0f, 0.6f, 1.0f), radius = 10f, color = ColorSet.loading_gray_4),
        CircleAnim(easing = CubicBezierEasing(0.12f, 0.0f, 0.5f, 1.0f), radius = 13f, color = ColorSet.loading_gray_5),
        CircleAnim(easing = CubicBezierEasing(0.04f, 0.0f, 0.4f, 1.0f), radius = 16f, color = ColorSet.loading_gray_6),
    )

    val animationAngles = circleAnims.map {
        val infiniteTransition = rememberInfiniteTransition(label = "")
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