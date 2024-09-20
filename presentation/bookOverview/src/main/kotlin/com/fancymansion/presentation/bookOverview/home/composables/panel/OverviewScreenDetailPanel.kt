package com.fancymansion.presentation.bookOverview.home.composables.panel

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.fancymansion.domain.model.book.BookInfoModel
import com.fancymansion.presentation.bookOverview.home.OverviewHomeContract

@Composable
fun OverviewScreenDetailPanel(
    modifier: Modifier,
    bookInfo: BookInfoModel,
    onEventSent: (event: OverviewHomeContract.Event) -> Unit
) {
    val listState = rememberLazyListState()
    val screenHeight = LocalConfiguration.current.screenHeightDp
    var panelHeight by remember { mutableFloatStateOf(0.7f) }
    val animatedPanelHeight by animateDpAsState(
        targetValue = (screenHeight * panelHeight).dp,
        animationSpec = spring(),
        label = ""
    )

    Box(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .height(animatedPanelHeight)
                .background(Color.LightGray)
        ) {
            LazyColumn(
                modifier = modifier.fillMaxWidth().fillMaxHeight().background(color = Color.LightGray),
                state = listState
            ) {
                item {
                    Column {
                        Text(
                            modifier = Modifier
                                .draggable(
                                    orientation = Orientation.Vertical,
                                    state = rememberDraggableState { delta ->
                                        panelHeight = maxOf(0.7f, minOf(1f, panelHeight - delta / screenHeight))
                                    },
                                    onDragStopped = {
                                        panelHeight = if (panelHeight > 0.85f) 1f else 0.7f
                                    }
                                ),
                            text = bookInfo.id
                        )

                        Text(
                            text = bookInfo.editor.editorName
                        )

                        Text(
                            text = bookInfo.introduce.description
                        )

                        Spacer(modifier = Modifier.height(30.dp))

                        Text(
                            text = bookInfo.introduce.description,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}