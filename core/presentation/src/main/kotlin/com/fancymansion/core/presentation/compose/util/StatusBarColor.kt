package com.fancymansion.core.presentation.compose.util

import android.app.Activity
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat

@Composable
fun View.ChangeStatusBarColor(
    color : Color,
    isStatusBarTextDark : Boolean = true
) {
    //새로운 뷰고 하얀색으로 변경되어야 할 경우 이전 컬러와 동일하기 때문에, beforeColor를 선택하지 않을 색으로 지정
    val beforeColor = remember { mutableStateOf(Color.Gray) }
    if(beforeColor.value==color) return

    this.apply {
        if(this.isInEditMode) return
        SideEffect {
            (this.context as Activity).window.let {
                it.statusBarColor = color.toArgb()
                WindowCompat.getInsetsController(it, this)
                    .isAppearanceLightStatusBars = isStatusBarTextDark

                if (color == Color.Transparent) {
                    WindowCompat.setDecorFitsSystemWindows(it, false)
                } else {
                    WindowCompat.setDecorFitsSystemWindows(it, true)
                }
            }
        }
    }

    beforeColor.value = color
}