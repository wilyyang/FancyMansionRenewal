package com.fancymansion.presentation.editor.selectorList.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fancymansion.core.common.resource.StringValue
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.compose.modifier.dragContainer
import com.fancymansion.core.presentation.compose.modifier.draggableItems
import com.fancymansion.core.presentation.compose.modifier.rememberDragDropState
import com.fancymansion.core.presentation.compose.screen.NoDataScreen
import com.fancymansion.presentation.editor.common.itemMarginHeight
import com.fancymansion.presentation.editor.selectorList.EditorSelectorListContract
import com.fancymansion.presentation.editor.selectorList.SelectorState
import com.fancymansion.presentation.editor.selectorList.SelectorSortOrder

@Composable
fun EditorPageListScreenContent(
    modifier: Modifier = Modifier,
    uiState: EditorSelectorListContract.State,
    selectorStates : SnapshotStateList<SelectorState>,
    onEventSent: (event: EditorSelectorListContract.Event) -> Unit,
    onCommonEventSent: (event: CommonEvent) -> Unit
) {
    if (!uiState.isInitSuccess) {
        Box(
            modifier = modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .fillMaxSize(), contentAlignment = Alignment.Center
        ) {

            NoDataScreen(
                modifier = Modifier.padding(horizontal = 20.dp),
                option1Title = StringValue.StringResource(resId = com.fancymansion.core.presentation.R.string.button_back),
                onClickOption1 = {
                    onCommonEventSent(CommonEvent.CloseEvent)
                }
            )
        }
    } else {
        // Page Holder 의 드래그 이동
        val listState = rememberLazyListState()

        val dragDropState = rememberDragDropState(
            lazyListState = listState,
            draggableItemsSize = selectorStates.size,
            onMove = { fromIndex, toIndex ->
                onEventSent(EditorSelectorListContract.Event.MoveHolderPosition(fromIndex, toIndex))
            }
        )

        Column {
            PageListHeader(
                isEditMode = uiState.isEditMode,
                pageSize = selectorStates.size,
                selectorSortOrder = uiState.selectorSortOrder,
                onListModeChangeClicked = {
                    onEventSent(EditorSelectorListContract.Event.SelectorListModeChangeButtonClicked)
                },
                onPageSortOrderLastEdited = {
                    if (uiState.selectorSortOrder != SelectorSortOrder.LAST_EDITED) {
                        onEventSent(EditorSelectorListContract.Event.SelectorSortOrderLastEdited)
                    }
                },
                onPageSortOrderTitleAscending = {
                    if (uiState.selectorSortOrder != SelectorSortOrder.TEXT_ASCENDING) {
                        onEventSent(EditorSelectorListContract.Event.SelectorSortOrderTextAscending)
                    }
                },
                onSelectAllHolders = {
                    onEventSent(EditorSelectorListContract.Event.SelectAllHolders)
                },
                onDeselectAllHolders = {
                    onEventSent(EditorSelectorListContract.Event.DeselectAllHolders)
                },
                onAddPageButtonClicked = {
                    onEventSent(EditorSelectorListContract.Event.AddSelectorButtonClicked)
                },
                onDeleteSelectedHolders = {
                    onEventSent(EditorSelectorListContract.Event.DeleteSelectedHolders)
                }
            )


            LazyColumn(
                modifier = Modifier
                    .dragContainer(dragDropState)
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.surface),
                state = listState
            ) {

                draggableItems(items = selectorStates, dragDropState = dragDropState) { modifier, _, state ->
                    PageHolder(
                        modifier = modifier,
                        isEditMode = uiState.isEditMode,
                        state = state,
                        onPageContentButtonClicked = {
                            if (uiState.isEditMode) {
                                onEventSent(EditorSelectorListContract.Event.SelectorHolderSelectClicked(it))
                            } else {
                                onEventSent(
                                    EditorSelectorListContract.Event.SelectorHolderNavigateClicked(
                                        it
                                    )
                                )
                            }
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(itemMarginHeight * 2))
                }
            }
        }
    }
}