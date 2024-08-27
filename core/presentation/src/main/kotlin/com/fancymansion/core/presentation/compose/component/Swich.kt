package com.fancymansion.core.presentation.compose.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.shape.RoundedRectangleShape
import com.fancymansion.core.presentation.compose.theme.FancyMansionTheme

@Composable
fun BasicSwitch(
    modifier: Modifier,

    checkedColor : Color = MaterialTheme.colorScheme.primary,
    uncheckedColor : Color = MaterialTheme.colorScheme.outline,

    thumbColor: Color = Color.White,
    thumbPadding : Dp = 1.dp,

    checked: Boolean,
    onClickChecked: (Boolean) -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (checked) checkedColor else uncheckedColor,
        animationSpec = tween(durationMillis = 200),
        label = "background color"
    )
    val thumbSpaceRatio by animateFloatAsState(
        targetValue = if (checked) 0.5f else 0f,
        animationSpec = tween(durationMillis = 200),
        label = "thumb animation"
    )

    Box(
        modifier = modifier
            .aspectRatio(1.9f / 1f)
            .clip(shape = RoundedRectangleShape())
            .background(backgroundColor)
            .clickSingle {
                onClickChecked(!checked)
            }
    ) {
        Row(
            modifier = Modifier
                .padding(thumbPadding)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.fillMaxWidth(thumbSpaceRatio))
            Box(modifier = Modifier
                .fillMaxHeight().aspectRatio(1f)
                .clip(CircleShape)
                .background(thumbColor)
            )
        }
    }
}

@Preview
@Composable
fun SettingScreenPreview(
) {
    FancyMansionTheme {
        BasicSwitch(modifier = Modifier.height(30.dp), checked = true, onClickChecked = {})
    }
}