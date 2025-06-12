package com.fancymansion.presentation.editor.conditionContent.composables

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
import com.fancymansion.presentation.editor.conditionContent.EditorConditionContentContract

@Composable
fun EditorConditionContentScreenContent(
    modifier: Modifier = Modifier,
    uiState: EditorConditionContentContract.State,
    onEventSent: (event: EditorConditionContentContract.Event) -> Unit,
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
        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.onSecondaryContainer)) {

        }
    }
}