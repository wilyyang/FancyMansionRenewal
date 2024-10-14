package com.fancymansion.presentation.bookOverview.home.composables.panel.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.theme.onSurfaceInactive
import com.fancymansion.domain.model.book.BookInfoModel
import com.fancymansion.presentation.bookOverview.R
import com.fancymansion.presentation.bookOverview.home.composables.OverviewPanelState
import kotlinx.coroutines.launch

@Composable
fun OverviewScreenDetailTabPager(
    modifier: Modifier = Modifier,
    key: Any,
    bookInfo : BookInfoModel,
    listState : LazyListState
) {
    val tabs = listOf(
        stringResource(id = R.string.tab_title_book_introduce),
        stringResource(id = R.string.tab_title_book_info)
    )
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    // 초기화 코드
    LaunchedEffect(key) {
        if(key == OverviewPanelState.Detail.ordinal && pagerState.currentPage == 1){
            pagerState.animateScrollToPage(0)
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        // Tab Titles
        Row(modifier = Modifier.fillMaxWidth()) {
            tabs.forEachIndexed { index, title ->
                val isSelected = pagerState.currentPage == index
                val textColor = if (isSelected) MaterialTheme.colorScheme.onSurface else onSurfaceInactive.copy(alpha = 0.6f)
                val dividerColor = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.surface

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickSingle {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(top = 12.dp, bottom = 15.dp),
                        text = title,
                        color = textColor,
                        style = MaterialTheme.typography.titleSmall.copy(fontSize = 15.8.sp, fontWeight = FontWeight.Bold)
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .height(2.dp)
                            .fillMaxWidth()
                            .background(dividerColor)
                    )
                }
            }
        }
        HorizontalDivider(modifier = Modifier.fillMaxWidth().height(0.2.dp), color = MaterialTheme.colorScheme.outline)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        state = listState
    ) {
        item{
            // Horizontal Pager
            HorizontalPager(
                state = pagerState,
                verticalAlignment = Alignment.Top
            ) { page ->
                when (page) {
                    0 -> IntroduceContent(bookInfo = bookInfo)
                    1 -> InfoContent(bookInfo = bookInfo)
                }
            }
        }
    }
}

@Composable
fun IntroduceContent(bookInfo : BookInfoModel) {
    Column(modifier = Modifier) {
        Text("Introduce content")
        Text(bookInfo.introduce.title)
        Text(bookInfo.introduce.keywordList.joinToString(separator = " "))
        Text(bookInfo.introduce.description)
        Text(bookInfo.introduce.description)
    }

}

@Composable
fun InfoContent(bookInfo : BookInfoModel) {
    // Info 탭의 내용
    Column(modifier = Modifier) {
        Text("Info content")
        Text(bookInfo.editor.editorName)
        Text(bookInfo.editor.editorEmail)
        Text(bookInfo.editor.editorId)
        Text(bookInfo.introduce.description)
    }
}