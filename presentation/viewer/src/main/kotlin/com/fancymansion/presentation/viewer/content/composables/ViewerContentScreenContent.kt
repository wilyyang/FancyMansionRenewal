package com.fancymansion.presentation.viewer.content.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.fancymansion.core.common.const.PageLineHeight
import com.fancymansion.core.common.const.PageMarginHorizontal
import com.fancymansion.core.common.const.PageTextSize
import com.fancymansion.core.common.const.PageTheme
import com.fancymansion.core.common.const.SelectorPaddingVertical
import com.fancymansion.core.presentation.theme.ColorSet
import com.fancymansion.domain.model.book.PageSettingModel
import com.fancymansion.domain.model.book.SelectorModel
import com.fancymansion.presentation.viewer.content.PageWrapper
import com.fancymansion.presentation.viewer.content.SourceWrapper
import com.fancymansion.presentation.viewer.content.ViewerContentContract

@Composable
fun ViewerContentScreenContent(
    modifier: Modifier = Modifier,
    uiState: ViewerContentContract.State,
    onEventSent: (event: ViewerContentContract.Event) -> Unit
) {
    ViewerContentScreenPageContent(modifier, uiState.pageSetting, uiState.pageWrapper, uiState.selectors,  onEventSent)
}


@Composable
fun ViewerContentScreenPageContent(
    modifier: Modifier,
    pageSetting: PageSettingModel,
    pageWrapper : PageWrapper?,
    selectors: List<SelectorModel>,
    onEventSent: (event: ViewerContentContract.Event) -> Unit
) {
    val titleTextStyle = pageSetting.pageContentSetting.run {
        MaterialTheme.typography.titleLarge.copy(
            fontSize = textSize.dpSize.sp,
            lineHeight = (textSize.dpSize + lineHeight.dpSize).sp,
            color = Color(pageSetting.pageTheme.textColor.colorCode),
            fontWeight = FontWeight.SemiBold
        )
    }

    val contentTextStyle = pageSetting.pageContentSetting.run {
        MaterialTheme.typography.bodyLarge.copy(
            fontSize = textSize.dpSize.sp,
            lineHeight = (textSize.dpSize + lineHeight.dpSize).sp,
            color = Color(pageSetting.pageTheme.textColor.colorCode),
            fontWeight = FontWeight.Normal
        )
    }

    val selectorTextStyle = pageSetting.selectorSetting.run {
        MaterialTheme.typography.bodyLarge.copy(
            fontSize = textSize.dpSize.sp,
            lineHeight = (textSize.dpSize * 1.1).sp,
            color = Color(pageSetting.pageTheme.selectorTextColor.colorCode),
            fontWeight = FontWeight.Medium
        )
    }
    val listState = rememberLazyListState()

    when (pageWrapper) {
        null -> {
            Box(modifier = modifier
                .background(color = Color(pageSetting.pageTheme.pageColor.colorCode))
                .fillMaxSize(), contentAlignment = Alignment.Center){
                Text(text = "No Data")
            }
        }
        else -> {
            LaunchedEffect(key1 = pageWrapper) {
                listState.animateScrollToItem(0)
            }


            LazyColumn(
                modifier = modifier.background(color = Color(pageSetting.pageTheme.pageColor.colorCode)),
                state = listState
            ) {
                item {
                    Text(
                        modifier = Modifier.padding(vertical = 20.dp, horizontal = 5.dp),
                        text = pageWrapper.title, style = titleTextStyle
                    )
                }
                items(pageWrapper.sources) {
                    when (it) {
                        is SourceWrapper.TextWrapper -> {
                            Text(
                                modifier = Modifier.padding(horizontal = pageSetting.pageContentSetting.textMarginHorizontal.dpSize.dp),
                                text = it.description,
                                style = contentTextStyle
                            )
                        }

                        is SourceWrapper.ImageWrapper -> {
                            AsyncImage(
                                modifier = Modifier.padding(horizontal = pageSetting.pageContentSetting.imageMarginHorizontal.dpSize.dp).fillMaxWidth(),
                                model = ImageRequest.Builder(LocalContext.current).data(it.imageFile).build(),
                                contentDescription = "",
                                contentScale = ContentScale.FillWidth
                            )
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
                items(selectors) { selector ->
                    Row(
                        modifier = Modifier
                            .padding(vertical = 10.dp, horizontal = 15.dp)
                            .fillMaxWidth()
                            .clip(
                                shape = MaterialTheme.shapes.small
                            )
                            .background(color = Color(pageSetting.pageTheme.selectorColor.colorCode))
                            .clickable {
                                onEventSent(
                                    ViewerContentContract.Event.OnClickSelector(
                                        selector.pageId,
                                        selector.selectorId
                                    )
                                )
                            }
                            .padding(horizontal = 15.dp, vertical = pageSetting.selectorSetting.paddingVertical.dpSize.dp)
                    ) {
                        Text(text = selector.text, style = selectorTextStyle)
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }

                /**
                 * 임시 페이지 설정 코드
                 */
                item {
                    Spacer(modifier = Modifier.height(50.dp))
                    
                    Box(modifier = Modifier
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(color = Color(pageSetting.pageTheme.selectorColor.colorCode)))

                    Row(
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 15.dp),
                        verticalAlignment = Alignment.CenterVertically){
                        Text(text = "배경색 :  ")

                        PageTheme.entries.forEach {
                            Box(
                                modifier = Modifier
                                    .padding(end = 5.dp)
                                    .size(30.dp, 30.dp)
                                    .clip(shape = MaterialTheme.shapes.small)
                                    .background(color = Color(it.pageColor.colorCode))
                                    .border(
                                        width = 1.dp,
                                        shape = MaterialTheme.shapes.small,
                                        color = if (pageSetting.pageTheme.pageColor == it.pageColor) ColorSet.sky_c1ebfe else Color.Black
                                    )
                                    .clickable {
                                        onEventSent(
                                            ViewerContentContract.Event.SettingEvent.ChangeSettingPageTheme(
                                                it
                                            )
                                        )
                                    },
                                contentAlignment = Alignment.Center
                            ){}
                        }
                    }

                    SettingRow("본문 텍스트", PageTextSize.entries, pageSetting.pageContentSetting.textSize) {
                        onEventSent(
                            ViewerContentContract.Event.SettingEvent.ChangeSettingContentTextSize(it)
                        )
                    }

                    SettingRow("본문 줄간격", PageLineHeight.entries, pageSetting.pageContentSetting.lineHeight) {
                        onEventSent(
                            ViewerContentContract.Event.SettingEvent.ChangeSettingContentLineHeight(it)
                        )
                    }

                    SettingRow("텍스트 너비", PageMarginHorizontal.entries, pageSetting.pageContentSetting.textMarginHorizontal) {
                        onEventSent(
                            ViewerContentContract.Event.SettingEvent.ChangeSettingContentTextMargin(it)
                        )
                    }

                    SettingRow("이미지 너비", PageMarginHorizontal.entries, pageSetting.pageContentSetting.imageMarginHorizontal) {
                        onEventSent(
                            ViewerContentContract.Event.SettingEvent.ChangeSettingContentImageMargin(it)
                        )
                    }

                    SettingRow("설렉터 텍스트", PageTextSize.entries, pageSetting.selectorSetting.textSize) {
                        onEventSent(
                            ViewerContentContract.Event.SettingEvent.ChangeSettingSelectorTextSize(it)
                        )
                    }

                    SettingRow("설렉터 줄간격", SelectorPaddingVertical.entries, pageSetting.selectorSetting.paddingVertical) {
                        onEventSent(
                            ViewerContentContract.Event.SettingEvent.ChangeSettingSelectorPaddingVertical(it)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun<T> SettingRow(title:String, settings : Iterable<T>, current : T, onClickItem: (T) -> Unit){
    Row(
        modifier = Modifier.padding(vertical = 10.dp, horizontal = 15.dp),
        verticalAlignment = Alignment.CenterVertically){
        Text(text = "$title :  ")

        LazyRow {
            item {
                settings.forEachIndexed { index, item ->
                    Box(
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .size(25.dp, 25.dp)
                            .clip(shape = MaterialTheme.shapes.small)
                            .border(
                                width = 1.dp,
                                shape = MaterialTheme.shapes.small,
                                color = if (current == item) ColorSet.sky_c1ebfe else Color.Gray
                            )
                            .clickable {
                                onClickItem(item)
                            },
                        contentAlignment = Alignment.Center
                    ){
                        Text(text = "${index+1}", style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray))
                    }
                }
            }
        }
    }
}