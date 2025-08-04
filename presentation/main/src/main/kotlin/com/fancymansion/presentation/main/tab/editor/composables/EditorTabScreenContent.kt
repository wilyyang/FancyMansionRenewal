package com.fancymansion.presentation.main.tab.editor.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.dp
import com.fancymansion.core.common.resource.StringValue
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.compose.component.EnumDropdown
import com.fancymansion.core.presentation.compose.component.RoundedTextField
import com.fancymansion.core.presentation.compose.screen.NoDataScreen
import com.fancymansion.core.presentation.compose.shape.borderLine
import com.fancymansion.core.presentation.compose.theme.onSurfaceDimmed
import com.fancymansion.presentation.main.tab.editor.EditorTabContract
import com.fancymansion.presentation.main.R
import com.fancymansion.presentation.main.tab.editor.EditBookSortOrder
import com.fancymansion.presentation.main.tab.editor.EditBookState
import com.fancymansion.presentation.main.tab.editor.EditBookWrapper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun EditorTabScreenContent(
    modifier: Modifier = Modifier,
    uiState: EditorTabContract.State,
    bookInfoStates: SnapshotStateList<EditBookState>,
    onEventSent: (event: EditorTabContract.Event) -> Unit,
    onCommonEventSent: (event: CommonEvent) -> Unit
) {
    if (!uiState.isInitSuccess) {
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
        val context = LocalContext.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RoundedTextField(
                    modifier = Modifier.fillMaxWidth(0.85f),
                    value = ""
                ) {}

                Box(modifier = Modifier.fillMaxWidth(1f)) {
                    Text(
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .align(Alignment.CenterEnd),
                        text = "취소",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .borderLine(
                                density = LocalDensity.current,
                                color = MaterialTheme.colorScheme.outline,
                                bottom = 1.dp
                            )
                            .padding(vertical = 18.dp, horizontal = 14.dp)
                    ) {
                        Text(
                            text = "작품",
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }


                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 18.dp, horizontal = 14.dp)
                    ){
                        EnumDropdown(
                            modifier = Modifier.width(125.dp),
                            options = EditBookSortOrder.entries.toTypedArray(),
                            selectedOption = EditBookSortOrder.LAST_EDITED,
                            getDisplayName = {
                                context.getString(it.textResId)
                            }
                        ) {

                        }

                        Text(
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .align(Alignment.CenterEnd),
                            text = "편집",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }

                itemsIndexed (bookInfoStates){ idx, data ->
                    EditBookHolder(
                        bookState = data
                    )

                    if (idx < bookInfoStates.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier
                                .padding(horizontal = 14.dp)
                                .height(0.3.dp)
                                .fillMaxWidth(),
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }

                item{
                    Row(
                        modifier = Modifier
                            .padding(vertical = 50.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ){
                        for(i in 1 .. 3){
                            Text(
                                modifier = Modifier.padding(end = if(i != 3) 30.dp else 0.dp),
                                text = "$i",
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = if(i == 1) SemiBold else Medium),
                                color = if(i == 1) MaterialTheme.colorScheme.onSurface else onSurfaceDimmed
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EditBookHolder(
    modifier : Modifier = Modifier,
    bookState : EditBookState
){
    Row(
        modifier = modifier.padding(vertical = 18.dp, horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Image(
            modifier = Modifier
                .fillMaxWidth(0.25f)
                .heightIn(max = 200.dp),
            painter = painterResource(id = R.drawable.holder_book_image_sample),
            contentScale = ContentScale.FillWidth,
            contentDescription = ""
        )

        Column(
            modifier = Modifier
                .fillMaxWidth(1f)
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

            Row(
                modifier = Modifier.padding(top = 5.dp)
            ) {
                bookState.bookInfo.keywords.forEach { keyword ->
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
            }
        }
    }
}

fun formatTimestampLegacy(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("yyyy.M.d", Locale.getDefault())
    return formatter.format(date)
}