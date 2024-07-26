package com.fancymansion.presentation.viewer.content.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.fancymansion.presentation.viewer.content.composables.layer.ViewerContentFullPanel
import com.fancymansion.presentation.viewer.content.composables.layer.ViewerContentScreenPageContent
import com.fancymansion.presentation.viewer.content.composables.layer.ViewerContentSettingPanel
import kotlinx.coroutines.delay

const val HIDE_DELAY_MILLIS = 3000L

enum class ViewerPanelState{
    None, FullPanel, SettingPanel
}

class HideDelayRequest(val direct: Boolean = false)

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
            var showFullPanelRequest by remember { mutableStateOf(false) }
            val layerState by remember {
                derivedStateOf {
                    if (showFullPanelRequest) {
                        ViewerPanelState.FullPanel
                    } else {
                        ViewerPanelState.None // 또는 다른 초기 상태
                    }
                }
            }

            var hideLayerRequest by remember {
                mutableStateOf(HideDelayRequest())
            }
            val hideDelayMillis = remember { HIDE_DELAY_MILLIS }
            LaunchedEffect(hideLayerRequest) {
                handleHideLayerRequest(layerState, hideLayerRequest, hideDelayMillis) { newState ->
                    showFullPanelRequest = false
                }
            }

            Box(modifier = modifier) {
                ViewerContentScreenPageContent(
                    modifier = Modifier.fillMaxSize().clickSingle {
                        hideLayerRequest = HideDelayRequest()
                        showFullPanelRequest = true
                    },
                    setting,
                    page,
                    uiState.selectors,
                    onEventSent
                )

                AnimatedVisibility(
                    visible = layerState == ViewerPanelState.FullPanel,
                    enter = fadeIn(animationSpec = tween(durationMillis = 0)),
                    exit = fadeOut()
                ) {
                    ViewerContentFullPanel(modifier = Modifier.fillMaxSize().clickable {
                        hideLayerRequest = HideDelayRequest(direct = true)
                    })

                }

                AnimatedVisibility(
                    modifier = Modifier.align(Alignment.TopEnd),
                    visible = layerState == ViewerPanelState.SettingPanel,
                    enter = slideInHorizontally { it },
                    exit = slideOutHorizontally { it }
                ) {
                    ViewerContentSettingPanel(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}

private suspend fun handleHideLayerRequest(
    currentState: ViewerPanelState,
    request: HideDelayRequest,
    delayMillis: Long,
    onStateChanged: (ViewerPanelState) -> Unit
) {
    if (currentState != ViewerPanelState.None) {
        if (!request.direct) {
            delay(delayMillis)
        }
        onStateChanged(ViewerPanelState.None)
    }
}