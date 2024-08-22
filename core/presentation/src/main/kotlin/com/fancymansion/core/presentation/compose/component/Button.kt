package com.fancymansion.core.presentation.compose.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import com.fancymansion.core.presentation.compose.theme.ColorSet

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

    buttonImage.intrinsicSize
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
        val heightDp = (buttonImage.intrinsicSize.height / LocalDensity.current.density).dp
        val widthDp = (buttonImage.intrinsicSize.width / LocalDensity.current.density).dp

        val (imageModifier, contentScale) = if(minHeight > 0.0.dp && minWidth > 0.0.dp){
            Pair(Modifier.fillMaxSize(), ContentScale.FillBounds)
        }else if(minHeight > 0.0.dp){
            val adaptWidthDp = widthDp * (minHeight / heightDp)
            if(adaptWidthDp > maxWidth){
                Pair(Modifier.fillMaxSize(), ContentScale.FillBounds)
            }else{
                Pair(Modifier.fillMaxHeight(), ContentScale.FillHeight)
            }
        }else if(minWidth > 0.0.dp){
            val adaptHeightDp = heightDp * (minWidth / widthDp)
            if(adaptHeightDp > maxHeight){
                Pair(Modifier.fillMaxSize(), ContentScale.FillBounds)
            }else{
                Pair(Modifier.fillMaxWidth(), ContentScale.FillWidth)
            }
        }else{
            if(heightDp > maxHeight && widthDp > maxWidth){
                val adaptHeightDp = heightDp * (maxWidth / widthDp)
                if(adaptHeightDp > maxHeight){
                    Pair(Modifier.fillMaxHeight(), ContentScale.FillHeight)
                }else{
                    Pair(Modifier.fillMaxWidth(), ContentScale.FillWidth)
                }
            }else if(heightDp > maxHeight){
                Pair(Modifier.fillMaxHeight(), ContentScale.FillHeight)
            }else if(widthDp > maxWidth){
                Pair(Modifier.fillMaxWidth(), ContentScale.FillWidth)
            }else{
                Pair(Modifier.wrapContentSize(), ContentScale.None)
            }
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
    text: String? = null,
    textStyle: TextStyle = MaterialTheme.typography.titleSmall,
    textOverflow: TextOverflow = TextOverflow.Clip,
    textMaxLines: Int = Int.MAX_VALUE,
    iconStart: Int? = null,
    iconEnd: Int? = null,
    iconPadding: Dp = 0.dp,
    iconColor: Color? = null,
    isClickable: Boolean = true,
    colorBackground: Color = MaterialTheme.colorScheme.primary,
    colorText : Color = MaterialTheme.colorScheme.onPrimary,
    clickInterval: Int = 600,
    onClick: () -> Unit,
) {
    val (backgroundColor, textColor) = if (isClickable) {
        (colorBackground to colorText)
    } else {
        (ColorSet.gray_9d9d9d to ColorSet.gray_f1f1f1)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
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
        horizontalArrangement = arrangementTextAndIcon,
    ) {
        iconStart?.also {
            Icon(
                painter = painterResource(id = it),
                contentDescription = null,
                tint = iconColor ?: textColor,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
            )
            Spacer(modifier = Modifier.width(iconPadding))
        }
        text?.also {
            Text(
                text = it,
                color = textColor,
                style = textStyle,
                overflow = textOverflow,
                maxLines = textMaxLines,
                modifier = if (textOverflow != TextOverflow.Clip) Modifier.weight(1f) else Modifier.wrapContentWidth()
            )
        }
        iconEnd?.also {
            Spacer(modifier = Modifier.width(iconPadding))
            Icon(
                painter = painterResource(id = it),
                contentDescription = null,
                tint = iconColor ?: textColor,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
            )
        }
    }
}