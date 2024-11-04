package com.fancymansion.core.presentation.compose.frame

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.modifier.scaleOnPress
import com.fancymansion.core.presentation.base.window.TypePane

val topBarDpMobile = 50.dp
val topBarDpTablet = 73.dp

@Composable
fun FancyMansionTopBar(
    typePane: TypePane,
    title: String? = null,
    titleColor : Color = MaterialTheme.colorScheme.onSurface,

    subTitle: String? = null,
    subTitleColor : Color = MaterialTheme.colorScheme.onSurface,

    titleImage : Int? = null,
    titleImageVerticalPadding : Dp = 0.dp,

    idLeftIcon: Int? = null,
    sideLeftText: String? = null,
    onClickLeftIcon: (() -> Unit) = { },

    idRightIcon: Int? = null,
    sideRightText: String? = null,
    onClickRightIcon: (() -> Unit) = { },

    topBarColor : Color? = Color.Transparent,
    topBarImage : Int? = null,
    shadowElevation: Dp = 0.dp
) {
    val topBarHeight = if (typePane == TypePane.MOBILE) topBarDpMobile else topBarDpTablet
    val topBarVerticalPadding = 4.dp
    val titleStyle = MaterialTheme.typography.titleLarge
    val subTitleStyle = MaterialTheme.typography.bodyLarge
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(topBarHeight),
        color = topBarColor ?: MaterialTheme.colorScheme.surface,
        shadowElevation = shadowElevation
    ) {
        topBarImage?.also {
            Image(
                modifier = Modifier
                    .fillMaxSize(),
                painter = painterResource(id = it),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
        }

        Box(modifier = Modifier.fillMaxSize()) {
            if(sideLeftText != null) {
                Box(modifier = Modifier
                    .align(Alignment.CenterStart)
                    .fillMaxHeight()
                    .padding(vertical = topBarVerticalPadding)
                    .padding(start = 12.dp)
                    .clickSingle(
                        onClick = onClickLeftIcon
                    )){
                    Text(
                        modifier = Modifier.align(Alignment.CenterStart),
                        text = sideLeftText,
                        style = subTitleStyle
                    )
                }

            }else if(idLeftIcon != null){
                val leftIconInteractionSource = remember { MutableInteractionSource() }
                Icon(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .fillMaxHeight()
                        .padding(vertical = topBarVerticalPadding)
                        .clickSingle(
                            onClick = onClickLeftIcon,
                            interactionSource = leftIconInteractionSource
                        )
                        .scaleOnPress(
                            interactionSource = leftIconInteractionSource,
                            pressScale = 0.9f
                        ),
                    painter = painterResource(id = idLeftIcon),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = "Left Icon"
                )
            }

            titleImage?.let {
                Image(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxHeight()
                        .padding(vertical = titleImageVerticalPadding),
                    painter = painterResource(id = it),
                    contentScale = ContentScale.FillHeight,
                    contentDescription = null
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(if(typePane== TypePane.MOBILE) 0.67f else 0.93f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                subTitle?.let {
                    Text(
                        text = it,
                        style = subTitleStyle,
                        color = subTitleColor,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }

                title?.let {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = it,
                        style = titleStyle,
                        color = titleColor,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        textAlign = TextAlign.Center
                    )
                }
            }

            if(sideRightText != null) {
                Box(modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
                    .padding(vertical = topBarVerticalPadding)
                    .padding(end = 12.dp)
                    .clickSingle(
                        onClick = onClickRightIcon
                    )){
                    Text(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        text = sideRightText,
                        style = subTitleStyle
                    )
                }
            }else if(idRightIcon != null){
                val rightIconInteractionSource = remember { MutableInteractionSource() }
                Icon(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .fillMaxHeight()
                        .padding(vertical = topBarVerticalPadding)
                        .clickSingle(
                            onClick = onClickRightIcon,
                            interactionSource = rightIconInteractionSource
                        )
                        .scaleOnPress(
                            interactionSource = rightIconInteractionSource,
                            pressScale = 0.9f
                        ),
                    painter = painterResource(id = idRightIcon),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = "Right Icon"
                )
            }
        }
    }
}