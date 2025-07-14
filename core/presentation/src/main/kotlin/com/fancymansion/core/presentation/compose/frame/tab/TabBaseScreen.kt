package com.fancymansion.core.presentation.compose.frame.tab

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.fancymansion.core.common.const.ANIMATION_LOADING_FADE_OUT_MS
import com.fancymansion.core.common.const.DELAY_LOADING_FADE_OUT_MS
import com.fancymansion.core.common.const.DELAY_SCREEN_ANIMATION_MS
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.core.presentation.base.window.TypePane
import com.fancymansion.core.presentation.compose.dialog.Loading
import com.fancymansion.core.presentation.compose.frame.CommonPopupLayerProcess
import com.fancymansion.core.presentation.compose.frame.InitShowState
import com.fancymansion.core.presentation.compose.frame.topBarDpMobile
import com.fancymansion.core.presentation.compose.frame.topBarDpTablet
import kotlinx.coroutines.delay

@Composable
fun TabBaseScreen(
    modifier : Modifier = Modifier,
    description : String = "TabBaseScreen",
    statusBarColor : Color? = null,
    isStatusBarTextDark : Boolean = true,
    typePane: TypePane,

    // top bar
    isOverlayTopBar : Boolean = false,
    topBar: @Composable (() -> Unit)? = null,
    topBarHeight: Dp = if(typePane == TypePane.MOBILE) topBarDpMobile else topBarDpTablet,

    // ui state
    loadState : LoadState,
    initContent: (@Composable () -> Unit)? = null,

    content : @Composable () -> Unit
)
{
    val statusBarPaddingDp = with(LocalDensity.current) { WindowInsets.statusBars.getTop(this).toDp() }
    val navigationBarPaddingDp = with(LocalDensity.current) { WindowInsets.navigationBars.getBottom(this).toDp() }

    val view = LocalView.current
    SideEffect {
        (view.context as Activity).window.let {
            WindowCompat.getInsetsController(it, view)
                .isAppearanceLightStatusBars = isStatusBarTextDark
        }
    }

    val initShowState =
        remember { mutableStateOf( if (loadState is LoadState.Init) InitShowState.ScreenAnimationDelay else InitShowState.None ) }

    LaunchedEffect(initShowState.value) {
        when (initShowState.value) {
            InitShowState.ScreenAnimationDelay -> {
                delay(DELAY_SCREEN_ANIMATION_MS)
                initShowState.value = InitShowState.InitShow
            }
            InitShowState.InitShow -> {
                initShowState.value = initShowState.value.transState(loadState)
            }
            InitShowState.InitFadingOut -> {
                delay(DELAY_LOADING_FADE_OUT_MS)
                initShowState.value = InitShowState.None
            }
            else -> {}
        }
    }

    LaunchedEffect(loadState) {
        initShowState.value = initShowState.value.transState(loadState)
    }


    Box(modifier = Modifier
        .fillMaxSize()
        .padding(bottom = navigationBarPaddingDp)) {

        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = if (statusBarColor != null && statusBarColor != Color.Transparent) statusBarPaddingDp else 0.dp)
            ) {
                TabBaseContent(
                    modifier = modifier
                        .semantics {
                            contentDescription = description
                            testTag = loadState.javaClass.simpleName
                        }
                        .background(color = MaterialTheme.colorScheme.surface),

                    isOverlayTopBar = isOverlayTopBar,
                    topBar = topBar,
                    topBarHeight = topBarHeight,

                    initShowState = initShowState.value,
                    initContent = initContent,

                    content = content
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (statusBarColor != null && statusBarColor != Color.Transparent) statusBarPaddingDp else 0.dp)
                    .background(color = statusBarColor ?: Color.Transparent)
            )
        }
    }

    if(initShowState.value == InitShowState.None){
        CommonPopupLayerProcess(
            loadState = loadState
        )
    }

    /**
     * Init.1 initContent == null -> Loading
     * Status Bar를 차지해야함
     */
    if (initShowState.value != InitShowState.None && initContent == null) {
        Loading(
            delayMillis = DELAY_SCREEN_ANIMATION_MS
        )
    }
}

@Composable
fun TabBaseContent(
    modifier : Modifier = Modifier,

    isOverlayTopBar : Boolean = false,
    topBar: @Composable (() -> Unit)? = null,
    topBarHeight: Dp = 0.dp,

    initShowState : InitShowState,
    initContent: (@Composable () -> Unit)? = null,

    content : @Composable () -> Unit
) {

    Box(modifier = modifier) {
        if (initShowState != InitShowState.ScreenAnimationDelay) {
            AnimatedVisibility(
                visible = initShowState == InitShowState.InitFadingOut || initShowState == InitShowState.None,
                enter = fadeIn(animationSpec = tween(durationMillis = ANIMATION_LOADING_FADE_OUT_MS)),
                exit = ExitTransition.None
            ) {
                Box(modifier = Modifier
                    .padding(top = if (isOverlayTopBar || topBar == null) 0.dp else topBarHeight)
                    .fillMaxSize()) {
                    content()
                    if(initShowState != InitShowState.None){
                        Box(modifier = Modifier.fillMaxSize().pointerInput(Unit){})
                    }
                }

                if(topBar != null){
                    topBar()
                }
            }
        }

        val alpha by animateFloatAsState(
            targetValue = if (initShowState == InitShowState.InitFadingOut) 0.0f else 1f,
            animationSpec = tween(durationMillis = ANIMATION_LOADING_FADE_OUT_MS),
            label = "initContentAlpha"
        )

        /**
         * Init.2 initContent != null -> initContent
         * Status Bar를 차지하지 않음
         */
        if (initShowState != InitShowState.None && initContent != null) {
            Box(modifier = Modifier.alpha(alpha)) {
                initContent()
            }
        }
    }
}