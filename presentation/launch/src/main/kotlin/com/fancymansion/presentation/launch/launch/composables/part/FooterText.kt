package com.fancymansion.presentation.launch.launch.composables.part

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp

@Composable
fun FooterText(
    modifier: Modifier = Modifier,
    text: String
) {
    val createdByFont = remember {
        FontFamily(Font(com.fancymansion.core.presentation.R.font.comfortaa_medium))
    }

    Text(
        text = text,
        modifier = modifier,
        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
        fontFamily = createdByFont,
        fontSize = MaterialTheme.typography.bodySmall.fontSize,
        letterSpacing = 0.5.sp
    )
}