package com.fancymansion.core.presentation.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.fancymansion.core.presentation.theme.dimmedAlpha

@Composable
fun CustomDialog(
    customDialog : @Composable () -> Unit
) {
    ScreenPopup()
    {
        Box (
            modifier = Modifier
                .semantics {
                    contentDescription = "CustomDialog"
                }
                .fillMaxSize()
                .background(Color.Black.copy(alpha = dimmedAlpha)),
            contentAlignment = Alignment.Center
        ){
            customDialog()
        }
    }
}