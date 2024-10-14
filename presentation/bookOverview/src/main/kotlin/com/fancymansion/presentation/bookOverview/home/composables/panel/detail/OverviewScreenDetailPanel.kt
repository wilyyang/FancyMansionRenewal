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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.fancymansion.core.presentation.compose.theme.onSurfaceDimmed
import com.fancymansion.domain.model.book.BookInfoModel
import com.fancymansion.presentation.bookOverview.home.OverviewHomeContract
import com.fancymansion.presentation.bookOverview.home.composables.OverviewPanelState
import com.fancymansion.presentation.bookOverview.home.composables.detailPanelShape

enum class DetailPanelState {
    COLLAPSED,
    EXPANDED
}

@Composable
fun OverviewScreenDetailPanel(
    key: Any,
    bookInfo: BookInfoModel,
    collapsedHeightDp: Float,
    expandedHeightDp: Float,
    onHideDetailPanel: () -> Unit,
    onEventSent: (event: OverviewHomeContract.Event) -> Unit
) {
    val density = LocalDensity.current.density

    fun DetailPanelState.getPanelHeight() : Float = when(this){
        DetailPanelState.COLLAPSED -> collapsedHeightDp
        DetailPanelState.EXPANDED -> expandedHeightDp
    }

    val listState = rememberLazyListState()
    var panelState by remember { mutableStateOf(DetailPanelState.COLLAPSED) }

    // Drag 시 적용 높이
    var dragHeightDp by remember { mutableFloatStateOf(panelState.getPanelHeight()) }

    // Drag 종료 시 적용 높이
    val dragEndAnimateHeightDp = remember { Animatable(panelState.getPanelHeight()) }
    var dragEndEffect by remember { mutableStateOf(false) }
    var isSnapToCurrentHeight by remember { mutableStateOf(false) }

    // Panel 적용 높이
    val panelHeightDp = if (isSnapToCurrentHeight) dragEndAnimateHeightDp.value else dragHeightDp

    // 초기화 코드
    LaunchedEffect(key) {
        when (key) {
            OverviewPanelState.Detail.ordinal -> {
                panelState = DetailPanelState.COLLAPSED
                dragHeightDp = panelState.getPanelHeight()
                dragEndEffect = false
            }
        }
    }

    // 화면 높이 조절 이펙트
    LaunchedEffect(key1 = dragEndEffect) {
        if (dragEndEffect) {
            if (dragHeightDp < panelState.getPanelHeight()) {
                onHideDetailPanel()
            } else {
                // 현재 높이로 Snap
                dragEndAnimateHeightDp.snapTo(dragHeightDp)
                isSnapToCurrentHeight = true

                // 최대 높이로 슬라이드
                dragEndAnimateHeightDp.animateTo(
                    targetValue = DetailPanelState.EXPANDED.getPanelHeight(),
                    animationSpec = tween(durationMillis = 300)
                )

                // 값 조정
                panelState = DetailPanelState.EXPANDED
                dragHeightDp = panelState.getPanelHeight()
            }
            dragEndEffect = false
        }
    }

    // 화면 Scroll 이펙트 영역
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

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .height(panelHeightDp.dp)
                .clip(detailPanelShape)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Box(modifier = Modifier
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
                }) {

                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(vertical = 11.dp)
                        .height(4.dp)
                        .width(38.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                )
            }

            OverviewScreenDetailTabPager(bookInfo = bookInfo, key = key, listState = listState)
        }
    }
}