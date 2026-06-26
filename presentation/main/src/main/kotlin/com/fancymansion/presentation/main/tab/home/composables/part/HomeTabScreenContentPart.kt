package com.fancymansion.presentation.main.tab.home.composables.part

import androidx.compose.foundation.Image
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.fancymansion.core.common.util.formatTimestampYearDate
import com.fancymansion.core.presentation.compose.component.EnumDropdown
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.theme.ColorSet
import com.fancymansion.presentation.main.R
import com.fancymansion.presentation.main.tab.home.HomeBookSortOrder
import com.fancymansion.presentation.main.tab.home.HomeBookWrapper
import com.fancymansion.presentation.main.tab.home.HomeTabContract

@Composable
fun HomeTabHeader(
    modifier: Modifier,
    bookSortOrder: HomeBookSortOrder,
    onEventSent: (event: HomeTabContract.Event) -> Unit
) {
    val context = LocalContext.current
    Box(
        modifier = modifier
    ) {
        EnumDropdown(
            modifier = Modifier.width(140.dp),
            options = HomeBookSortOrder.entries.toTypedArray(),
            selectedOption = bookSortOrder,
            getDisplayName = {
                context.getString(it.textResId)
            }
        ) {
            onEventSent(HomeTabContract.Event.SelectBookSortOrder(it))
        }
    }
}

@Composable
fun HomeBookHolder(
    modifier : Modifier = Modifier,
    bookWrapper: HomeBookWrapper,
    onClickHolder : (String) -> Unit
){

    Row(
        modifier = modifier
            .padding(start = 15.5.dp, end = 18.dp)
            .padding(vertical = 18.dp)
            .clickSingle {
                onClickHolder(bookWrapper.bookId)
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
                    painter = rememberAsyncImagePainter(
                        model = bookWrapper.thumbnailUrl
                    ),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = "Book Holder Thumbnail"
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        ) {
            Text(
                text = bookWrapper.title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
            )

            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = stringResource(
                    id = R.string.home_book_holder_update_date,
                    formatTimestampYearDate(bookWrapper.updateTime)
                ),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                modifier = Modifier.padding(top = 3.5.dp),
                text = stringResource(
                    id = R.string.home_book_holder_page_count,
                    bookWrapper.pageCount
                ),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )

            Row(
                modifier = Modifier.padding(top = 3.5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val rating = bookWrapper.averageRating.coerceIn(0f, 5f)
                val starSize = MaterialTheme.typography.bodySmall.lineHeight.value.dp

                repeat(5) { index ->
                    val starResId = when {
                        rating >= index + 1f -> R.drawable.ic_average_star_full
                        rating >= index + 0.5f -> R.drawable.ic_average_star_half
                        else -> R.drawable.ic_average_star_empty
                    }

                    Icon(
                        modifier = Modifier
                            .padding(end = 1.dp)
                            .size(starSize),
                        painter = painterResource(id = starResId),
                        contentDescription = null,
                        tint = ColorSet.yellow_ffa000
                    )
                }

                Text(
                    modifier = Modifier.padding(start = 3.dp),
                    text = "%.1f (%d)".format(rating, bookWrapper.reviewCount),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            FlowRow(
                modifier = Modifier.padding(top = 5.5.dp)
            ) {
                bookWrapper.keywords.take(6).forEach { keyword ->
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