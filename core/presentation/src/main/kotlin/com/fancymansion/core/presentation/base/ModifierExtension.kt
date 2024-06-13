package com.fancymansion.core.presentation.base

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.scaleOnPress(
    interactionSource: InteractionSource,
    pressScale : Float = 0.95f
) = composed {
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        if (isPressed) {
            pressScale
        } else {
            1f
        }, label = ""
    )
    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
}

/**
 * pointerInput : Modifier 영역 내의 입력을 처리하기 위함 (PointerInputScope 블록을 생성함)
 * detectTapGestures : 탭 관련 동작 감지용
 * focusManager.clearFocus() : 전달 받은 포커스매니저를 통해 포커스 해제
 */
fun Modifier.addFocusCleaner(focusManager: FocusManager) = pointerInput(Unit) {
    this.detectTapGestures {
        focusManager.clearFocus()
    }
}