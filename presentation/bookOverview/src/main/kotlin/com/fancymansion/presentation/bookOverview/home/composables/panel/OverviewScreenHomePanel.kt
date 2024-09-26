package com.fancymansion.presentation.bookOverview.home.composables.panel

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.fancymansion.core.presentation.compose.frame.topBarDpMobile
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.domain.model.book.BookInfoModel
import com.fancymansion.presentation.bookOverview.R
import com.fancymansion.presentation.bookOverview.home.OverviewHomeContract
import java.io.File

@Composable
fun OverviewHomeTopBar(
    bookInfo: BookInfoModel,
    surfaceColor: Color,
    titleColor: Color,
    contentColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(topBarDpMobile)
            .background(surfaceColor).clickSingle {  }
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterStart)
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.fillMaxHeight(),
                painter = painterResource(id = com.fancymansion.core.presentation.R.drawable.ic_back),
                tint = contentColor,
                contentDescription = "Back"
            )
            Text(
                modifier = Modifier.padding(horizontal = 5.dp),
                text = bookInfo.introduce.title,
                style = MaterialTheme.typography.titleLarge.copy(color = titleColor),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}

@Composable
fun OverviewScreenHomePanel(
    modifier: Modifier,
    bookInfo: BookInfoModel,
    bookCoverHeightDp : Float,
    coverImageFile: File?,
    showDetailPanel: () -> Unit,
    onEventSent: (event: OverviewHomeContract.Event) -> Unit
) {
    val density = LocalDensity.current
    val colorScheme = MaterialTheme.colorScheme

    val topBarPx = remember { topBarDpMobile.value * density.density }
    val bookCoverHeightPx = remember { bookCoverHeightDp * density.density }
    val topBarShowStartPx = remember { bookCoverHeightPx - topBarPx }

    val listState = rememberLazyListState()

    val alphaColor = remember {
        derivedStateOf {
            when (listState.firstVisibleItemIndex) {
                0 -> maxOf(
                    0f,
                    (listState.firstVisibleItemScrollOffset - topBarShowStartPx) / topBarPx
                )

                else -> 1f
            }
        }
    }

    val topBarSurfaceColor by remember(colorScheme, alphaColor) {
        derivedStateOf {
            colorScheme.surface.copy(alpha = alphaColor.value)
        }
    }
    val topBarTitleColor by remember(colorScheme, alphaColor) {
        derivedStateOf {
            colorScheme.onSurface.copy(alpha = alphaColor.value)
        }
    }
    val topBarContentColor by remember(colorScheme, alphaColor) {
        derivedStateOf {
            colorScheme.onSurface.copy(alpha = alphaColor.value)
                .compositeOver(colorScheme.surface)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Yellow),
            state = listState
        ) {
            item {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(bookCoverHeightDp.dp)
                        .background(color = MaterialTheme.colorScheme.surface)){

                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(coverImageFile)
                            .fallback(R.drawable.img_not_found_file)
                            .build(),
                        contentDescription = "",
                        contentScale = ContentScale.Crop
                    )
                }
            }
            item {
                Column(
                    modifier = Modifier.clickable {
                        showDetailPanel()
                    }
                ) {
                    Text(
                        text = bookInfo.introduce.title,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

            item {
                Column(modifier = Modifier.height(1000.dp)) {
                    Text(text = bookInfo.introduce.description)
                }
            }
        }

        OverviewHomeTopBar(
            bookInfo = bookInfo,
            surfaceColor = topBarSurfaceColor,
            titleColor = topBarTitleColor,
            contentColor = topBarContentColor
        )
    }
}