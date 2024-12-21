package com.fancymansion.presentation.editor.common.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import com.fancymansion.core.presentation.compose.theme.Paddings

@Composable
fun CommonEditInfoTitle(
    modifier: Modifier = Modifier,
    verticalPadding : Dp = Paddings.Basic.vertical,
    title: String,
    style : TextStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
){
    Text(
        modifier = modifier.padding(vertical = verticalPadding),
        text = title,
        style = style
    )
}