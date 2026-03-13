package com.fancymansion.presentation.launch.launch.composables.part.logo

import androidx.compose.ui.unit.TextUnit

data class LaunchLogoAnimState(
    val visibleText: String,
    val fontSize: TextUnit,
    val translationX: Float,
    val scale: Float
)