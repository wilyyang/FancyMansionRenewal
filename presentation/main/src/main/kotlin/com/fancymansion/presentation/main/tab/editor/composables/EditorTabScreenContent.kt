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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class EditBookSortOrder(val textTitle : String) {
    LAST_EDITED ("최근 편집순"),
    TITLE_ASCENDING ("제목 오름차순")
}

data class BookHolderData(
    val title : String,
    val editDate : Long,
    val pageCount : Int,
    val keywords : List<String>
)

@Composable
fun EditorTabScreenContent(
    modifier: Modifier = Modifier,
    uiState: EditorTabContract.State,
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
        val testListData = mutableListOf<BookHolderData>()
        for (i in 0..30) {
            testListData.add(
                BookHolderData(
                    title = "나는 왜 남들보다 쉽게 지칠까 $i",
                    editDate = 1752105600000L,
                    pageCount = 21,
                    keywords = listOf("희망", "절망", "리얼리즘")
                )
            )
        }

        val list1 = testListData.subList(0, 10)
        val list2 = testListData.subList(10, 20)
        val list3 = testListData.subList(20, 30)

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
                                it.textTitle
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

                itemsIndexed (list1){ idx, data ->
                    EditBookHolder(
                        bookHolderData = data
                    )

                    if (idx < list1.size - 1) {
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
    bookHolderData : BookHolderData
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
                text = bookHolderData.title,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                modifier = Modifier.padding(top = 5.dp),
                text = "${formatTimestampLegacy(bookHolderData.editDate)} 편집됨",
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                modifier = Modifier.padding(top = 5.dp),
                text = "총 ${bookHolderData.pageCount} 페이지",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Row(
                modifier = Modifier.padding(top = 5.dp)
            ) {
                bookHolderData.keywords.forEach { keyword ->
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
                        text = "#$keyword",
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