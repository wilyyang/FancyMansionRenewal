package com.fancymansion.presentation.bookOverview.home.composables.panel.detail

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.domain.model.book.BookInfoModel
import com.fancymansion.presentation.bookOverview.home.composables.OverviewPanelState
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OverviewScreenDetailTabPager(
    modifier: Modifier = Modifier,
    key: Any,
    bookInfo : BookInfoModel,
    listState : LazyListState
) {
    val pagerState = rememberPagerState(pageCount = { 2 })
    val tabs = listOf("Introduce", "Info")
    val coroutineScope = rememberCoroutineScope()

    // 초기화 코드
    LaunchedEffect(key) {
        if(key == OverviewPanelState.Detail.ordinal && pagerState.currentPage == 1){
            pagerState.animateScrollToPage(0)
        }
    }

    Column(modifier = modifier.fillMaxWidth().background(color = Color.Red)) {
        // Tab Titles
        Row(modifier = Modifier.fillMaxWidth()) {
            tabs.forEachIndexed { index, title ->
                val isSelected = pagerState.currentPage == index
                val textColor = if (isSelected) Color.Black else Color.Gray
                val dividerColor = if (isSelected) Color.Black else Color.Gray

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickSingle {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(title, color = textColor, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .height(2.dp)
                            .fillMaxWidth()
                            .background(dividerColor)
                    )
                }
            }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = Color.LightGray),
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
    Column(modifier = Modifier.background(color = Color.Cyan)) {
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
    Column(modifier = Modifier.background(color = Color.Magenta)) {
        Text("Info content")
        Text(bookInfo.editor.editorName)
        Text(bookInfo.editor.editorEmail)
        Text(bookInfo.editor.editorId)
        Text(bookInfo.introduce.description)
    }
}