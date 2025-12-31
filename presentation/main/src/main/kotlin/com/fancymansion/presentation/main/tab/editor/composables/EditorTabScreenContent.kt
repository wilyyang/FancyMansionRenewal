package com.fancymansion.presentation.main.tab.editor.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.fancymansion.core.common.const.ImagePickType
import com.fancymansion.core.common.resource.StringValue
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.screen.NoDataScreen
import com.fancymansion.core.presentation.compose.shape.borderLine
import com.fancymansion.core.presentation.compose.theme.onSurfaceDimmed
import com.fancymansion.presentation.main.tab.editor.EditorTabContract
import com.fancymansion.presentation.main.R
import com.fancymansion.presentation.main.tab.editor.composables.part.BottomBookPagination
import com.fancymansion.presentation.main.tab.editor.composables.part.EditBookHolder
import com.fancymansion.presentation.main.tab.editor.composables.part.EditorTabHeader
import com.fancymansion.presentation.main.tab.editor.composables.part.SearchTextField
import kotlinx.coroutines.launch

@Composable
fun EditorTabScreenContent(
    modifier: Modifier = Modifier,
    uiState: EditorTabContract.State,
    onEventSent: (event: EditorTabContract.Event) -> Unit,
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
        val painterList = uiState.visibleBookList.map { data ->
            when (data.bookInfo.thumbnail) {
                is ImagePickType.SavedImage -> {
                    rememberAsyncImagePainter(data.bookInfo.thumbnail.file)
                }

                else -> {
                    painterResource(id = R.drawable.holder_book_image_no_available)
                }
            }
        }
        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()
        LaunchedEffect(uiState.currentPage) {
            coroutineScope.launch {
                listState.scrollToItem(0)
            }
        }

        val focusManager = LocalFocusManager.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SearchTextField(
                    modifier = Modifier.fillMaxWidth(0.85f),
                    value = uiState.searchText,
                    maxLine = 1,
                    hint = stringResource(R.string.edit_book_search_text_hint),
                    keyboardActions = KeyboardActions {
                        focusManager.clearFocus()
                        onEventSent(EditorTabContract.Event.SearchClicked)
                    },
                    isEnabled = !uiState.isEditMode,
                    isCancelable = true
                ) {
                    onEventSent(EditorTabContract.Event.SearchTextInput(searchText = it))
                }

                Box(
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .fillMaxWidth(1f)
                        .height(35.dp)
                        .clickSingle(
                            enabled = !uiState.isEditMode
                        ){
                            focusManager.clearFocus()
                            onEventSent(EditorTabContract.Event.SearchCancel)
                        }
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "취소",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .borderLine(
                                density = LocalDensity.current,
                                color = MaterialTheme.colorScheme.outline,
                                bottom = 1.dp
                            )
                            .padding(vertical = 18.dp, horizontal = 14.dp)
                    ) {
                        Text(
                            text = "작품",
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }

                item {
                    EditorTabHeader(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 18.dp, horizontal = 14.dp),
                        isEditMode = uiState.isEditMode,
                        bookSortOrder = uiState.bookSortOrder,
                        focusManager = focusManager,
                        onEventSent = onEventSent
                    )
                }

                itemsIndexed (uiState.visibleBookList){ idx, data ->
                    EditBookHolder(
                        bookState = data,
                        painter = painterList[idx],
                        isEditMode = uiState.isEditMode,
                        onClickHolder = {
                            onEventSent(EditorTabContract.Event.BookHolderClicked(it))
                        }
                    )

                    if (idx < uiState.visibleBookList.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier
                                .padding(horizontal = 14.dp)
                                .height(0.3.dp)
                                .fillMaxWidth(),
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }

                item{
                    BottomBookPagination(
                        modifier = Modifier
                            .padding(vertical = 50.dp)
                            .fillMaxWidth(),
                        currentPage = uiState.currentPage,
                        totalPageCount = uiState.totalPageCount,
                        onClickPageNumber = {
                            onEventSent(EditorTabContract.Event.BookPageNumberClicked(it))
                        }
                    )
                }
            }
        }
    }
}