package com.fancymansion.presentation.bookOverview.home.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fancymansion.core.common.resource.StringValue
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.compose.screen.NoDataScreen
import com.fancymansion.presentation.bookOverview.R
import com.fancymansion.presentation.bookOverview.home.OverviewHomeContract

@Composable
fun OverviewHomeScreenContent(
    modifier: Modifier = Modifier,
    uiState: OverviewHomeContract.State,
    onEventSent: (event: OverviewHomeContract.Event) -> Unit,
    onCommonEventSent: (event: CommonEvent) -> Unit
) {
    uiState.bookInfo?.let { bookInfo ->
        if(bookInfo.id.isBlank() || bookInfo.editor.editorId.isBlank()){
            Box(modifier = modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .fillMaxSize(), contentAlignment = Alignment.Center){

                NoDataScreen(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    titleMessage = StringValue.StringResource(resId = R.string.screen_no_book_info_title),
                    detailMessage = StringValue.StringResource(resId = R.string.screen_no_book_info_detail),
                    option1Title = StringValue.StringResource(resId = com.fancymansion.core.presentation.R.string.button_back),
                    onClickOption1 = {
                        onCommonEventSent(CommonEvent.CloseEvent)
                    }
                )
            }
        }else {

        }
    }
}