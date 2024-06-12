package com.fancymansion.presentation.viewer.content.composables.tablet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fancymansion.core.common.const.TABLET_PREVIEW_SPEC
import com.fancymansion.core.presentation.R
import com.fancymansion.core.presentation.frame.BaseScreen
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.core.presentation.base.SIDE_EFFECTS_KEY
import com.fancymansion.core.presentation.base.addFocusCleaner
import com.fancymansion.core.presentation.frame.FancyMansionTopBar
import com.fancymansion.core.presentation.frame.topBarDpTablet
import com.fancymansion.core.presentation.theme.ColorSet
import com.fancymansion.core.presentation.theme.FancyMansionTheme
import com.fancymansion.core.presentation.window.TypePane
import com.fancymansion.presentation.viewer.content.LoginContract
import com.fancymansion.presentation.viewer.content.LoginValue
import com.fancymansion.presentation.viewer.content.Navigation
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun LoginScreenFrameTablet(
    uiState: LoginContract.State,
    loadState: LoadState,
    effectFlow: SharedFlow<LoginContract.Effect>?,
    onCommonEventSent: (event: CommonEvent) -> Unit,
    onEventSent: (event: LoginContract.Event) -> Unit,
    onNavigationRequested: (LoginContract.Effect.Navigation) -> Unit
) {
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            when (effect) {
                is LoginContract.Effect.Navigation -> {
                    onNavigationRequested(effect)
                }
            }
        }?.collect()
    }

    BaseScreen(
        loadState = loadState,
        description = Navigation.Routes.LOGIN,
        statusBarColor = ColorSet.sky_c1ebfe,
        typePane = TypePane.DUAL,
        topBar = {
            FancyMansionTopBar(
                typePane = TypePane.DUAL,
                title = stringResource(id = com.fancymansion.core.common.R.string.login_title),
                idRightIcon = R.drawable.img_bar_close_btn,
                onClickRightIcon = {
                    onCommonEventSent(CommonEvent.CloseEvent)
                }
            )
        },
        isOverlayTopBar = true
    ) {
        LoginScreenContentTablet(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = ColorSet.sky_c1ebfe
                )
                .padding(top = topBarDpTablet),
            uiState = uiState,
            onEventSent = onEventSent
        )
    }
}

@Composable
fun LoginScreenContentTablet(
    modifier: Modifier = Modifier,
    uiState: LoginContract.State,
    onEventSent: (event: LoginContract.Event) -> Unit
) {
    val focusManager = LocalFocusManager.current
    Box(
        modifier = modifier.addFocusCleaner(focusManager),
    ) {

        Image(
            modifier = Modifier.align(Alignment.BottomCenter)
                .fillMaxWidth(),
            painter = painterResource(id = R.drawable.img_bar_blue_bg_tablet),
            contentScale = ContentScale.FillWidth,
            contentDescription = null
        )

        LoginScreenCommonTablet(
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 13.5.dp)
                .fillMaxWidth(0.47f),
            focusManager = focusManager,
            uiState = uiState,
            onEventSent = onEventSent
        )
    }
}

@Preview(device = TABLET_PREVIEW_SPEC)
@Composable
fun LoginScreenFrameTabletPreview(
) {
    FancyMansionTheme {
        LoginScreenFrameTablet(
            uiState = LoginContract.State(loginValue = LoginValue()),
            loadState = LoadState.Idle,
            effectFlow = null,
            onCommonEventSent = {},
            onEventSent = {},
            onNavigationRequested = {}
        )
    }
}