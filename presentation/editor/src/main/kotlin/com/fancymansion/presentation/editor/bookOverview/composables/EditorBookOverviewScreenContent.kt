package com.fancymansion.presentation.editor.bookOverview.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fancymansion.core.common.resource.StringValue
import com.fancymansion.core.presentation.R
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.compose.screen.NoDataScreen
import com.fancymansion.presentation.editor.bookOverview.EditorBookOverviewContract

@Composable
fun EditorBookOverviewScreenContent(
    modifier: Modifier = Modifier,
    uiState: EditorBookOverviewContract.State,
    onEventSent: (event: EditorBookOverviewContract.Event) -> Unit,
    onCommonEventSent: (event: CommonEvent) -> Unit
) {
    uiState.bookInfo?.let { bookInfo ->
        if (bookInfo.id.isBlank() || bookInfo.editor.editorId.isBlank()) {
            Box(
                modifier = modifier
                    .background(color = MaterialTheme.colorScheme.surface)
                    .fillMaxSize(), contentAlignment = Alignment.Center
            ) {

                NoDataScreen(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    option1Title = StringValue.StringResource(resId = R.string.button_back),
                    onClickOption1 = {
                        onCommonEventSent(CommonEvent.CloseEvent)
                    }
                )
            }
        } else {
            Column {

            }

            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)) {

                item { 
                    Spacer(modifier = Modifier.height(4.dp))
                }
                item {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(1200.dp)
                        .background(color = MaterialTheme.colorScheme.surface)){

                    }

                }
            }
        }
    }
}