package com.fancymansion.presentation.main.tab.editor.composables.part

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fancymansion.core.common.const.PublishStatus
import com.fancymansion.core.common.util.formatTimestampYearDate
import com.fancymansion.core.common.util.formatTimestampOnlyDate
import com.fancymansion.core.presentation.compose.component.EnumDropdown
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.theme.onSurfaceDimmed
import com.fancymansion.core.presentation.compose.theme.onSurfaceSub
import com.fancymansion.presentation.main.R
import com.fancymansion.presentation.main.tab.editor.EditBookSortOrder
import com.fancymansion.presentation.main.tab.editor.EditBookState
import com.fancymansion.presentation.main.tab.editor.EditorTabContract

@Composable
fun EditorTabHeader(
    modifier: Modifier,
    isEditMode: Boolean,
    bookSortOrder: EditBookSortOrder,
    onEventSent: (event: EditorTabContract.Event) -> Unit
) {
    val context = LocalContext.current
    Box(
        modifier = modifier
    ) {
        EnumDropdown(
            modifier = Modifier.width(140.dp),
            options = EditBookSortOrder.entries.toTypedArray(),
            selectedOption = bookSortOrder,
            getDisplayName = {
                context.getString(it.textResId)
            },
            isEnabled = !isEditMode,
            backgroundColor = if (isEditMode) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.surface
        ) {
            onEventSent(EditorTabContract.Event.SelectBookSortOrder(it))
        }

        Row(
            modifier = Modifier.align(Alignment.CenterEnd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(isEditMode){
                Text(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .clickSingle {
                            onEventSent(EditorTabContract.Event.BookHolderSelectAll)
                        },
                    text = stringResource(id = R.string.edit_book_header_select_all),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
                )

                Text(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .clickSingle {
                            onEventSent(EditorTabContract.Event.BookHolderDeselectAll)
                        },
                    text = stringResource(id = R.string.edit_book_header_deselect_all),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
                )

                Text(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .clickSingle {
                            onEventSent(EditorTabContract.Event.BookHolderAddBook)
                        },
                    text = stringResource(id = R.string.edit_book_header_add),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
                )

                Text(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .clickSingle {
                            onEventSent(EditorTabContract.Event.BookHolderDeleteBook)
                        },
                    text = stringResource(id = R.string.edit_book_header_delete),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
                )

                Text(
                    modifier = Modifier.clickSingle {
                        onEventSent(EditorTabContract.Event.BookListExitEditMode)
                    },
                    text = stringResource(id = R.string.edit_book_header_mode_complete),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
                )
            } else {
                Text(
                    modifier = Modifier.clickSingle {
                        onEventSent(EditorTabContract.Event.BookListEnterEditMode)
                    },
                    text = stringResource(id = R.string.edit_book_header_mode_edit),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
                )
            }
        }
    }
}

@Composable
fun EditBookHolder(
    modifier : Modifier = Modifier,
    painter: Painter,
    isEditMode: Boolean,
    bookState : EditBookState,
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
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .clip(shape = MaterialTheme.shapes.extraSmall)
            ) {
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    painter = painter,
                    contentScale = ContentScale.FillWidth,
                    contentDescription = "Book Holder Thumbnail"
                )

                if (bookState.bookInfo.metadata.status == PublishStatus.PUBLISHED) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .clip(
                                shape = RoundedCornerShape(
                                    topEnd = 4.dp
                                )
                            )
                            .background(color = MaterialTheme.colorScheme.tertiary)
                            .padding(6.dp)
                    ) {
                        Text(
                            text = stringResource(
                                R.string.edit_book_holder_publish_date,
                                formatTimestampOnlyDate(bookState.bookInfo.metadata.updatedAt)
                            ),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onTertiary,
                                fontSize = 10.sp,
                                lineHeight = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
            }
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
                    formatTimestampYearDate(bookState.bookInfo.editTime)
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

private const val VISIBLE_PAGE_COUNT = 5
@Composable
fun BottomBookPagination(
    modifier: Modifier = Modifier,
    currentPage: Int,
    totalPageCount: Int,
    onClickPageNumber: (Int) -> Unit
) {
    val windowStart = (currentPage / VISIBLE_PAGE_COUNT) * VISIBLE_PAGE_COUNT
    val windowEndExclusive =
        if (totalPageCount > 0)
            minOf(windowStart + VISIBLE_PAGE_COUNT, totalPageCount)
        else 1

    val prevPage = windowStart - 1
    val nextPage = windowStart + VISIBLE_PAGE_COUNT

    val canPrev = totalPageCount > 0 && prevPage >= 0
    val canNext = totalPageCount > 0 && nextPage < totalPageCount

    val prevColor = MaterialTheme.colorScheme.onSurface.copy(alpha = if (canPrev) 1f else 0.3f)
    val nextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = if (canNext) 1f else 0.3f)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier
                .padding(end = 18.dp)
                .clip(CircleShape)
                .clickSingle(enabled = canPrev) { onClickPageNumber(prevPage) }
                .border(
                    width = 0.5.dp,
                    color = prevColor,
                    shape = CircleShape
                )
                .padding(5.dp)
                .size(24.dp),
            painter = painterResource(R.drawable.ic_list_left),
            contentDescription = "Previous window",
            tint = prevColor
        )

        Row(
            modifier = Modifier
                .width(180.dp)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            for (page in windowStart until windowEndExclusive) {
                Text(
                    modifier = Modifier
                        .padding(end = if (page < windowEndExclusive - 1) 20.dp else 0.dp)
                        .width(20.dp)
                        .clickSingle(
                            enabled = totalPageCount > 0
                        ) { onClickPageNumber(page) },
                    text = "${page + 1}",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = if (page == currentPage) FontWeight.SemiBold else FontWeight.Medium
                    ),
                    color = if (page == currentPage) MaterialTheme.colorScheme.onSurface else onSurfaceDimmed
                )
            }
        }

        Icon(
            modifier = Modifier
                .padding(start = 18.dp)
                .clip(CircleShape)
                .clickSingle(enabled = canNext) { onClickPageNumber(nextPage) }
                .border(
                    width = 0.5.dp,
                    color = nextColor,
                    shape = CircleShape
                )
                .padding(5.dp)
                .size(24.dp),
            painter = painterResource(R.drawable.ic_list_right),
            contentDescription = "Next window",
            tint = nextColor
        )
    }
}