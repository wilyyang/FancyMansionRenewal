package com.fancymansion.core.presentation.compose.modifier

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalDensity

@Composable
fun Modifier.customImePadding(): Modifier = composed {
    Modifier.padding(
        bottom = with(LocalDensity.current) {
            val imePadding = WindowInsets.ime.getBottom(this) - WindowInsets.navigationBars.getBottom(this)
            (if (imePadding > 0) imePadding else 0).toDp()
        }
    )
}