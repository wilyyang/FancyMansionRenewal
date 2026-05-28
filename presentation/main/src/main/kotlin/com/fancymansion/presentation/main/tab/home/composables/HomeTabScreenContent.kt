package com.fancymansion.presentation.main.tab.home.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fancymansion.core.common.resource.StringValue
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.compose.modifier.addFocusCleanerWhenImeVisible
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.screen.NoDataScreen
import com.fancymansion.core.presentation.compose.shape.borderLine
import com.fancymansion.presentation.main.R
import com.fancymansion.presentation.main.common.composables.BottomBookPagination
import com.fancymansion.presentation.main.common.composables.SearchTextField
import com.fancymansion.presentation.main.tab.home.HomeTabContract
import com.fancymansion.presentation.main.tab.home.composables.part.HomeBookHolder
import com.fancymansion.presentation.main.tab.home.composables.part.HomeTabHeader
import kotlinx.coroutines.launch

@Composable
fun HomeTabScreenContent(
    modifier: Modifier = Modifier,
    uiState: HomeTabContract.State,
    onEventSent: (event: HomeTabContract.Event) -> Unit,
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
                option1Title = StringValue.StringResource(resId = com.fancymansion.core.presentation.R.string.button_exit),
                onClickOption1 = {
                    onCommonEventSent(CommonEvent.CloseEvent)
                }
            )
        }
    } else {
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
                    .padding(start = 15.5.dp, end = 13.5.dp)
                    .padding(vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SearchTextField(
                    modifier = Modifier.fillMaxWidth(0.875f),
                    value = uiState.searchText,
                    maxLine = 1,
                    hint = stringResource(R.string.home_book_search_text_hint),
                    keyboardActions = KeyboardActions {
                        focusManager.clearFocus()
                        onEventSent(HomeTabContract.Event.SearchClicked)
                    },
                    isCancelable = true
                ) {
                    onEventSent(HomeTabContract.Event.SearchTextInput(searchText = it))
                }

                Box(
                    modifier = Modifier
                        .padding(start = 10.5.dp)
                        .fillMaxWidth(1f)
                        .height(35.dp)
                        .clickSingle {
                            focusManager.clearFocus()
                            onEventSent(HomeTabContract.Event.SearchCancel)
                        }
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center).padding(bottom = 3.5.dp),
                        text = stringResource(R.string.home_book_search_text_cancel),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                    )
                }
            }

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .addFocusCleanerWhenImeVisible(focusManager)
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
                            .padding(top = 13.dp, bottom = 8.dp)
                            .padding(horizontal = 15.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.home_book_bar_title),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }

                item {
                    HomeTabHeader(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 18.dp, horizontal = 14.dp),
                        bookSortOrder = uiState.bookSortOrder,
                        onEventSent = onEventSent
                    )
                }

                if(uiState.visibleBookList.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 25.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                modifier = Modifier.fillMaxWidth(0.5f),
                                painter = painterResource(id = R.drawable.img_book_list_empty),
                                contentScale = ContentScale.FillWidth,
                                contentDescription = null
                            )

                            Text(
                                modifier = Modifier.padding(top = 12.dp),
                                text = stringResource(id = R.string.home_book_list_empty_title),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                modifier = Modifier.padding(top = 8.dp),
                                text = stringResource(id = R.string.home_book_list_empty_sub_title),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                    }
                }else{
                    itemsIndexed (uiState.visibleBookList){ idx, wrapper ->
                        HomeBookHolder(
                            bookWrapper = wrapper,
                            onClickHolder = {
                                onEventSent(HomeTabContract.Event.BookHolderClicked(it))
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
                }

                item{
                    BottomBookPagination(
                        modifier = Modifier
                            .padding(top = 40.dp, bottom = 50.dp)
                            .fillMaxWidth(),
                        currentPage = uiState.currentPage,
                        totalPageCount = uiState.endPage,
                        onClickPageNumber = {
                            onEventSent(HomeTabContract.Event.BookPageNumberClicked(it))
                        }
                    )
                }
            }
        }
    }
}