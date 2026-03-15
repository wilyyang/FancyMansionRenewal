package com.fancymansion.presentation.main.common.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.theme.onSurfaceDimmed
import com.fancymansion.presentation.main.R

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