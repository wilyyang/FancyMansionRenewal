package com.fancymansion.presentation.main.tab.editor.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.fancymansion.core.presentation.compose.component.EnumDropdown
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.screen.NoDataScreen
import com.fancymansion.core.presentation.compose.shape.borderLine
import com.fancymansion.core.presentation.compose.theme.onSurfaceDimmed
import com.fancymansion.presentation.main.tab.editor.EditorTabContract
import com.fancymansion.presentation.main.R
import com.fancymansion.presentation.main.tab.editor.EditBookSortOrder
import com.fancymansion.presentation.main.tab.editor.EditBookState
import com.fancymansion.presentation.main.tab.editor.composables.part.SearchTextField
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
        val context = LocalContext.current
        val painterList = uiState.visibleBookList.map { data ->
            when (data.bookInfo.thumbnail) {
                is ImagePickType.SavedImage -> {
                    rememberAsyncImagePainter(data.bookInfo.thumbnail.file)
                }

                else -> {
                    painterResource(id = R.drawable.holder_book_image_sample)
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
                    isCancelable = true
                ) {
                    onEventSent(EditorTabContract.Event.SearchTextInput(searchText = it))
                }

                Box(
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .fillMaxWidth(1f)
                        .height(35.dp)
                        .clickSingle{
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
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 18.dp, horizontal = 14.dp)
                    ){
                        EnumDropdown(
                            modifier = Modifier.width(140.dp),
                            options = EditBookSortOrder.entries.toTypedArray(),
                            selectedOption = uiState.bookSortOrder,
                            getDisplayName = {
                                context.getString(it.textResId)
                            }
                        ) {
                            onEventSent(EditorTabContract.Event.SelectBookSortOrder(it))
                        }

                        Text(
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .align(Alignment.CenterEnd),
                            text = "편집",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }

                itemsIndexed (uiState.visibleBookList){ idx, data ->
                    EditBookHolder(
                        bookState = data,
                        painter = painterList[idx],
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
                    Row(
                        modifier = Modifier
                            .padding(vertical = 50.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ){
                        for(i in 0 until uiState.totalPageCount){
                            Text(
                                modifier = Modifier
                                    .padding(end = if (i < uiState.totalPageCount - 1) 30.dp else 0.dp)
                                    .clickSingle {
                                        onEventSent(EditorTabContract.Event.BookPageNumberClicked(i))
                                    },
                                text = "${i + 1}",
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = if (i == uiState.currentPage) SemiBold else Medium),
                                color = if (i == uiState.currentPage) MaterialTheme.colorScheme.onSurface else onSurfaceDimmed
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EditBookHolder(
    modifier : Modifier = Modifier,
    painter: Painter,
    bookState : EditBookState,
    onClickHolder : (String) -> Unit
){

    Row(
        modifier = modifier
            .padding(vertical = 18.dp, horizontal = 14.dp)
            .clickSingle {
                onClickHolder(bookState.bookInfo.bookId)
            },
        verticalAlignment = Alignment.CenterVertically
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth(0.25f)
                .heightIn(max = 200.dp)
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                painter = painter,
                contentScale = ContentScale.FillWidth,
                contentDescription = ""
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(start = 10.dp)
        ) {
            Text(
                text = bookState.bookInfo.title,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                modifier = Modifier.padding(top = 5.dp),
                text = "${formatTimestampLegacy(bookState.bookInfo.editTime)} 편집됨",
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                modifier = Modifier.padding(top = 5.dp),
                text = "총 ${bookState.bookInfo.pageCount} 페이지",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )

            FlowRow(
                modifier = Modifier.padding(top = 5.dp)
            ) {
                bookState.bookInfo.keywords.take(6).forEach { keyword ->
                    Text(
                        modifier = Modifier
                            .padding(end = 2.dp)
                            .padding(bottom = 2.dp)
                            .clip(shape = MaterialTheme.shapes.extraSmall)
                            .padding(0.5.dp)
                            .border(
                                width = 0.5.dp,
                                color = MaterialTheme.colorScheme.outline,
                                shape = MaterialTheme.shapes.extraSmall
                            )
                            .padding(horizontal = 7.dp, vertical = 5.dp),
                        text = "#${keyword.name}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

fun formatTimestampLegacy(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("yyyy.M.d", Locale.getDefault())
    return formatter.format(date)
}