package com.fancymansion.presentation.bookOverview.home.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fancymansion.core.common.resource.StringValue
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.screen.NoDataScreen
import com.fancymansion.presentation.bookOverview.R
import com.fancymansion.presentation.bookOverview.home.OverviewHomeContract
import com.fancymansion.presentation.bookOverview.home.composables.panel.OverviewScreenDetailPanel
import com.fancymansion.presentation.bookOverview.home.composables.panel.OverviewScreenHomePanel

enum class OverviewPanelState {
    Home,
    Detail
}

@Composable
fun OverviewHomeScreenContent(
    modifier: Modifier = Modifier,
    uiState: OverviewHomeContract.State,
    onEventSent: (event: OverviewHomeContract.Event) -> Unit,
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
                    titleMessage = StringValue.StringResource(resId = R.string.screen_no_book_info_title),
                    detailMessage = StringValue.StringResource(resId = R.string.screen_no_book_info_detail),
                    option1Title = StringValue.StringResource(resId = com.fancymansion.core.presentation.R.string.button_back),
                    onClickOption1 = {
                        onCommonEventSent(CommonEvent.CloseEvent)
                    }
                )
            }
        } else {
            var panelState by remember { mutableStateOf(OverviewPanelState.Home) }

            Box(modifier = Modifier.fillMaxSize()) {
                // 홈 화면
                OverviewScreenHomePanel(
                    modifier = Modifier.align(Alignment.TopCenter),
                    bookInfo = uiState.bookInfo,
                    showDetailPanel = {
                        panelState = OverviewPanelState.Detail
                    },
                    onEventSent = onEventSent
                )

                // 상세 패널
                AnimatedVisibility(
                    visible = panelState == OverviewPanelState.Detail,
                    enter = fadeIn(animationSpec = tween(durationMillis = 300)),
                    exit = fadeOut(animationSpec = tween(durationMillis = 300))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f))
                            .clickSingle {
                                panelState = OverviewPanelState.Home
                            }
                    )
                }

                AnimatedVisibility(
                    modifier = Modifier
                        .align(Alignment.BottomStart),
                    visible = panelState == OverviewPanelState.Detail,
                    enter = slideInVertically { it },
                    exit = slideOutVertically { it },
                ) {
                    OverviewScreenDetailPanel(
                        modifier = Modifier.align(Alignment.BottomStart),
                        bookInfo = uiState.bookInfo,
                        onEventSent = onEventSent
                    )
                }
            }
        }
    }
}