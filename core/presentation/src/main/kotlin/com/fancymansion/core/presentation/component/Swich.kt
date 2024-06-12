package com.fancymansion.core.presentation.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fancymansion.core.presentation.base.clickSingle
import com.fancymansion.core.presentation.theme.ColorSet
import com.fancymansion.core.presentation.theme.FancyMansionTheme

@Composable
fun BasicSwitch(
    height: Dp = 18.dp,
    switchOutSidePadding : Dp = 7.dp,
    checkedColor : Color = ColorSet.blue_20b1f9,
    uncheckedColor : Color = ColorSet.gray_cecece,
    thumbColor: Color = Color.White,
    thumbPadding : Dp = 1.dp,
    isSwitchOn: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSwitchOn) checkedColor else uncheckedColor,
        animationSpec = tween(durationMillis = 200),
        label = "background color"
    )
    val thumbSpaceRatio by animateFloatAsState(
        targetValue = if (isSwitchOn) 0.5f else 0f,
        animationSpec = tween(durationMillis = 200),
        label = "thumb animation"
    )
    Box(
        modifier = Modifier
            .padding(switchOutSidePadding)
            .height(height)
            .aspectRatio(1.9f / 1f)
            .clip(RoundedCornerShape(height))
            .background(backgroundColor)
            .clickSingle {
                onCheckedChange(!isSwitchOn)
            }
    ) {
        Row(
            modifier = Modifier
                .padding(thumbPadding)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.fillMaxWidth(thumbSpaceRatio))
            Box(modifier = Modifier
                .size(height-1.dp)
                .clip(RoundedCornerShape(height))
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
        BasicSwitch(isSwitchOn = true, onCheckedChange = {})
    }
}