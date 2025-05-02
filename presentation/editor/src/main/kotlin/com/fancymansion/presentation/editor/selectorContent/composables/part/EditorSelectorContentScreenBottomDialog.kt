package com.fancymansion.presentation.editor.selectorContent.composables.part

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.modifier.dragContainer
import com.fancymansion.core.presentation.compose.modifier.draggableItems
import com.fancymansion.core.presentation.compose.modifier.rememberDragDropState
import com.fancymansion.core.presentation.compose.theme.Paddings
import com.fancymansion.core.presentation.compose.theme.onSurfaceSub
import com.fancymansion.presentation.editor.R
import com.fancymansion.presentation.editor.common.composables.CommonEditInfoTitle
import com.fancymansion.presentation.editor.selectorContent.EditorSelectorContentContract
import com.fancymansion.presentation.editor.selectorContent.RouteState

const val detailPanelCornerHeight = 12
val detailPanelShape = RoundedCornerShape(topStart = detailPanelCornerHeight.dp, topEnd = detailPanelCornerHeight.dp)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BottomRouteListDialog(
    routeStates: SnapshotStateList<RouteState>,
    onEventSent: (event: EditorSelectorContentContract.Event) -> Unit,
) {

    val listState = rememberLazyListState()
    val dragDropState = rememberDragDropState(
        lazyListState = listState,
        draggableItemsSize = routeStates.size,
        onMove = { fromIndex, toIndex ->
            onEventSent(
                EditorSelectorContentContract.Event.MoveRouteHolderPosition(
                    fromIndex = fromIndex,
                    toIndex = toIndex
                )
            )
        }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.6f)
            .clip(detailPanelShape)
            .background(MaterialTheme.colorScheme.surface)
            .padding(top = 10.dp)
            .clickSingle { }
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            CommonEditInfoTitle(
                modifier = Modifier
                    .padding(vertical = Paddings.Basic.vertical)
                    .padding(start = Paddings.Basic.horizontal),
                title = stringResource(id = R.string.edit_selector_dialog_top_label_route)
            )

            Text(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(horizontal = Paddings.Basic.horizontal / 2)
                    .clip(shape = MaterialTheme.shapes.medium)
                    .clickSingle(
                        indication = LocalIndication.current
                    ) {
                        onEventSent(EditorSelectorContentContract.Event.AddRouteClicked)
                    }
                    .padding(
                        vertical = Paddings.Basic.vertical,
                        horizontal = Paddings.Basic.horizontal / 2
                    ),
                text = stringResource(id = R.string.edit_selector_dialog_top_route_item_add),
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
            )
        }

        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 0.3.dp, color = onSurfaceSub)

        LazyColumn(
            modifier = Modifier
                .dragContainer(dragDropState)
                .fillMaxSize(),
            state = listState
        ) {

            draggableItems(items = routeStates, dragDropState = dragDropState) { modifier, index, state ->
                RouteHolder(
                    modifier = modifier,
                    state = state,
                    isEnd = index == routeStates.size - 1,
                    onRouteHolderDelete = {
                        onEventSent(EditorSelectorContentContract.Event.RouteHolderDeleteClicked(state.route.routeId))
                    },
                    onRouteHolderClicked = {
                        onEventSent(EditorSelectorContentContract.Event.RouteHolderNavigateClicked(state.route.routeId))
                    }
                )
            }

            item {
                HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 0.3.dp, color = onSurfaceSub)
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}