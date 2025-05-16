package com.fancymansion.presentation.editor.common.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fancymansion.core.presentation.R

@Composable
fun SelectItem(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    textHorizontalPadding: Dp = 16.dp,
    textVerticalPadding: Dp = 8.dp,
    borderColor: Color = MaterialTheme.colorScheme.outline,
    borderShape: Shape = MaterialTheme.shapes.small,
    itemText: String,
    onClickItemText: () -> Unit
){
    Row(
        modifier = modifier
            .border(
                width = 1.dp,
                color = borderColor,
                shape = borderShape
            )
            .padding(0.5.dp)
            .clip(borderShape)
            .clickable{
                onClickItemText()
            }
            .padding(horizontal = textHorizontalPadding, vertical = textVerticalPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            modifier = Modifier.weight(1f),
            text = itemText,
            style = textStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Icon(
            modifier = Modifier
                .height(textStyle.lineHeight.value.dp),
            painter = painterResource(id = R.drawable.ic_check_bold_600),
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = null
        )
    }
}