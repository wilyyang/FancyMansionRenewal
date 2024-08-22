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
import com.fancymansion.core.presentation.window.TypePane

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
    onClickLeftIcon: (() -> Unit) = { },

    idRightIcon: Int? = null,
    idRightIconRatio: Float? = null,
    onClickRightIcon: (() -> Unit) = { },

    topBarColor : Color? = Color.Transparent,
    topBarImage : Int? = null,
    shadowElevation: Dp = 0.dp
) {
    val topBarHeight = if (typePane == TypePane.SINGLE) topBarDpMobile else topBarDpTablet
    val topBarVerticalPadding = if (typePane == TypePane.SINGLE) 6.6.dp else 10.dp
    val topBarHorizontalPadding = if (typePane == TypePane.SINGLE) 12.6.dp else 18.dp
    val titleStyle = if (typePane == TypePane.SINGLE) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.headlineMedium
    val subTitleStyle = if (typePane == TypePane.SINGLE) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodyLarge
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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = topBarHorizontalPadding)
        ) {
            idLeftIcon?.also {
                val leftIconInteractionSource = remember { MutableInteractionSource() }
                Image(
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
                    painter = painterResource(id = it),
                    contentDescription = null,
                    contentScale = ContentScale.FillHeight
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
                    .fillMaxWidth(if(typePane== TypePane.SINGLE) 0.67f else 0.93f),
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

            idRightIcon?.also {
                val rightIconInteractionSource = remember { MutableInteractionSource() }
                Image(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .fillMaxHeight(idRightIconRatio?:1f)
                        .padding(vertical = if(idRightIconRatio != null) topBarVerticalPadding/2 else topBarVerticalPadding)
                        .clickSingle(
                            onClick = onClickRightIcon,
                            interactionSource = rightIconInteractionSource
                        )
                        .scaleOnPress(
                            interactionSource = rightIconInteractionSource,
                            pressScale = 0.9f
                        ),
                    painter = painterResource(id = it),
                    contentDescription = null,
                    contentScale = ContentScale.FillHeight
                )
            }
        }
    }
}