package com.fancymansion.presentation.viewer.content.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fancymansion.core.common.resource.StringValue
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.clickSingle
import com.fancymansion.core.presentation.screen.NoDataScreen
import com.fancymansion.presentation.viewer.R
import com.fancymansion.presentation.viewer.content.ViewerContentContract
import com.fancymansion.presentation.viewer.content.composables.layer.ViewerContentScreenPageContent
import com.fancymansion.presentation.viewer.content.composables.layer.ViewerContentSettingPanel

enum class ViewerPanelState {
    Content,
    SettingPanel
}

@Composable
fun ViewerContentScreenContent(
    modifier: Modifier = Modifier,
    uiState: ViewerContentContract.State,
    onEventSent: (event: ViewerContentContract.Event) -> Unit,
    onCommonEventSent: (event: CommonEvent) -> Unit
) {
    uiState.pageWrapper?.let { page ->
        val setting = uiState.pageSetting

        if(page.title.isBlank() && page.sources.isEmpty()){
            Box(modifier = modifier
                .background(color = Color(setting.pageTheme.pageColor.colorCode))
                .fillMaxSize(), contentAlignment = Alignment.Center){

                NoDataScreen(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    titleMessage = StringValue.StringResource(resId = R.string.screen_no_page_title),
                    detailMessage = StringValue.StringResource(resId = R.string.screen_no_page_detail),
                    option1Title = StringValue.StringResource(resId = com.fancymansion.core.presentation.R.string.button_back),
                    onClickOption1 = {
                        onCommonEventSent(CommonEvent.CloseEvent)
                    }
                )
            }
        }else {
            var panelState by remember { mutableStateOf(ViewerPanelState.Content) }
            val listState = rememberLazyListState()

            Box(modifier = Modifier.fillMaxSize()) {
                ViewerContentScreenPageContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickSingle {
                            panelState =
                                if (panelState == ViewerPanelState.SettingPanel) ViewerPanelState.Content else ViewerPanelState.SettingPanel
                        },
                    setting = setting,
                    page = page,
                    selectors = uiState.selectors,
                    listState = listState,
                    onEventSent = onEventSent
                )

                ViewerContentSettingPanel(
                    modifier = Modifier.fillMaxSize(),
                    visible = panelState == ViewerPanelState.SettingPanel,
                    title = "${uiState.bookTitle} ${uiState.episodeTitle.ifBlank { "" }}" ,
                    setting = uiState.pageSetting,
                    onEventSent = onEventSent,
                    onClickBack = { onCommonEventSent(CommonEvent.CloseEvent) }
                )

                LaunchedEffect(listState) {
                    snapshotFlow { listState.isScrollInProgress }
                        .collect { isScrolling ->
                            if (isScrolling && panelState == ViewerPanelState.SettingPanel) {
                                panelState = ViewerPanelState.Content
                            }
                        }
                }

            }
        }
    }
}