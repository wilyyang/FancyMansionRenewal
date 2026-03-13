package com.fancymansion.presentation.launch.launch.composables.part.logo

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun LaunchLogoText(
    fullText: String,
    isAnimationStart: Boolean,
    onAnimationEnd: () -> Unit
) {
    val logoFont = remember {
        FontFamily(Font(com.fancymansion.core.presentation.R.font.comfortaa_semibold))
    }

    var step by remember { mutableIntStateOf(0) }
    var pulseKey by remember { mutableIntStateOf(0) }

    val stepDelayMs = 140L

    LaunchedEffect(isAnimationStart) {

        if (!isAnimationStart) {
            step = fullText.length
            pulseKey = 0
            return@LaunchedEffect
        }

        step = 0

        for (i in 1..fullText.length) {
            step = i
            pulseKey++
            delay(stepDelayMs)
        }
        onAnimationEnd()
    }

    val logoAnim = rememberLaunchLogoAnimation(
        fullText = fullText,
        step = step,
        pulseKey = pulseKey
    )

    Text(
        text = logoAnim.visibleText,
        modifier = Modifier.graphicsLayer {
            this.translationX = translationX
            scaleX = logoAnim.scale
            scaleY = logoAnim.scale
        },
        color = Color.White,
        fontFamily = logoFont,
        fontSize = logoAnim.fontSize,
        letterSpacing = 0.5.sp
    )
}