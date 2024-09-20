package com.fancymansion.presentation.bookOverview.home.composables.panel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fancymansion.domain.model.book.BookInfoModel
import com.fancymansion.presentation.bookOverview.home.OverviewHomeContract

@Composable
fun OverviewScreenDetailPanel(
    modifier: Modifier,
    bookInfo : BookInfoModel,
    onEventSent: (event: OverviewHomeContract.Event) -> Unit
) {
    val listState = rememberLazyListState()

    LazyColumn(
        modifier = modifier.fillMaxWidth().fillMaxHeight(0.7f).background(color = Color.Blue),
        state = listState
    ) {
        item {
            Column {
                Text(
                    text = bookInfo.id
                )

                Text(
                    text = bookInfo.editor.editorName
                )

                Text(
                    text = bookInfo.introduce.description
                )

                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = bookInfo.introduce.description,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}