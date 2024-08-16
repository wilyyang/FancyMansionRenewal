package com.fancymansion.presentation.viewer.content.composables.layer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.fancymansion.core.presentation.frame.topBarDpMobile
import com.fancymansion.domain.model.book.PageSettingModel
import com.fancymansion.domain.model.book.SelectorModel
import com.fancymansion.presentation.viewer.content.PageWrapper
import com.fancymansion.presentation.viewer.content.SourceWrapper
import com.fancymansion.presentation.viewer.content.ViewerContentContract

@Composable
fun ViewerContentScreenPageContent(
    modifier: Modifier,
    setting: PageSettingModel,
    page : PageWrapper,
    selectors: List<SelectorModel>,
    listState : LazyListState,
    onEventSent: (event: ViewerContentContract.Event) -> Unit
) {
    val titleTextStyle = setting.pageContentSetting.run {
        MaterialTheme.typography.titleLarge.copy(
            fontSize = textSize.dpSize.sp,
            lineHeight = (textSize.dpSize + lineHeight.dpSize).sp,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold
        )
    }

    val contentTextStyle = setting.pageContentSetting.run {
        MaterialTheme.typography.bodyLarge.copy(
            fontSize = textSize.dpSize.sp,
            lineHeight = (textSize.dpSize + lineHeight.dpSize).sp,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Normal
        )
    }

    val selectorTextStyle = setting.selectorSetting.run {
        MaterialTheme.typography.bodyLarge.copy(
            fontSize = textSize.dpSize.sp,
            lineHeight = (textSize.dpSize * 1.1).sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )
    }

     LaunchedEffect(key1 = page) {
         listState.animateScrollToItem(0)
     }

    LazyColumn(
        modifier = modifier.background(color = MaterialTheme.colorScheme.surface),
        state = listState
    ) {
        item {
            Row(
                modifier = Modifier
                    .height(topBarDpMobile)
                    .padding(horizontal = setting.pageContentSetting.textMarginHorizontal.dpSize.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = page.title, style = titleTextStyle
                )
            }
        }
        items(page.sources) {
            when (it) {
                is SourceWrapper.TextWrapper -> {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = setting.pageContentSetting.textMarginHorizontal.dpSize.dp)
                            .fillMaxWidth(),
                        text = it.description,
                        style = contentTextStyle
                    )

                }

                is SourceWrapper.ImageWrapper -> {
                    AsyncImage(
                        modifier = Modifier
                            .padding(horizontal = setting.pageContentSetting.imageMarginHorizontal.dpSize.dp)
                            .fillMaxWidth(),
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
                    .background(color = MaterialTheme.colorScheme.surfaceVariant)
                    .clickable {
                        onEventSent(
                            ViewerContentContract.Event.OnClickSelector(
                                selector.pageId,
                                selector.selectorId
                            )
                        )
                    }
                    .padding(
                        horizontal = 15.dp,
                        vertical = setting.selectorSetting.paddingVertical.dpSize.dp
                    )
            ) {
                Text(text = selector.text, style = selectorTextStyle)
            }
        }
        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}