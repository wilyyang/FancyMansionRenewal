package com.fancymansion.presentation.launch.launch.composables.part.logo

import androidx.compose.runtime.State
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun rememberLaunchLogoAnimation(
    fullText: String,
    step: Int,
    pulseKey: Int
): LaunchLogoAnimState {

    val visibleText = fullText.take(step)

    val startSize = 60.sp
    val stepDown = 3.5.sp
    val minSize = 32.sp

    val rawSize =
        (startSize.value - (stepDown * (step - 1).coerceAtLeast(0)).value).sp

    val targetFontSize =
        if (rawSize.value < minSize.value) minSize else rawSize

    val fontSize by animateTextUnitAsState(
        targetValue = targetFontSize,
        animationSpec = tween(70, easing = FastOutSlowInEasing),
        label = "fontSize"
    )

    val pushAnim by rememberPushAnim(pulseKey)
    val popScale by rememberPopScale(pulseKey)

    return LaunchLogoAnimState(
        visibleText = visibleText,
        fontSize = fontSize,
        translationX = pushAnim,
        scale = popScale
    )
}

@Composable
fun rememberPushAnim(pulseKey: Int): State<Float>{
    val target = pulseKey
    val anim = remember { androidx.compose.animation.core.Animatable(0f) }
    LaunchedEffect(target) {
        if (target == 0) return@LaunchedEffect anim.snapTo(0f)
        anim.animateTo(-10f, tween(durationMillis = 70, easing = FastOutSlowInEasing))
        anim.animateTo(0f, tween(durationMillis = 90, easing = FastOutSlowInEasing))
    }
    return rememberUpdatedState(anim.value)
}

@Composable
fun rememberPopScale(pulseKey: Int): State<Float> {
    val target = pulseKey
    val anim = remember { androidx.compose.animation.core.Animatable(1f) }
    LaunchedEffect(target) {
        if (target == 0) return@LaunchedEffect anim.snapTo(1f)
        anim.animateTo(1.05f, tween(durationMillis = 70, easing = FastOutSlowInEasing))
        anim.animateTo(1.00f, tween(durationMillis = 90, easing = FastOutSlowInEasing))
    }
    return rememberUpdatedState(anim.value)
}

@Composable
fun animateTextUnitAsState(
    targetValue: TextUnit,
    animationSpec: AnimationSpec<Float>,
    label: String
): State<TextUnit> {
    val animated by animateFloatAsState(
        targetValue = targetValue.value,
        animationSpec = animationSpec,
        label = label
    )
    return rememberUpdatedState(animated.sp)
}