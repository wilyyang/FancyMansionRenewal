package com.fancymansion.presentation.editor.routeContent.composables

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fancymansion.core.common.resource.StringValue
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.modifier.dragContainer
import com.fancymansion.core.presentation.compose.modifier.draggableItems
import com.fancymansion.core.presentation.compose.modifier.rememberDragDropState
import com.fancymansion.core.presentation.compose.screen.NoDataScreen
import com.fancymansion.core.presentation.compose.shape.borderLine
import com.fancymansion.core.presentation.compose.theme.Paddings
import com.fancymansion.core.presentation.compose.theme.onSurfaceSub
import com.fancymansion.presentation.editor.R
import com.fancymansion.presentation.editor.common.ConditionState
import com.fancymansion.presentation.editor.common.composables.CommonEditInfoTitle
import com.fancymansion.presentation.editor.common.composables.ConditionHolder
import com.fancymansion.presentation.editor.routeContent.EditorRouteContentContract
import com.fancymansion.presentation.editor.routeContent.composables.part.SelectRouteTargetPage

@Composable
fun EditorRouteContentScreenContent(
    modifier: Modifier = Modifier,
    uiState: EditorRouteContentContract.State,
    routeConditionStates : SnapshotStateList<ConditionState>,
    onEventSent: (event: EditorRouteContentContract.Event) -> Unit,
    onCommonEventSent: (event: CommonEvent) -> Unit,
    onOpenTargetPageList: () -> Unit,
    focusManager : FocusManager
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
        val listState = rememberLazyListState()
        val dragDropState = rememberDragDropState(
            lazyListState = listState,
            draggableItemsSize = routeConditionStates.size,
            onMove = { fromIndex, toIndex ->
                // TODO 05.26
            }
        )

        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)) {

            LazyColumn(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .dragContainer(dragDropState)
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.surface),
                state = listState
            ) {

                item {
                    SelectRouteTargetPage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Paddings.Basic.horizontal)
                            .padding(top = Paddings.Basic.vertical),
                        itemText = uiState.targetPage.pageTitle,
                        onClickItemText = {
                            onOpenTargetPageList()
                        }
                    )
                }

                item {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CommonEditInfoTitle(
                            modifier = Modifier
                                .padding(vertical = Paddings.Basic.vertical)
                                .padding(start = Paddings.Basic.horizontal),
                            title = stringResource(id = R.string.edit_route_content_top_label_route_condition)
                        )

                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(horizontal = Paddings.Basic.horizontal / 2)
                                .clip(shape = MaterialTheme.shapes.medium)
                                .clickSingle(
                                    indication = LocalIndication.current
                                ) {
                                    // TODO 05.26
                                }
                                .padding(
                                    vertical = Paddings.Basic.vertical,
                                    horizontal = Paddings.Basic.horizontal / 2
                                ),
                            text = stringResource(id = R.string.edit_route_content_route_condition_item_add),
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
                        )
                    }

                    HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 0.3.dp, color = onSurfaceSub)
                }

                draggableItems(items = routeConditionStates, dragDropState = dragDropState) { modifier, index, state ->
                    ConditionHolder(
                        modifier = modifier,
                        state = state,
                        isEnd = index == routeConditionStates.size - 1,
                        onConditionHolderDelete = {
                            // TODO 05.26
                        },
                        onConditionHolderClicked = {
                            // TODO 05.26
                        }
                    )
                }

                item {
                    HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 0.3.dp, color = onSurfaceSub)
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .borderLine(
                        density = LocalDensity.current,
                        color = MaterialTheme.colorScheme.outline,
                        top = 1.dp
                    )
            ) {

                Box(
                    modifier = Modifier
                        .padding(vertical = 11.dp, horizontal = 14.dp)
                        .fillMaxSize()
                        .clip(
                            shape = MaterialTheme.shapes.small
                        )
                        .background(color = MaterialTheme.colorScheme.primary)
                        .clickSingle {
                            // TODO 05.16
                        }
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = stringResource(id = R.string.edit_selector_content_button_page_preview),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}