package com.fancymansion.core.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.fancymansion.core.presentation.theme.disableAlpha

@Composable
fun DropDown(
    modifier: Modifier = Modifier,
    contentPadding : PaddingValues = PaddingValues(0.dp),
    backgroundColor : Color = MaterialTheme.colorScheme.surface,

    list : List<Pair<String, String>>,
    textStyle : TextStyle = MaterialTheme.typography.bodyMedium,
    textColor : Color = MaterialTheme.colorScheme.onSurface,

    isClickable : Boolean = true,
    selectedValue: Pair<String, String>? = null,
    onClick: (Pair<String, String>) -> Unit,
) {
    val (finalBackgroundColor, finalTextColor) = when (isClickable) {
        true -> backgroundColor to textColor
        false -> backgroundColor.copy(alpha = disableAlpha) to textColor.copy(alpha = disableAlpha)
    }

    var dropDownWidth by remember { mutableStateOf(0) }
    var expanded by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = modifier
    ) {
        DefaultButton(
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged { dropDownWidth = it.width },
            buttonPadding = contentPadding,
            text = selectedValue?.second,
            textStyle = textStyle,
            textOverflow = TextOverflow.Ellipsis,
            textMaxLines = 1,
            isClickable = isClickable,
            colorBackground = finalBackgroundColor,
            colorText = finalTextColor
        ) {
            expanded = !expanded
        }

        DropdownMenu(
            modifier = Modifier
                .width(with(LocalDensity.current) { dropDownWidth.toDp() })
                .background(color = backgroundColor),
            expanded = expanded,
            onDismissRequest = { expanded = false },
            properties = PopupProperties(focusable = false)
        ) {
            list.forEach {

                DropdownMenuItem(
                    text = { Text(text = it.second, style = textStyle, color = textColor, modifier = Modifier.fillMaxWidth())
                    },
                    onClick = {
                        expanded = false
                        onClick.invoke(it)
                    }
                )
            }
        }
    }
}