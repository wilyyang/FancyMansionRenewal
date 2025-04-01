package com.fancymansion.presentation.bookOverview.home.composables.panel

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.fancymansion.core.presentation.compose.frame.topBarDpMobile
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.modifier.scaleOnPress
import com.fancymansion.core.presentation.compose.shape.RoundedRectangleShape
import com.fancymansion.core.presentation.compose.theme.Paddings
import com.fancymansion.core.presentation.compose.theme.typography.TypeStyles
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
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface),
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
                        .padding(
                            horizontal = Paddings.Basic.horizontal,
                            vertical = Paddings.Basic.vertical
                        )
                    ) {

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
                    }

                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = Paddings.Basic.vertical),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        item {
                            Spacer(modifier = Modifier.width(Paddings.Basic.start))
                        }
                        items(bookInfo.introduce.keywordList) { keyword ->
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
                        item {
                            Spacer(modifier = Modifier.width(10.dp))
                        }
                    }

                    HorizontalDivider(modifier = Modifier
                        .padding(top = 16.dp, start = Paddings.Basic.start, end = Paddings.Basic.end)
                        .height(0.3.dp), color = MaterialTheme.colorScheme.outline)

                    Text(
                        modifier = Modifier
                            .padding(top = 18.dp, start = Paddings.Basic.start, end = Paddings.Basic.end)
                            .width(130.dp)
                            .clip(shape = RoundedRectangleShape())
                            .background(
                                color = Color.Black
                            )
                            .padding(vertical = 12.dp)
                            .clickSingle { onEventSent(OverviewHomeContract.Event.ReadBookButtonClicked) },
                        text = stringResource(id = R.string.button_overview_read_book),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )

                    Column(
                        modifier = Modifier.padding(
                            top = 25.dp,
                            start = Paddings.Basic.start,
                            end = Paddings.Basic.end
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.title_book_introduce),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = TypeStyles.titleSmallVariant,
                            color = Color.Black
                        )

                        Text(
                            modifier = Modifier.padding(top = 16.dp),
                            text = bookInfo.introduce.description,
                            maxLines = 4,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 24.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f)
                        )
                    }
                    Spacer(Modifier.height(500.dp))
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