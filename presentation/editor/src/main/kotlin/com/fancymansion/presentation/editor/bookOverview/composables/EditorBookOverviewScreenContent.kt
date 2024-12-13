package com.fancymansion.presentation.editor.bookOverview.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.fancymansion.core.presentation.compose.modifier.addFocusCleaner
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.modifier.customImePadding
import com.fancymansion.core.presentation.compose.screen.NoDataScreen
import com.fancymansion.core.presentation.compose.shape.borderLine
import com.fancymansion.presentation.editor.R
import com.fancymansion.presentation.editor.bookOverview.EditorBookOverviewContract
import com.fancymansion.presentation.editor.bookOverview.KeywordState

@Composable
fun EditorBookOverviewScreenContent(
    modifier: Modifier = Modifier,
    uiState: EditorBookOverviewContract.State,
    keywordStates : SnapshotStateList<KeywordState>,
    onEventSent: (event: EditorBookOverviewContract.Event) -> Unit,
    onCommonEventSent: (event: CommonEvent) -> Unit,
    onOpenEditKeywords: () -> Unit,
    focusManager : FocusManager
) {
    uiState.bookInfo?.let { bookInfo ->
        if (bookInfo.id.isBlank() || bookInfo.editor.editorId.isBlank()) {
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

            val commonPartModifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(
                    horizontal = EDIT_ITEM_HORIZONTAL_PADDING,
                    vertical = 15.dp
                )

            Box(modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)){

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .customImePadding()
                        .addFocusCleaner(focusManager)
                ) {

                    item {
                        EditOverviewTopInfo(
                            modifier = commonPartModifier,
                            imagePickType = uiState.imagePickType,
                            title = uiState.bookInfo.introduce.title,
                            onClickGalleryCoverPick = {
                                focusManager.clearFocus()
                                onEventSent(EditorBookOverviewContract.Event.GalleryBookCoverPickerRequest)
                            },
                            onClickCoverImageReset = {
                                focusManager.clearFocus()
                                onEventSent(EditorBookOverviewContract.Event.CoverImageReset)
                            },
                            updateBookInfoTitle = {
                                onEventSent(EditorBookOverviewContract.Event.EditBookInfoTitle(title = it))
                            }
                        )
                    }

                    item{
                        EditOverviewKeyword(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = MaterialTheme.colorScheme.surface),
                            keywordStates = keywordStates,
                            onOpenEditKeywords = {
                                focusManager.clearFocus()
                                onOpenEditKeywords()
                            }
                        )
                    }

                    item{
                        EditOverviewPageList(
                            modifier = commonPartModifier,
                            pageBriefList = uiState.pageBriefList,
                            onPageListMoreClicked = {
                                focusManager.clearFocus()
                                onEventSent(EditorBookOverviewContract.Event.EditorPageListClicked)
                            },
                            onPageListEditModeClicked = {
                                focusManager.clearFocus()
                                onEventSent(EditorBookOverviewContract.Event.EditorPageListEditModeClicked)
                            },
                            onPageContentButtonClicked = {
                                focusManager.clearFocus()
                                onEventSent(
                                    EditorBookOverviewContract.Event.EditorPageContentButtonClicked(
                                        it
                                    )
                                )
                            }
                        )
                    }

                    item{
                        EditOverviewDescription(
                            modifier = commonPartModifier,
                            description = uiState.bookInfo.introduce.description,
                            updateBookInfoDescription = {
                                onEventSent(EditorBookOverviewContract.Event.EditBookInfoDescription(description = it))
                            }
                        )
                    }

                    item {
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
                                focusManager.clearFocus()
                                onEventSent(EditorBookOverviewContract.Event.BookOverviewButtonClicked)
                            }
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = stringResource(id = R.string.edit_overview_button_overview_preview),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}