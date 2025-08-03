package com.fancymansion.presentation.main.tab.editor.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.fancymansion.core.common.resource.StringValue
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.compose.component.EnumDropdown
import com.fancymansion.core.presentation.compose.component.RoundedTextField
import com.fancymansion.core.presentation.compose.screen.NoDataScreen
import com.fancymansion.core.presentation.compose.shape.borderLine
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
        val testListData = listOf(BookHolderData(
            title = "나는 왜 남들보다 쉽게 지칠까",
            editDate = 1752105600000L,
            pageCount = 21,
            keywords = listOf("희망", "절망", "리얼리즘")
        ),
            BookHolderData(
                title = "나는 왜 남들보다 쉽게 지칠까",
                editDate = 1752105600000L,
                pageCount = 21,
                keywords = listOf("희망", "절망", "리얼리즘")
            ))
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

                items(testListData){ data ->
                    EditBookHolder(
                        bookHolderData = data
                    )
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
            modifier = Modifier.fillMaxWidth(0.25f).heightIn(max = 200.dp),
            painter = painterResource(id = R.drawable.holder_book_image_sample),
            contentScale = ContentScale.FillWidth,
            contentDescription = ""
        )

        Column(
            modifier = Modifier.fillMaxWidth(1f).padding(start = 10.dp)
        ) {
            Text(
                text = bookHolderData.title,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "${formatTimestampLegacy(bookHolderData.editDate)} 편집됨",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

fun formatTimestampLegacy(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("yyyy.M.d", Locale.getDefault())
    return formatter.format(date)
}