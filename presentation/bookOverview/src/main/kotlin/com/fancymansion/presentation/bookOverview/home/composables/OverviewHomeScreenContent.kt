package com.fancymansion.presentation.bookOverview.home.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.presentation.bookOverview.home.OverviewHomeContract

@Composable
fun OverviewHomeScreenContent(
    modifier: Modifier = Modifier,
    uiState: OverviewHomeContract.State,
    onEventSent: (event: OverviewHomeContract.Event) -> Unit,
    onCommonEventSent: (event: CommonEvent) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().background(color = Color.Yellow))

}