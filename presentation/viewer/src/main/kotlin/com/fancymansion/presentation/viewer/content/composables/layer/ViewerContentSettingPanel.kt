package com.fancymansion.presentation.viewer.content.composables.layer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.fancymansion.core.presentation.base.clickSingle
import com.fancymansion.core.presentation.frame.topBarDpMobile
import com.fancymansion.core.presentation.theme.onSurfaceDimmed
import com.fancymansion.core.presentation.util.borderLine

@Composable
fun ViewerContentSettingPanel(
    modifier: Modifier,
    visible: Boolean,

) {
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
                    modifier = Modifier.fillMaxHeight().align(Alignment.CenterStart),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "배트의 모험 - 1. 먹이 사냥은 매우 어려워!",
                        style = MaterialTheme.typography.titleLarge.copy(color = onSurfaceDimmed)
                    )
                    Icon(
                        modifier = Modifier.size(40.dp),
                        painter = painterResource(id = com.fancymansion.core.presentation.R.drawable.ic_back),
                        contentDescription = "Back"
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