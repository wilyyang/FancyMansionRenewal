package com.fancymansion.presentation.bookOverview.home.composables.panel.detail

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.fancymansion.domain.model.book.BookInfoModel
import com.fancymansion.presentation.bookOverview.home.OverviewHomeContract
import com.fancymansion.presentation.bookOverview.home.composables.OverviewPanelState

enum class DetailPanelState(private val ratio: Float) {
    COLLAPSED(0.7f),
    EXPANDED(0.95f);

    fun getBaseScreen(screenHeight:Int) = screenHeight * ratio
}

@Composable
fun OverviewScreenDetailPanel(
    modifier: Modifier,
    key: Any,
    bookInfo: BookInfoModel,
    onHideDetailPanel: () -> Unit,
    onEventSent: (event: OverviewHomeContract.Event) -> Unit
) {
    val density = LocalDensity.current.density
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    val listState = rememberLazyListState()
    var panelState by remember { mutableStateOf(DetailPanelState.COLLAPSED) }

    // Drag 시 적용 높이
    var dragHeightDp by remember { mutableFloatStateOf(panelState.getBaseScreen(screenHeightDp)) }

    // Drag 종료 시 적용 높이
    val dragEndAnimateHeightDp = remember { Animatable(panelState.getBaseScreen(screenHeightDp)) }
    var dragEndEffect by remember { mutableStateOf(false) }
    var isSnapToCurrentHeight by remember { mutableStateOf(false) }

    // Panel 적용 높이
    val panelHeightDp = if (isSnapToCurrentHeight) dragEndAnimateHeightDp.value else dragHeightDp

    // 초기화 코드
    LaunchedEffect(key) {
        when (key) {
            OverviewPanelState.Detail.ordinal -> {
                panelState = DetailPanelState.COLLAPSED
                dragHeightDp = panelState.getBaseScreen(screenHeightDp)
                dragEndEffect = false
            }
        }
    }

    // Scroll 이펙트 영역
    var previousIndex by remember { mutableIntStateOf(listState.firstVisibleItemIndex) }
    var previousOffset by remember { mutableIntStateOf(listState.firstVisibleItemScrollOffset) }
    var isScrollingUp by remember { mutableStateOf(false) }

    LaunchedEffect(listState.firstVisibleItemIndex, listState.firstVisibleItemScrollOffset) {
        val currentIndex = listState.firstVisibleItemIndex
        val currentOffset = listState.firstVisibleItemScrollOffset
        isScrollingUp = currentIndex > previousIndex || (currentIndex == previousIndex && currentOffset > previousOffset)
        previousIndex = currentIndex
        previousOffset = currentOffset
    }

    LaunchedEffect(key1 = isScrollingUp) {
        if (isScrollingUp) {
            dragEndEffect = true
        }
    }

    LaunchedEffect(key1 = dragEndEffect) {
        if (dragEndEffect) {
            if (dragHeightDp < panelState.getBaseScreen(screenHeightDp)) {
                onHideDetailPanel()
            } else {
                // 현재 높이로 Snap
                dragEndAnimateHeightDp.snapTo(dragHeightDp)
                isSnapToCurrentHeight = true

                // 최대 높이로 슬라이드
                dragEndAnimateHeightDp.animateTo(
                    targetValue = DetailPanelState.EXPANDED.getBaseScreen(screenHeightDp),
                    animationSpec = tween(durationMillis = 300)
                )

                // 값 조정
                panelState = DetailPanelState.EXPANDED
                dragHeightDp = panelState.getBaseScreen(screenHeightDp)
            }
            dragEndEffect = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .height(panelHeightDp.dp)
                .background(Color.LightGray)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectVerticalDragGestures(
                            onDragStart = { isSnapToCurrentHeight = false },
                            onDragEnd = {
                                dragEndEffect = true
                            },
                            onVerticalDrag = { change, dragAmountPx ->
                                dragHeightDp -= (dragAmountPx / density)
                                change.consume()
                            }
                        )
                    },
                text = bookInfo.id
            )

            OverviewScreenDetailTabPager(bookInfo = bookInfo, key = key, listState = listState)
        }
    }
}