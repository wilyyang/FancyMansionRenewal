package com.fancymansion.presentation.viewer.content.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
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
import com.fancymansion.core.presentation.base.clickSingle
import com.fancymansion.core.presentation.screen.NoDataScreen
import com.fancymansion.presentation.viewer.R
import com.fancymansion.presentation.viewer.content.ViewerContentContract
import com.fancymansion.presentation.viewer.content.composables.layer.ViewerContentScreenPageContent
import com.fancymansion.presentation.viewer.content.composables.layer.ViewerContentSettingPanel
import kotlinx.coroutines.delay

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

            Box(modifier = modifier) {
                if (panelState == ViewerPanelState.SettingPanel) {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .clickable { panelState = ViewerPanelState.Content })
                }

                ViewerContentScreenPageContent(
                    modifier = Modifier.fillMaxSize(),
                    setting = setting,
                    page = page,
                    selectors = uiState.selectors,
                    onEventSent = onEventSent,
                    listState = listState,
                    onClick = { panelState = ViewerPanelState.SettingPanel }
                )

                AnimatedVisibility(
                    modifier = Modifier.align(Alignment.TopEnd),
                    visible = panelState == ViewerPanelState.SettingPanel,
                    enter = slideInHorizontally { it },
                    exit = slideOutHorizontally { it }
                ) {
                    ViewerContentSettingPanel(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {

                            }
                    )
                }
            }

            LaunchedEffect(listState.firstVisibleItemIndex) {
                if (panelState == ViewerPanelState.SettingPanel && listState.firstVisibleItemIndex > 0) {
                    panelState = ViewerPanelState.Content
                }
            }
        }
    }
}