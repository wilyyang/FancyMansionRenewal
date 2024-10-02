package com.fancymansion.presentation.bookOverview.home.composables.panel

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.draw.clip
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
import com.fancymansion.core.presentation.compose.modifier.scaleOnPress
import com.fancymansion.core.presentation.compose.theme.ColorSet
import com.fancymansion.domain.model.book.BookInfoModel
import com.fancymansion.presentation.bookOverview.R
import com.fancymansion.presentation.bookOverview.home.OverviewHomeContract
import com.fancymansion.presentation.bookOverview.home.composables.detailPanelCornerHeight
import com.fancymansion.presentation.bookOverview.home.composables.detailPanelShape
import java.io.File

@Composable
fun OverviewHomeTopBar(
    modifier: Modifier = Modifier,
    statusBarPaddingDp : Float,
    bookInfo: BookInfoModel,
    surfaceColor: Color,
    titleColor: Color,
    contentColor: Color,
    onClickBack: () -> Unit
) {
    val backInteractionSource = remember { MutableInteractionSource() }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(topBarDpMobile + statusBarPaddingDp.dp)
            .background(surfaceColor)
            .padding(top = statusBarPaddingDp.dp)
            .clickSingle { }
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterStart)
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .fillMaxHeight()
                    .clickSingle(
                        onClick = onClickBack,
                        interactionSource = backInteractionSource
                    )
                    .scaleOnPress(
                        interactionSource = backInteractionSource,
                        pressScale = 0.9f
                    ),
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
    statusBarPaddingDp : Float,
    coverImageFile: File?,
    showDetailPanel: () -> Unit,
    onClickBack: () -> Unit,
    onEventSent: (event: OverviewHomeContract.Event) -> Unit
) {
    val density = LocalDensity.current
    val listState = rememberLazyListState()
    val bookCoverExceptTopBarDp = remember { (bookCoverHeightDp - topBarDpMobile.value - detailPanelCornerHeight) }
    val alphaColor = remember {
        derivedStateOf {
            when (listState.firstVisibleItemIndex) {
                0 -> {
                    val offsetDp = with(density) { listState.firstVisibleItemScrollOffset.toDp() }.value
                    val temp = (offsetDp - bookCoverExceptTopBarDp ) / topBarDpMobile.value
                    when {
                        temp > 1f -> 1f
                        temp < 0f -> 0f
                        else -> temp
                    }
                }

                else -> 1f
            }
        }
    }

    val colorScheme = MaterialTheme.colorScheme
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
            modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.surface),
            state = listState
        ) {
            item {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((bookCoverHeightDp + statusBarPaddingDp).dp)
                        .background(color = MaterialTheme.colorScheme.background)){

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
                // Bottom Round Corner Area
                Box {
                    Column(
                        modifier = Modifier
                            .offset(y = -(detailPanelCornerHeight).dp)
                            .height(detailPanelCornerHeight.dp)
                            .fillMaxWidth()
                            .clip(shape = detailPanelShape)
                            .background(color = MaterialTheme.colorScheme.surface)
                    ){}

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .height(detailPanelCornerHeight.dp)
                            .fillMaxWidth()
                            .background(color = Color.Cyan)
                            .background(color = MaterialTheme.colorScheme.surface)
                    ){}
                }
            }

            item {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 14.dp, end = 12.dp)
                        .padding(vertical = 6.dp)) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(0.9f),
                                text = bookInfo.introduce.title,
                                color = Color.Black,
                                style = MaterialTheme.typography.headlineSmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Box(modifier = Modifier.fillMaxWidth()) {
                                Icon(
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .height(MaterialTheme.typography.headlineSmall.lineHeight.value.dp)
                                        .clickSingle {
                                            showDetailPanel()
                                        },
                                    painter = painterResource(id = com.fancymansion.core.presentation.R.drawable.ic_arrow_down_semi_bold),
                                    tint = Color.Black,
                                    contentDescription = "Expand"
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier.height(MaterialTheme.typography.bodyLarge.lineHeight.value.dp),
                                painter = painterResource(id = com.fancymansion.core.presentation.R.drawable.ic_star_fill),
                                tint = ColorSet.red_dc3232,
                                contentDescription = "Star"
                            )

                            Text(
                                text = "4.9점",
                                color = ColorSet.red_dc3232,
                                style = MaterialTheme.typography.bodyLarge
                            )

                            Text(
                                text = "(2051)",
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }

        OverviewHomeTopBar(
            statusBarPaddingDp = statusBarPaddingDp,
            bookInfo = bookInfo,
            surfaceColor = topBarSurfaceColor,
            titleColor = topBarTitleColor,
            contentColor = topBarContentColor,
            onClickBack = onClickBack
        )
    }
}