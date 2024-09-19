package com.fancymansion.presentation.bookOverview.home.composables.panel

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.fancymansion.domain.model.book.BookInfoModel
import com.fancymansion.presentation.bookOverview.home.OverviewHomeContract

@Composable
fun OverviewContentScreenHome(
    modifier: Modifier,
    bookInfo : BookInfoModel,
    showDetailPanel: () -> Unit,
    onEventSent: (event: OverviewHomeContract.Event) -> Unit
){
    val listState = rememberLazyListState()
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val itemHeight = screenHeight * 0.3f

    LazyColumn(
        modifier = modifier.fillMaxSize().background(color = Color.Yellow),
        state = listState
    ) {
        item {
            Box(modifier = Modifier.fillMaxWidth().height(itemHeight.dp).background(color = Color.Green))
        }
        item {
            Column(
                modifier = Modifier.clickable {
                        showDetailPanel()
                    }
            ) {
                Text(
                    text = bookInfo.introduce.title,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }

        item {
            Column {
                Text(
                    text = bookInfo.introduce.description,
                    style = MaterialTheme.typography.bodyLarge
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