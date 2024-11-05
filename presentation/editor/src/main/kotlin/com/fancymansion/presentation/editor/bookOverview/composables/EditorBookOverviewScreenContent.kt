package com.fancymansion.presentation.editor.bookOverview.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fancymansion.core.common.resource.StringValue
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.compose.component.RoundedTextField
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.screen.NoDataScreen
import com.fancymansion.core.presentation.compose.shape.borderLine
import com.fancymansion.core.presentation.compose.theme.onSurfaceSub
import com.fancymansion.presentation.editor.R
import com.fancymansion.presentation.editor.bookOverview.EditorBookOverviewContract

val EDIT_ITEM_VERTICAL_PADDING = 5.dp
val EDIT_ITEM_HORIZONTAL_PADDING = 10.dp

@Composable
fun EditorBookOverviewScreenContent(
    modifier: Modifier = Modifier,
    uiState: EditorBookOverviewContract.State,
    onEventSent: (event: EditorBookOverviewContract.Event) -> Unit,
    onCommonEventSent: (event: CommonEvent) -> Unit
) {
    val topTextStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
    uiState.bookInfo?.let { bookInfo ->
        if (bookInfo.id.isBlank() || bookInfo.editor.editorId.isBlank()) {
            Box(
                modifier = modifier
                    .background(color = MaterialTheme.colorScheme.surface)
                    .fillMaxSize(), contentAlignment = Alignment.Center
            ) {

                NoDataScreen(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    option1Title = StringValue.StringResource(resId = com.fancymansion.core.presentation.R.string.button_back),
                    onClickOption1 = {
                        onCommonEventSent(CommonEvent.CloseEvent)
                    }
                )
            }
        } else {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)){

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {

                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = MaterialTheme.colorScheme.surface)
                                .padding(
                                    horizontal = EDIT_ITEM_HORIZONTAL_PADDING,
                                    vertical = 15.dp
                                )
                        ) {

                            Text(
                                modifier = Modifier.padding(
                                    vertical = EDIT_ITEM_VERTICAL_PADDING
                                ),
                                text = stringResource(id = R.string.edit_top_text_book_cover),
                                style = topTextStyle
                            )

                            Image(
                                modifier = Modifier
                                    .padding(
                                        vertical = EDIT_ITEM_VERTICAL_PADDING
                                    )
                                    .clip(shape = MaterialTheme.shapes.small)
                                    .border(
                                        0.5.dp,
                                        color = onSurfaceSub,
                                        shape = MaterialTheme.shapes.medium
                                    )
                                    .size(64.dp)
                                    .padding(10.dp),
                                painter = painterResource(id = R.drawable.ic_gallery_photo),
                                contentDescription = "Gallery"
                            )

                            Spacer(modifier = Modifier.height(15.dp))

                            Text(
                                modifier = Modifier.padding(
                                    vertical = EDIT_ITEM_VERTICAL_PADDING
                                ),
                                text = stringResource(id = R.string.edit_top_text_book_title),
                                style = topTextStyle
                            )

                            RoundedTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        vertical = EDIT_ITEM_VERTICAL_PADDING
                                    ), value = "",
                                hint = stringResource(id = R.string.edit_top_text_book_title_hint)
                            ) {

                            }

                            Spacer(modifier = Modifier.height(15.dp))

                            Text(
                                modifier = Modifier.padding(
                                    vertical = EDIT_ITEM_VERTICAL_PADDING
                                ),
                                text = stringResource(id = R.string.edit_top_text_book_keyword),
                                style = topTextStyle
                            )

                            LazyRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = EDIT_ITEM_VERTICAL_PADDING),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
//                            item {
//                                Spacer(modifier = Modifier.width(startPadding))
//                            }
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

                            Spacer(modifier = Modifier.height(15.dp))

                            Text(
                                modifier = Modifier.padding(
                                    vertical = EDIT_ITEM_VERTICAL_PADDING
                                ),
                                text = stringResource(id = R.string.edit_top_text_book_introduce),
                                style = topTextStyle
                            )

                            RoundedTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        vertical = EDIT_ITEM_VERTICAL_PADDING
                                    ), value = "",
                                minLine = 20,
                                maxLine = 20,
                                hint = stringResource(id = R.string.edit_top_text_book_introduce_hint)
                            ) {

                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }


                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .borderLine(
                            density = LocalDensity.current,
                            color = MaterialTheme.colorScheme.outline,
                            top = 1.dp
                        )
                ) {

                    Box(
                        modifier = Modifier
                            .padding(vertical = 10.dp, horizontal = 8.dp)
                            .fillMaxSize()
                            .clip(
                                shape = MaterialTheme.shapes.extraSmall
                            )
                            .background(color = MaterialTheme.colorScheme.primary)
                            .clickSingle {
                            }
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = "작성 완료",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}