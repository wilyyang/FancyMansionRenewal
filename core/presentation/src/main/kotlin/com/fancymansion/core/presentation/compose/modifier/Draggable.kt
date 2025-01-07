package com.fancymansion.core.presentation.compose.modifier

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.zIndex
import kotlinx.coroutines.channels.Channel

data class DraggableItem(val index: Int)

/**
 * 드래그 앤 드롭 상태를 관리하는 클래스
 *
 * @param listState LazyColumn의 상태로, 항목 정보를 추출하고 스크롤 처리를 위해 필요
 * @param onMove 항목의 위치를 교체할 때 호출되는 콜백 (현재 인덱스, 목표 인덱스를 전달)
 * @param draggableItemsSize 드래그 가능한 항목의 총 개수로, 스크롤 처리 시 마지막 항목 제외 조건에 사용
 *
 * 내부 상태:
 * - draggingItem: 현재 드래그 중인 항목의 정보 (LazyListItemInfo)
 * - draggingItemIndex: 현재 드래그 중인 항목의 인덱스
 * - draggingOffsetY: 드래그 항목이 시작 위치에서 이동한 Y축 총합
 *
 * 주요 메서드:
 * - onDragStart(offset): 드래그 시작 시 호출. DraggableItem 여부를 확인하고 상태를 초기화.
 * - onDragInterrupted(): 드래그 중단 시 호출. 상태를 초기화.
 * - onDrag(offset): 드래그 중 호출. 항목 위치 교체 및 화면 끝 도달 시 스크롤 처리.
 *
 * 동작 흐름:
 * 1. 드래그 시작 시, 화면의 터치 위치에서 DraggableItem을 찾아 상태를 설정.
 * 2. 드래그 중:
 *    - 다른 DraggableItem과 겹치면 위치를 교체(onMove 호출).
 *    - 화면 상단 또는 하단에 도달하면 스크롤을 트리거.
 * 3. 드래그 종료 시, 상태를 초기화.
 */
class DragDropState(
    private val listState: LazyListState,
    private val onMove: (Int, Int) -> Unit,
    private val draggableItemsSize: Int
) {
    private var draggingItem: LazyListItemInfo? = null
    var draggingItemIndex: Int? by mutableStateOf(null)
    var draggingOffsetY by mutableFloatStateOf(0f)

    val scrollChannel = Channel<Float>()

    internal fun onDragStart(offset: Offset) {
        listState.layoutInfo.visibleItemsInfo
            .firstOrNull { item -> offset.y.toInt() in item.offset..(item.offset + item.size) }
            ?.also { currentItem ->
                (currentItem.contentType as? DraggableItem)?.let { draggableItem ->
                    draggingItem = currentItem
                    draggingItemIndex = draggableItem.index
                }
            }
    }

    internal fun onDragInterrupted() {
        draggingItem = null
        draggingItemIndex = null
        draggingOffsetY = 0f
    }

    internal fun onDrag(offset: Offset) {
        val currentDraggingItem      = draggingItem ?: return
        val currentDraggingItemIndex = draggingItemIndex ?: return

        draggingOffsetY += offset.y

        val startOffset  = currentDraggingItem.offset + draggingOffsetY
        val endOffset    = startOffset + currentDraggingItem.size
        val middleOffset = startOffset + currentDraggingItem.size / 2

        val targetItem =
            listState.layoutInfo.visibleItemsInfo.find { item ->
                middleOffset.toInt() in item.offset..item.offset + item.size &&
                        currentDraggingItem.index != item.index &&
                        item.contentType is DraggableItem
            }

        if (targetItem != null) {
            val targetIndex = (targetItem.contentType as DraggableItem).index
            onMove(currentDraggingItemIndex, targetIndex)
            draggingOffsetY += currentDraggingItem.offset - targetItem.offset
            draggingItem = targetItem
            draggingItemIndex = targetIndex
        } else {
            val startOffsetToTop = startOffset - listState.layoutInfo.viewportStartOffset
            val endOffsetToBottom = endOffset - listState.layoutInfo.viewportEndOffset
            val scroll =
                when {
                    startOffsetToTop < 0 -> startOffsetToTop.coerceAtMost(0f)
                    endOffsetToBottom > 0 -> endOffsetToBottom.coerceAtLeast(0f)
                    else -> 0f
                }

            if (scroll != 0f && currentDraggingItemIndex != 0 && currentDraggingItemIndex != draggableItemsSize - 1) {
                scrollChannel.trySend(scroll)
            }
        }
    }
}

@Composable
fun rememberDragDropState(
    lazyListState: LazyListState,
    onMove: (Int, Int) -> Unit,
    draggableItemsSize: Int
): DragDropState {
    val state =
        remember(lazyListState) {
            DragDropState(
                listState = lazyListState,
                onMove = onMove,
                draggableItemsSize = draggableItemsSize,
            )
        }

    LaunchedEffect(state) {
        while (true) {
            val diff = state.scrollChannel.receive()
            lazyListState.scrollBy(diff)
        }
    }
    return state
}

fun Modifier.dragContainer(dragDropState: DragDropState): Modifier {
    return this.pointerInput(dragDropState) {
        detectDragGesturesAfterLongPress(
            onDragStart = { offset -> dragDropState.onDragStart(offset) },
            onDrag = { change, offset ->
                change.consume()
                dragDropState.onDrag(offset = offset)
            },
            onDragEnd = { dragDropState.onDragInterrupted() },
            onDragCancel = { dragDropState.onDragInterrupted() }
        )
    }
}

inline fun <T : Any> LazyListScope.draggableItems(
    items: List<T>,
    dragDropState: DragDropState,
    crossinline content: @Composable (Modifier, T) -> Unit,
) {
    itemsIndexed(
        items = items,
        contentType = { index, _ -> DraggableItem(index = index) })
    { index, item ->
        val modifier = if (dragDropState.draggingItemIndex == index) {
            Modifier
                .zIndex(1f)
                .graphicsLayer {
                    translationY = dragDropState.draggingOffsetY
                }
        } else {
            Modifier.animateItem(placementSpec = tween(
                durationMillis = 500,
                easing = LinearOutSlowInEasing
            ))
        }
        content(modifier, item)
    }
}