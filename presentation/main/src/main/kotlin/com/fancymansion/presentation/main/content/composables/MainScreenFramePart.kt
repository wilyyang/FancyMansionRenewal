package com.fancymansion.presentation.main.content.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.theme.onSurfaceSub
import com.fancymansion.presentation.main.R
import com.fancymansion.presentation.main.common.formatTimestampLegacy
import com.fancymansion.presentation.main.content.HomeBookState
import com.fancymansion.presentation.main.content.LibraryBookState


@Composable
fun HomeBookHolder(
    modifier : Modifier = Modifier,
    painter: Painter,
    bookState : HomeBookState,
    onClickHolder : (String) -> Unit
){
    Row(
        modifier = modifier
            .padding(start = 15.5.dp, end = 18.dp)
            .padding(vertical = 18.dp)
            .clickSingle {
                onClickHolder(bookState.bookInfo.bookId)
            },
        verticalAlignment = Alignment.CenterVertically
    ){
        Box(
            modifier = Modifier
                .width(70.dp)
                .height(120.dp)
                .clip(shape = MaterialTheme.shapes.extraSmall)
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                painter = painter,
                contentScale = ContentScale.FillWidth,
                contentDescription = "Book Holder Thumbnail"
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        ) {
            Text(
                text = bookState.bookInfo.title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
            )

            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = stringResource(
                    id = R.string.edit_book_holder_edit_date,
                    formatTimestampLegacy(bookState.bookInfo.editTime)
                ),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                modifier = Modifier.padding(top = 3.5.dp),
                text = stringResource(
                    id = R.string.edit_book_holder_page_count,
                    bookState.bookInfo.pageCount
                ),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )

            FlowRow(
                modifier = Modifier.padding(top = 5.5.dp)
            ) {
                bookState.bookInfo.keywords.take(6).forEach { keyword ->
                    Text(
                        modifier = Modifier
                            .padding(end = 2.dp)
                            .padding(bottom = 2.5.dp)
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
        }
    }
}

@Composable
fun LibraryBookHolder(
    modifier : Modifier = Modifier,
    painter: Painter,
    isEditMode: Boolean,
    bookState : LibraryBookState,
    onClickHolder : (String) -> Unit
){

    Row(
        modifier = modifier
            .padding(start = 15.5.dp, end = 18.dp)
            .padding(vertical = 18.dp)
            .clickSingle {
                onClickHolder(bookState.bookInfo.bookId)
            },
        verticalAlignment = Alignment.CenterVertically
    ){
        Box(
            modifier = Modifier
                .width(70.dp)
                .height(120.dp)
                .clip(shape = MaterialTheme.shapes.extraSmall)
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                painter = painter,
                contentScale = ContentScale.FillWidth,
                contentDescription = "Book Holder Thumbnail"
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        ) {
            Text(
                text = bookState.bookInfo.title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
            )

            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = stringResource(
                    id = R.string.edit_book_holder_edit_date,
                    formatTimestampLegacy(bookState.bookInfo.editTime)
                ),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                modifier = Modifier.padding(top = 3.5.dp),
                text = stringResource(
                    id = R.string.edit_book_holder_page_count,
                    bookState.bookInfo.pageCount
                ),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )

            FlowRow(
                modifier = Modifier.padding(top = 5.5.dp)
            ) {
                bookState.bookInfo.keywords.take(6).forEach { keyword ->
                    Text(
                        modifier = Modifier
                            .padding(end = 2.dp)
                            .padding(bottom = 2.5.dp)
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
        }

        Box(
            modifier = Modifier.width(24.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            if (isEditMode) {
                Box(
                    modifier = Modifier
                        .clip(shape = CircleShape)
                        .padding(0.5.dp)
                        .border(
                            width = 0.5.dp,
                            color = onSurfaceSub,
                            shape = CircleShape
                        )
                        .padding(4.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .size(13.dp)
                            .clip(shape = CircleShape)
                            .background(color = if (bookState.selected.value) onSurfaceSub else Color.Transparent)
                    )
                }
            }
        }
    }
}