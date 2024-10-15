package com.fancymansion.presentation.bookOverview.home.composables.panel.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.theme.onSurfaceInactive
import com.fancymansion.core.presentation.compose.theme.typography.TypeStyles
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
                        style = TypeStyles.titleSmallVariant
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
        HorizontalDivider(modifier = Modifier
            .fillMaxWidth()
            .height(0.2.dp), color = MaterialTheme.colorScheme.outline)
    }

    val pageVerticalPadding = 20.dp
    val pageHorizontalPadding = 15.dp
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
                    0 -> IntroduceContent(modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = pageVerticalPadding, horizontal = pageHorizontalPadding), bookInfo = bookInfo)
                    1 -> InfoContent(modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = pageVerticalPadding, horizontal = pageHorizontalPadding), bookInfo = bookInfo)
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun IntroduceContent(
    modifier: Modifier = Modifier,
    bookInfo : BookInfoModel
) {
    val titleBottomPadding = 13.dp
    val contentBottomPadding = 38.dp

    Column(modifier = modifier) {
        Text(modifier = Modifier.padding(bottom = titleBottomPadding), text = stringResource(id = R.string.introduce_tab_title_keyword), style = TypeStyles.titleSmallVariant)

        FlowRow(
            modifier = Modifier.padding(bottom = contentBottomPadding),
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp),
        ) {

            for (keyword in bookInfo.introduce.keywordList) {

                Text(
                    modifier = Modifier
                        .padding(end = 2.dp)
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


        Text(
            modifier = Modifier.padding(bottom = titleBottomPadding),
            text = stringResource(id = R.string.introduce_tab_title_introduce),
            style = TypeStyles.titleSmallVariant
        )
        Text(
            modifier = Modifier.padding(bottom = contentBottomPadding),
            text = bookInfo.introduce.description,
            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 24.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f)
        )
    }

}

@Composable
fun InfoContent(
    modifier: Modifier = Modifier,
    bookInfo : BookInfoModel
) {
    val titleTextStyle = TypeStyles.titleSmallVariant
    Column(modifier = modifier) {
        Text("Info content")
        Text(bookInfo.editor.editorName)
        Text(bookInfo.editor.editorEmail)
        Text(bookInfo.editor.editorId)
        Text(bookInfo.introduce.description)
    }
}