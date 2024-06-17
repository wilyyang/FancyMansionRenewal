package com.fancymansion.presentation.viewer.content.composables

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fancymansion.core.common.const.MOBILE_PREVIEW_SPEC
import com.fancymansion.core.presentation.R
import com.fancymansion.core.presentation.frame.BaseScreen
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.core.presentation.base.SIDE_EFFECTS_KEY
import com.fancymansion.core.presentation.base.addFocusCleaner
import com.fancymansion.core.presentation.frame.FancyMansionTopBar
import com.fancymansion.core.presentation.frame.topBarDpMobile
import com.fancymansion.core.presentation.theme.ColorSet
import com.fancymansion.core.presentation.theme.FancyMansionTheme
import com.fancymansion.core.presentation.window.TypePane
import com.fancymansion.presentation.viewer.content.ViewerContentContract
import com.fancymansion.presentation.viewer.content.Navigation
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
        description = Navigation.Routes.VIEWER_CONTENT,
        statusBarColor = ColorSet.sky_c1ebfe,
        typePane = TypePane.SINGLE,
        topBar = {
            FancyMansionTopBar(
                typePane = TypePane.SINGLE,
                title = "임시 타이틀",
                idRightIcon = R.drawable.img_bar_close_btn,
                onClickRightIcon = {
                    onCommonEventSent(CommonEvent.CloseEvent)
                }
            )
        },
        isOverlayTopBar = true
    ) {
        ViewerContentScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = ColorSet.sky_c1ebfe
                )
                .padding(top = topBarDpMobile)
                .imePadding(),
            uiState = uiState,
            onEventSent = onEventSent
        )
    }

    BackHandler {
        onCommonEventSent(CommonEvent.CloseEvent)
    }
}

@Composable
fun ViewerContentScreenContent(
    modifier: Modifier = Modifier,
    uiState: ViewerContentContract.State,
    onEventSent: (event: ViewerContentContract.Event) -> Unit
) {
    val focusManager = LocalFocusManager.current
    LazyColumn(
        modifier = modifier.addFocusCleaner(focusManager),
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Image(
                modifier = Modifier
                    .fillMaxWidth(),
                painter = painterResource(id = R.drawable.img_btn_sky_w527_h123),
                contentScale = ContentScale.FillWidth,
                contentDescription = null
            )

            Spacer(modifier = Modifier.height(20.6.dp))
        }
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