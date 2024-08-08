package com.fancymansion.presentation.viewer.content.composables.layer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.fancymansion.core.presentation.base.clickSingle
import com.fancymansion.core.presentation.base.scaleOnPress
import com.fancymansion.core.presentation.frame.topBarDpMobile
import com.fancymansion.core.presentation.theme.onSurfaceDimmed
import com.fancymansion.core.presentation.util.borderLine
import com.fancymansion.domain.model.book.PageSettingModel
import com.fancymansion.presentation.viewer.content.ViewerContentContract

@Composable
fun ViewerContentSettingPanel(
    modifier: Modifier,
    visible: Boolean,
    title: String,
    setting: PageSettingModel,
    onEventSent: (event: ViewerContentContract.Event) -> Unit,
    onClickBack:() -> Unit
) {
    val backInteractionSource = remember { MutableInteractionSource() }
    Box(modifier = modifier) {
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.TopStart),
            visible = visible,
            enter = slideInVertically { -it },
            exit = slideOutVertically { -it },
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(topBarDpMobile)
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .borderLine(
                        density = LocalDensity.current,
                        color = MaterialTheme.colorScheme.outline,
                        bottom = 1.dp
                    )
                    .clickSingle { }
                ) {

                Row(
                    modifier = Modifier.fillMaxHeight().align(Alignment.CenterStart).padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.fillMaxHeight().clickSingle(
                            onClick = onClickBack,
                            interactionSource = backInteractionSource
                        ).scaleOnPress(
                            interactionSource = backInteractionSource,
                            pressScale = 0.9f
                        ),
                        painter = painterResource(id = com.fancymansion.core.presentation.R.drawable.ic_back),
                        tint = onSurfaceDimmed,
                        contentDescription = "Back"
                    )
                    Text(
                        modifier = Modifier.padding(horizontal = 5.dp),
                        text = title,
                        style = MaterialTheme.typography.titleLarge.copy(color = onSurfaceDimmed),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            }
        }


        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.BottomStart),
            visible = visible,
            enter = slideInVertically { it },
            exit = slideOutVertically { it },
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .borderLine(
                        density = LocalDensity.current,
                        color = MaterialTheme.colorScheme.outline,
                        top = 1.dp
                    )
                    .clickSingle { }) {

            }
        }
    }
}