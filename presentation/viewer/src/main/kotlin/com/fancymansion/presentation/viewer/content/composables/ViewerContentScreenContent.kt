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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.fancymansion.core.common.const.PageColor
import com.fancymansion.core.common.const.PageMarginHorizontal
import com.fancymansion.core.common.const.PageTextSize
import com.fancymansion.core.common.const.PageTheme
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
    val contentTextStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = pageSetting.pageContentSetting.textSize.dpSize.sp, lineHeight = pageSetting.pageContentSetting.lineHeight.dpSize.sp, color = Color(pageSetting.pageTheme.textColor.colorCode))
    val selectorTextStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = pageSetting.selectorSetting.textSize.dpSize.sp, lineHeight = pageSetting.selectorSetting.lineHeight.dpSize.sp, color = Color(pageSetting.pageTheme.selectorTextColor.colorCode))
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
                        modifier = Modifier
                            .padding(vertical = 20.dp)
                            .padding(horizontal = 5.dp),
                        text = pageWrapper.title, style = MaterialTheme.typography.titleLarge
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
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = pageSetting.pageContentSetting.imageMarginHorizontal.dpSize.dp),
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(it.imageFile)

                                    .build(),
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
                            .padding(horizontal = 15.dp, vertical = 10.dp)
                    ) {
                        Text(text = selector.text, style = selectorTextStyle)
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }

                item {
                    Spacer(modifier = Modifier.height(50.dp))
                    
                    Box(modifier = Modifier.height(2.dp).fillMaxWidth().background(color = Color(pageSetting.pageTheme.selectorColor.colorCode)))

                    Row{
                        Text(text = "배경색")
                        PageTheme.entries.forEach {

                            Box(modifier = Modifier
                                .size(30.dp, 30.dp)
                                .background(color = Color(it.pageColor.colorCode))
                                .then(
                                    if (pageSetting.pageTheme.pageColor == it.pageColor) {
                                        Modifier.border(
                                            width = 1.dp,
                                            shape = MaterialTheme.shapes.small,
                                            color = ColorSet.sky_c1ebfe
                                        )
                                    } else {
                                        Modifier
                                    }
                                )
                                .clickable {
                                    onEventSent(ViewerContentContract.Event.ChangePageTheme(it))
                                })
                        }
                    }

                    Row{
                        Text(text = "본문 텍스트 크기")
                        PageTextSize.entries.forEach {
                            Text(modifier = Modifier
                                .size(30.dp, 30.dp)
                                .clickable {
                                    onEventSent(ViewerContentContract.Event.ChangeContentTextSize(it))
                                }, text = it.name)
                        }
                    }

                    Row{
                        Text(text = "이미지 좌우 너비")
                        PageMarginHorizontal.entries.forEach {
                            Text(modifier = Modifier
                                .size(30.dp, 30.dp)
                                .clickable {
                                    onEventSent(ViewerContentContract.Event.ChangeImageMargin(it))
                                }, text = it.name)
                        }
                    }
                }
            }
        }
    }
}