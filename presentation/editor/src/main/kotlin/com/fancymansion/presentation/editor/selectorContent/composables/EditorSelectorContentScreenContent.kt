package com.fancymansion.presentation.editor.selectorContent.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fancymansion.core.common.resource.StringValue
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.modifier.dragContainer
import com.fancymansion.core.presentation.compose.modifier.rememberDragDropState
import com.fancymansion.core.presentation.compose.screen.NoDataScreen
import com.fancymansion.core.presentation.compose.shape.borderLine
import com.fancymansion.core.presentation.compose.theme.Paddings
import com.fancymansion.presentation.editor.R
import com.fancymansion.presentation.editor.common.composables.CommonEditInfoTitle
import com.fancymansion.presentation.editor.selectorContent.EditorSelectorContentContract
import com.fancymansion.presentation.editor.selectorContent.composables.part.EditSelectorText
import com.fancymansion.presentation.editor.selectorContent.composables.part.SelectorContentHeader

@Composable
fun EditorSelectorContentScreenContent(
    modifier: Modifier = Modifier,
    uiState: EditorSelectorContentContract.State,
    onEventSent: (event: EditorSelectorContentContract.Event) -> Unit,
    onCommonEventSent: (event: CommonEvent) -> Unit,
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
            // TODO Show Condition 04.02
            draggableItemsSize = 0,
            onMove = { fromIndex, toIndex ->
                // TODO Show Condition 04.02
            }
        )

        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)) {

            Column(
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                SelectorContentHeader(
                    // TODO Route 04.02
                    routeSize = 0,
                    onShowRouteList = {
                        // TODO Event 04.02
                    }
                )

                LazyColumn(
                    modifier = Modifier
                        .dragContainer(dragDropState)
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.surface)
                        .padding(horizontal = Paddings.Basic.horizontal),
                    state = listState
                ) {

                    item {
                        EditSelectorText(
                            modifier = Modifier.fillMaxWidth()
                                .padding(top = Paddings.Basic.vertical),
                            text = uiState.selectorText,
                            updateSelectorText = {
                                // TODO Event 04.02
                            }
                        )
                    }

                    item {
                        CommonEditInfoTitle(
                            modifier = Modifier.padding(top = Paddings.Basic.vertical),
                            title = stringResource(id = R.string.edit_selector_content_top_label_show_condition)
                        )
                    }

                    // TODO Show Condition 04.02

                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
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
                            focusManager.clearFocus()
                            // TODO Event 04.02
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