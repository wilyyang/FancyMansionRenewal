package com.fancymansion.presentation.viewer.content.composables

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import com.fancymansion.core.common.const.MOBILE_PREVIEW_SPEC
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.core.presentation.base.SIDE_EFFECTS_KEY
import com.fancymansion.core.presentation.frame.BaseScreen
import com.fancymansion.core.presentation.theme.ColorSet
import com.fancymansion.core.presentation.theme.FancyMansionTheme
import com.fancymansion.core.presentation.window.TypePane
import com.fancymansion.presentation.viewer.content.ViewerContentContract
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun ViewerContentScreenFrame(
    uiState: ViewerContentContract.State,
    loadState: LoadState,
    effectFlow: SharedFlow<ViewerContentContract.Effect>?,
    onCommonEventSent: (event: CommonEvent) -> Unit,
    onEventSent: (event: ViewerContentContract.Event) -> Unit,
    onNavigationRequested: (ViewerContentContract.Effect.Navigation) -> Unit
) {
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            if(effect is ViewerContentContract.Effect.Navigation){
                onNavigationRequested(effect)
            }
        }?.collect()
    }

    BaseScreen(
        loadState = loadState,
        description = ViewerContentContract.NAME,
        statusBarColor = ColorSet.sky_c1ebfe,
        typePane = TypePane.SINGLE,
        isOverlayTopBar = true
    ) {
        ViewerContentScreenContent(
            uiState = uiState,
            onEventSent = onEventSent
        )
    }

    BackHandler {
        onCommonEventSent(CommonEvent.CloseEvent)
    }
}



@Preview(device = MOBILE_PREVIEW_SPEC)
@Composable
fun ViewerContentScreenFramePreview(
) {
    FancyMansionTheme {
        ViewerContentScreenFrame(
            uiState = ViewerContentContract.State(),
            loadState = LoadState.Idle,
            effectFlow = null,
            onCommonEventSent = {},
            onEventSent = {},
            onNavigationRequested = {}
        )
    }
}