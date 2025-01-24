package com.fancymansion.presentation.editor.pageContent.composables

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
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import com.fancymansion.core.presentation.compose.modifier.draggableItems
import com.fancymansion.core.presentation.compose.modifier.rememberDragDropState
import com.fancymansion.core.presentation.compose.screen.NoDataScreen
import com.fancymansion.core.presentation.compose.shape.borderLine
import com.fancymansion.core.presentation.compose.theme.Paddings
import com.fancymansion.presentation.editor.R
import com.fancymansion.presentation.editor.common.composables.CommonEditInfoTitle
import com.fancymansion.presentation.editor.pageContent.EditorPageContentContract
import com.fancymansion.presentation.editor.pageContent.SourceWrapper
import com.fancymansion.presentation.editor.pageContent.composables.part.EditPageSourceImage
import com.fancymansion.presentation.editor.pageContent.composables.part.EditPageSourceText
import com.fancymansion.presentation.editor.pageContent.composables.part.EditPageTitle
import com.fancymansion.presentation.editor.pageContent.composables.part.PageContentHeader

@Composable
fun EditorPageContentScreenContent(
    modifier: Modifier = Modifier,
    uiState: EditorPageContentContract.State,
    contentSourceStates: SnapshotStateList<SourceWrapper>,
    onEventSent: (event: EditorPageContentContract.Event) -> Unit,
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
    }else{
        val listState = rememberLazyListState()
        val dragDropState = rememberDragDropState(
            lazyListState = listState,
            draggableItemsSize = contentSourceStates.size,
            onMove = { fromIndex, toIndex ->
                onEventSent(EditorPageContentContract.Event.MoveSourcePosition(fromIndex = fromIndex, toIndex = toIndex))
            }
        )

        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)){

            Column(
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                PageContentHeader(
                    contentSourceSize = contentSourceStates.size,
                    onShowSelectorList = { /*TODO*/ },
                    onAddContentSourceClicked = { /*TODO*/ }
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
                        EditPageTitle(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = Paddings.Basic.vertical),
                            title = uiState.pageTitle,
                            updateBookInfoTitle = {
                                onEventSent(EditorPageContentContract.Event.EditPageContentTitle(title = it))
                            }
                        )
                    }

                    item {
                        CommonEditInfoTitle(
                            title = stringResource(id = R.string.edit_page_content_top_label_page_content)
                        )
                    }

                    draggableItems(items = contentSourceStates, dragDropState = dragDropState) { modifier, index, state ->
                        when(state){
                            is SourceWrapper.TextWrapper -> {
                                EditPageSourceText(
                                    modifier = modifier,
                                    text = state.description,
                                    onClickText =  {
                                        onEventSent(EditorPageContentContract.Event.OnClickSourceText(index))
                                    }
                                )
                            }
                            is SourceWrapper.ImageWrapper -> {
                                EditPageSourceImage(
                                    modifier = modifier,
                                    imagePickType = state.imagePickType,
                                    onClickGalleryImagePick = {
                                        onEventSent(EditorPageContentContract.Event.OnClickSourceImage(index))
                                    }
                                )
                            }
                        }
                    }

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
                            // TODO
                        }
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = stringResource(id = com.fancymansion.presentation.editor.R.string.edit_page_content_button_page_preview),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}