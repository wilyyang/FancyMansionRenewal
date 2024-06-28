package com.fancymansion.presentation.viewer.content.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fancymansion.core.presentation.theme.ColorSet
import com.fancymansion.domain.model.book.Source
import com.fancymansion.presentation.viewer.content.ViewerContentContract

@Composable
fun ViewerContentScreenContent(
    modifier: Modifier = Modifier,
    uiState: ViewerContentContract.State,
    onEventSent: (event: ViewerContentContract.Event) -> Unit
) {
    ViewerContentScreenPageContent(modifier, uiState, onEventSent)
}


@Composable
fun ViewerContentScreenPageContent(
    modifier: Modifier,
    uiState: ViewerContentContract.State,
    onEventSent: (event: ViewerContentContract.Event) -> Unit
) {
    val contentTextStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp, lineHeight = 18.sp * 1.2)
    val listState = rememberLazyListState()

    uiState.pageState.page?.let { page ->
        LaunchedEffect(key1 = uiState.pageState) {
            listState.animateScrollToItem(0)
        }


        LazyColumn(
            modifier = modifier,
            state = listState
        ) {
            item {
                Text(
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .padding(horizontal = 5.dp),
                    text = page.title, style = MaterialTheme.typography.titleLarge
                )
            }
            items(page.sources) {
                when (it) {
                    is Source.Text -> {
                        Text(
                            modifier = Modifier.padding(horizontal = 5.dp),
                            text = it.description,
                            style = contentTextStyle
                        )
                    }

                    is Source.Image -> {
                        Image(
                            modifier = Modifier.fillMaxWidth(),
                            contentScale = ContentScale.FillWidth,
                            painter = painterResource(id = it.resId),
                            contentDescription = ""
                        )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
            items(uiState.selectors) { selector ->
                Row(
                    modifier = Modifier
                        .padding(vertical = 10.dp, horizontal = 15.dp)
                        .fillMaxWidth()
                        .clip(
                            shape = MaterialTheme.shapes.small
                        )
                        .background(color = ColorSet.sky_c1ebfe)
                        .clickable {
                            onEventSent(ViewerContentContract.Event.OnClickSelector(page.id, selector.id))
                        }
                        .padding(horizontal = 15.dp, vertical = 10.dp)
                ) {
                    Text(text = selector.text, style = contentTextStyle)
                }
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}