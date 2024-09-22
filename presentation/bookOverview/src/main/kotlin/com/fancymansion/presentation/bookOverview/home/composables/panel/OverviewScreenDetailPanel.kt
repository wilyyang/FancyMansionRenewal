package com.fancymansion.presentation.bookOverview.home.composables.panel

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.fancymansion.domain.model.book.BookInfoModel
import com.fancymansion.presentation.bookOverview.home.OverviewHomeContract

@Composable
fun OverviewScreenDetailPanel(
    modifier: Modifier,
    bookInfo: BookInfoModel,
    onHideDetailPanel: () -> Unit,
    onEventSent: (event: OverviewHomeContract.Event) -> Unit
) {
    val density = LocalDensity.current.density
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    val listState = rememberLazyListState()
    var baseHeight by remember { mutableFloatStateOf(screenHeightDp * 0.7f) }

    var targetHeightDp by remember { mutableFloatStateOf(baseHeight) }
    var isDragging by remember { mutableStateOf(false) }

    val panelHeight = remember { Animatable(baseHeight) } // Animatable 사용

    LaunchedEffect(key1 = targetHeightDp) { // targetHeightDp 변경 시 애니메이션 실행
        if (!isDragging) {
            panelHeight.animateTo(targetHeightDp, animationSpec = tween(durationMillis = 300))
        }
    }

    val panelHeightDp = if (isDragging) targetHeightDp else panelHeight.value

    Box(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .height(panelHeightDp.dp)
                .background(Color.LightGray)
        ) {
            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(color = Color.LightGray),
                state = listState
            ) {
                item {
                    Column {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .pointerInput(Unit) {
                                    detectVerticalDragGestures(
                                        onDragStart = { isDragging = true  },
                                        onDragEnd = {
                                            isDragging = false

                                            // panelHeight.snapTo(targetHeightDp)
                                            if(targetHeightDp <= baseHeight){
                                                onHideDetailPanel()
                                            }else{
                                                targetHeightDp = screenHeightDp.toFloat()
                                                baseHeight = screenHeightDp.toFloat()
                                            }
                                        },
                                        onVerticalDrag = { change, dragAmountPx ->
                                            targetHeightDp -= (dragAmountPx / density)
                                            change.consume()
                                        }
                                    )
                                },
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