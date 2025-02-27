package com.fancymansion.core.presentation.compose.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.fancymansion.core.presentation.R

@Composable
fun <T : Enum<T>> EnumDropdown(
    modifier: Modifier = Modifier,
    options: Array<T>,
    selectedOption: T,

    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    textHorizontalPadding: Dp = 16.dp,
    textVerticalPadding: Dp = 8.dp,

    borderColor: Color = MaterialTheme.colorScheme.outline,
    borderShape: Shape = MaterialTheme.shapes.small,

    dropdownOffset: DpOffset = DpOffset(x = 0.dp, y = 0.dp),
    dropdownBackgroundColor: Color = MaterialTheme.colorScheme.surface,

    getDisplayName: (T) -> String,
    onItemSelected: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = borderShape
                )
                .padding(0.5.dp)
                .clip(borderShape)
                .clickable { expanded = true }
                .padding(horizontal = textHorizontalPadding, vertical = textVerticalPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                modifier = Modifier.weight(1f),
                text = getDisplayName(selectedOption),
                style = textStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Image(
                modifier = Modifier
                    .height(textStyle.lineHeight.value.dp)
                    .padding(top = 4.dp)
                    .graphicsLayer(rotationZ = 180f),
                painter = painterResource(id = R.drawable.ic_triangle),
                contentScale = ContentScale.FillHeight,
                contentDescription = null
            )
        }


        DropdownMenu(
            expanded = expanded,
            offset = dropdownOffset,
            onDismissRequest = { expanded = false },
            properties = PopupProperties(focusable = true),
            containerColor = Color.Transparent,
            tonalElevation = 0.dp,
            shadowElevation = 0.dp,
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = borderShape
                )
                .padding(0.5.dp)
                .clip(borderShape)
                .background(dropdownBackgroundColor),
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = getDisplayName(option),
                            style = textStyle.copy(fontWeight = if (option == selectedOption) FontWeight.Bold else FontWeight.Normal)
                        )
                    },
                    contentPadding = PaddingValues(
                        vertical = textVerticalPadding,
                        horizontal = textHorizontalPadding
                    ),
                    onClick = {
                        onItemSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}