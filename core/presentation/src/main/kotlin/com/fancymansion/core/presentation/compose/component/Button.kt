package com.fancymansion.core.presentation.compose.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fancymansion.core.presentation.R
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.modifier.scaleOnPress
import com.fancymansion.core.presentation.compose.theme.onSurfaceInactive

@Preview
@Composable
fun FlexibleImageButton(
    modifier : Modifier = Modifier,
    contentPadding : PaddingValues = ButtonDefaults.ContentPadding,
    imageClickable : Painter = painterResource(id = R.drawable.img_longest_color_blue_btn),
    imageNotClickable : Painter? = null,

    text : String? = null,
    textStyle : TextStyle = MaterialTheme.typography.titleLarge,
    textOverflow : TextOverflow = TextOverflow.Clip,
    textMaxLines : Int = Int.MAX_VALUE,
    textColor : Color = MaterialTheme.colorScheme.onPrimary,

    pressScale : Float = 1.0f,
    isClickable : Boolean = true,
    clickInterval : Int = 600,
    onClick : () -> Unit = {},
) {
    val buttonImage : Painter = when (isClickable) {
        true -> imageClickable
        false -> imageNotClickable?:imageClickable
    }
    val buttonInteractionSource = remember { MutableInteractionSource() }
    val buttonEffect = if (pressScale < 1.0f) Modifier.scaleOnPress(
        interactionSource = buttonInteractionSource,
        pressScale = pressScale
    ) else Modifier

    val imageIntrinsicSize = buttonImage.intrinsicSize
    BoxWithConstraints(
        modifier = modifier
            .background(color = Color.Transparent)
            .clickSingle(
                enabled = isClickable,
                role = Role.Button,
                clickInterval = clickInterval,
                interactionSource = buttonInteractionSource,
                onClick = { onClick() }
            ).then(buttonEffect)
    ) {

        val fillBoundGroup = Modifier.fillMaxSize() to ContentScale.FillBounds
        val fillHeightGroup = Modifier.fillMaxHeight() to ContentScale.FillHeight
        val fillWidthGroup = Modifier.fillMaxWidth() to ContentScale.FillWidth

        val heightDp = (imageIntrinsicSize.height / LocalDensity.current.density).dp
        val widthDp = (imageIntrinsicSize.width / LocalDensity.current.density).dp

        val minScaledWidth = widthDp * (minHeight / heightDp)
        val minScaledHeight = heightDp * (minWidth / widthDp)
        val maxScaledHeight = heightDp * (maxWidth / widthDp)

        val (imageModifier, contentScale) = when{
            // 높이, 너비가 정해짐
            minHeight > 0.dp && minWidth > 0.dp -> fillBoundGroup
            // 높이만 정해짐. 높이에 맞춘 너비와 가능한 너비 비교
            minHeight > 0.dp -> if(maxWidth  <  minScaledWidth)  fillBoundGroup else fillHeightGroup
            // 너비만 정해짐. 너비에 맞춘 높이와 가능한 높이 비교
            minWidth  > 0.dp -> if(maxHeight <  minScaledHeight) fillBoundGroup else fillWidthGroup

            // 높이, 너비 모두 크기 초과. 축소 하여 맞춤
            maxHeight < heightDp  && maxWidth < widthDp -> if(maxHeight < maxScaledHeight) fillHeightGroup else fillWidthGroup
            // 높이만 초과. 높이에 맞춤
            maxHeight < heightDp        -> fillHeightGroup
            // 너비만 초과. 너비에 맞춤
            maxWidth  < widthDp         -> fillWidthGroup
            // 이미지 맞춤.
            else -> Modifier.wrapContentSize() to ContentScale.None
        }

        Image(
            modifier = Modifier
                .align(Alignment.Center)
                .then(imageModifier),
            painter = buttonImage,
            contentScale = contentScale,
            contentDescription = null
        )

        text?.also {
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(contentPadding),
                text = it,
                color = textColor,
                style = textStyle,
                overflow = textOverflow,
                maxLines = textMaxLines
            )
        }
    }
}

@Composable
fun DefaultButton(
    modifier: Modifier = Modifier,
    buttonPadding: PaddingValues = ButtonDefaults.ContentPadding,
    arrangementTextAndIcon: Arrangement.Horizontal = Arrangement.Center,

    colorBackground: Color = MaterialTheme.colorScheme.primary,
    colorText : Color = MaterialTheme.colorScheme.onPrimary,

    text: String? = null,
    textStyle: TextStyle = MaterialTheme.typography.titleSmall,
    textOverflow: TextOverflow = TextOverflow.Clip,
    textMaxLines: Int = Int.MAX_VALUE,

    iconStart: Int? = null,
    iconEnd: Int? = null,
    iconPadding: Dp = 0.dp,
    iconColor: Color? = null,

    isClickable: Boolean = true,
    clickInterval: Int = 600,
    onClick: () -> Unit,
) {
    val (backgroundColor, textColor) = if (isClickable) {
        (colorBackground to colorText)
    } else {
        (MaterialTheme.colorScheme.surfaceVariant to onSurfaceInactive)
    }

    Row(
        modifier = modifier
            .background(color = backgroundColor)
            .height(IntrinsicSize.Min)
            .width(IntrinsicSize.Max)
            .clickSingle(
                enabled = isClickable,
                role = Role.Button,
                clickInterval = clickInterval,
                onClick = { onClick() }
            )
            .padding(buttonPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = arrangementTextAndIcon,
    ) {
        iconStart?.also {
            Icon(
                modifier = Modifier.fillMaxHeight().padding(end = iconPadding),
                painter = painterResource(id = it),
                tint = iconColor ?: textColor,
                contentDescription = null
            )
        }
        text?.also {
            Text(
                modifier = if (textOverflow != TextOverflow.Clip) Modifier.weight(1f) else Modifier.wrapContentWidth(),
                text = it,
                color = textColor,
                style = textStyle,
                overflow = textOverflow,
                maxLines = textMaxLines,
            )
        }
        iconEnd?.also {
            Icon(
                modifier = Modifier.fillMaxHeight().padding(start = iconPadding),
                painter = painterResource(id = it),
                tint = iconColor ?: textColor,
                contentDescription = null
            )
        }
    }
}