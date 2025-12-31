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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fancymansion.core.presentation.compose.component.EnumDropdown
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.theme.onSurfaceDimmed
import com.fancymansion.core.presentation.compose.theme.onSurfaceSub
import com.fancymansion.presentation.main.R
import com.fancymansion.presentation.main.tab.editor.EditBookSortOrder
import com.fancymansion.presentation.main.tab.editor.EditBookState
import com.fancymansion.presentation.main.tab.editor.EditorTabContract
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun EditorTabHeader(
    modifier: Modifier,
    isEditMode: Boolean,
    bookSortOrder: EditBookSortOrder,
    focusManager: FocusManager,
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
            onClickDropdownTitle = {
                focusManager.clearFocus()
            },
            getDisplayName = {
                context.getString(it.textResId)
            },
            isEnabled = !isEditMode,
            backgroundColor = if (isEditMode) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.surface
        ) {
            onEventSent(EditorTabContract.Event.SelectBookSortOrder(it))
        }

        Row(
            modifier = Modifier
                .padding(end = 4.dp)
                .align(Alignment.CenterEnd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(isEditMode){
                Text(
                    modifier = Modifier.padding(end = 12.dp).clickSingle {
                        onEventSent(EditorTabContract.Event.BookHolderSelectAll)
                    },
                    text = "전체",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
                )

                Text(
                    modifier = Modifier.padding(end = 12.dp).clickSingle {
                        onEventSent(EditorTabContract.Event.BookHolderDeselectAll)
                    },
                    text = "해제",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
                )

                Text(
                    modifier = Modifier.padding(end = 12.dp).clickSingle {
                        onEventSent(EditorTabContract.Event.BookHolderAddBook)
                    },
                    text = "추가",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
                )

                Text(
                    modifier = Modifier.padding(end = 12.dp).clickSingle {
                        onEventSent(EditorTabContract.Event.BookHolderDeleteBook)
                    },
                    text = "삭제",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
                )

                Text(
                    modifier = Modifier.clickSingle {
                        onEventSent(EditorTabContract.Event.BookListExitEditMode)
                    },
                    text = "완료",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
                )
            } else {
                Text(
                    modifier = Modifier.clickSingle {
                        onEventSent(EditorTabContract.Event.BookListEnterEditMode)
                    },
                    text = "편집",
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
            .padding(vertical = 18.dp, horizontal = 14.dp)
            .clickSingle {
                onClickHolder(bookState.bookInfo.bookId)
            },
        verticalAlignment = Alignment.CenterVertically
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth(0.25f)
                .heightIn(max = 200.dp)
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                painter = painter,
                contentScale = ContentScale.FillWidth,
                contentDescription = ""
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp)
        ) {
            Text(
                text = bookState.bookInfo.title,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                modifier = Modifier.padding(top = 5.dp),
                text = "${formatTimestampLegacy(bookState.bookInfo.editTime)} 편집됨",
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                modifier = Modifier.padding(top = 5.dp),
                text = "총 ${bookState.bookInfo.pageCount} 페이지",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )

            FlowRow(
                modifier = Modifier.padding(top = 5.dp)
            ) {
                bookState.bookInfo.keywords.take(6).forEach { keyword ->
                    Text(
                        modifier = Modifier
                            .padding(end = 2.dp)
                            .padding(bottom = 2.dp)
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
                .padding(5.dp),
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
                        .padding(end = if (page < windowEndExclusive - 1) 30.dp else 0.dp)
                        .clickSingle(
                            enabled = totalPageCount > 0
                        ) { onClickPageNumber(page) },
                    text = "${page + 1}",
                    style = MaterialTheme.typography.bodyLarge.copy(
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
                .padding(5.dp),
            painter = painterResource(R.drawable.ic_list_right),
            contentDescription = "Next window",
            tint = nextColor
        )
    }
}

fun formatTimestampLegacy(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("yyyy.M.d", Locale.getDefault())
    return formatter.format(date)
}