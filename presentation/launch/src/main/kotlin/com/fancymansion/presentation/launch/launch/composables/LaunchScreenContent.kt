package com.fancymansion.presentation.launch.launch.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.presentation.launch.launch.LaunchContract

@Composable
fun LaunchScreenContent(
    modifier: Modifier = Modifier,
    uiState: LaunchContract.State,
    onEventSent: (event: LaunchContract.Event) -> Unit,
    onCommonEventSent: (event: CommonEvent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        if (!uiState.isInitSuccess) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "초기화 중",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleLarge
            )

        } else {

            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "Launch 화면",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}