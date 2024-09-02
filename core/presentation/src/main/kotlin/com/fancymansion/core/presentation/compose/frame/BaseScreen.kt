package com.fancymansion.core.presentation.compose.frame

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fancymansion.core.common.const.ANIMATION_LOADING_FADE_OUT_MS
import com.fancymansion.core.common.const.DELAY_LOADING_FADE_OUT_MS
import com.fancymansion.core.common.const.DELAY_LOADING_SHOW_MS
import com.fancymansion.core.presentation.compose.util.ChangeStatusBarColor
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.core.presentation.compose.dialog.AlarmDialog
import com.fancymansion.core.presentation.compose.dialog.ErrorDialog
import com.fancymansion.core.presentation.compose.dialog.Loading
import com.fancymansion.core.presentation.base.window.TypePane
import kotlinx.coroutines.delay

@Composable
fun BaseScreen(
    modifier : Modifier = Modifier,
    description : String = "BaseScreen",
    containerColor : Color? = null,
    statusBarColor : Color? = null,
    isStatusBarTextDark : Boolean = true,
    typePane: TypePane,

    // top bar
    isOverlayTopBar : Boolean = false,
    topBar: @Composable (() -> Unit)? = null,
    topBarHeight: Dp = if(typePane == TypePane.MOBILE) topBarDpMobile else topBarDpTablet,

    // drawer
    leftDrawerState: DrawerState? = null,
    leftDrawerContent: (@Composable () -> Unit)? = null,
    rightDrawerState: DrawerState? = null,
    rightDrawerContent: (@Composable () -> Unit)? = null,
    bottomDrawerState: DrawerState? = null,
    bottomDrawerContent: (@Composable () -> Unit)? = null,

    // ui state
    loadState : LoadState,
    loadingContent: (@Composable () -> Unit)? = null,
    isFadeOutLoading: Boolean = false,

    content : @Composable (paddingValues : PaddingValues) -> Unit
)
{
    statusBarColor?.let { color ->
        LocalView.current.ChangeStatusBarColor(color = color, isStatusBarTextDark = isStatusBarTextDark)
    }

    SideDrawer(
        leftDrawerState = leftDrawerState,
        leftDrawerContent = leftDrawerContent,
        rightDrawerState = rightDrawerState,
        rightDrawerContent = rightDrawerContent,
        bottomDrawerState = bottomDrawerState,
        bottomDrawerContent = bottomDrawerContent
    ){
        BaseContent(
            modifier = modifier.semantics {
                contentDescription = description
                testTag = loadState.javaClass.simpleName
            },
            containerColor = containerColor,

            isOverlayTopBar = isOverlayTopBar,
            topBar = topBar,
            topBarHeight = topBarHeight,

            loadState = loadState,
            loadingContent = loadingContent,
            isFadeOutLoading = isFadeOutLoading,

            content = content
        )
    }

    CommonPopupLayerProcess(
        loadState = loadState,
        isLoadingContent = loadingContent != null
    )
}

@Composable
fun CommonPopupLayerProcess(
    loadState : LoadState,
    isLoadingContent : Boolean
) {
    val context = LocalContext.current
    when(loadState){
        is LoadState.Loading -> {
            if(!isLoadingContent){
                Loading(
                    loadingMessage = loadState.message
                )
            }
        }
        is LoadState.ErrorDialog -> {
            ErrorDialog(
                title = loadState.title?.asString(context),
                message = loadState.message?.asString(context),
                errorMessage = loadState.errorMessage?.asString(context),
                background = loadState.backgroundColorCode?.let { Color(it) },
                confirmText = loadState.confirmText?.asString(context),
                dismissText = loadState.dismissText?.asString(context),
                onConfirm = loadState.onConfirm,
                onDismiss = loadState.onDismiss
            )
        }
        is LoadState.AlarmDialog -> {
            AlarmDialog(
                title = loadState.title?.asString(context),
                message = loadState.message?.asString(context),
                background = loadState.backgroundColorCode?.let { Color(it) },
                confirmText = loadState.confirmText?.asString(context),
                dismissText = loadState.dismissText?.asString(context),
                onConfirm = loadState.onConfirm,
                onDismiss = loadState.onDismiss
            )
        }
        else -> {}
    }
}

enum class ShowLoadingState {
    None,
    LoadingShowDelay,
    LoadingShow,
    FadingOut
}

@Composable
fun BaseContent(
    modifier : Modifier = Modifier,
    containerColor : Color? = null,

    isOverlayTopBar : Boolean = false,
    topBar: @Composable (() -> Unit)? = null,
    topBarHeight: Dp = 0.dp,

    loadState : LoadState,
    loadingContent: (@Composable () -> Unit)? = null,
    isFadeOutLoading: Boolean = false,

    content : @Composable (paddingValues : PaddingValues) -> Unit
) {

    Scaffold(
        modifier = modifier,
        containerColor = containerColor ?: MaterialTheme.colorScheme.background,

        // top bar
        topBar = {
            if(topBar != null){
                topBar()
            }
        },
        content = {
            Column(modifier = Modifier
                .padding(top = if (isOverlayTopBar || topBar == null) 0.dp else topBarHeight)
                .fillMaxSize()) {

                Box {
                    content(it)
                    if (loadingContent != null) {
                        val showLoadingState = remember {
                            mutableStateOf(
                                if (loadState is LoadState.Loading) ShowLoadingState.LoadingShowDelay
                                else ShowLoadingState.None
                            )
                        }

                        LaunchedEffect(loadState) {
                            showLoadingState.value = when(loadState){
                                is LoadState.Loading -> {
                                    ShowLoadingState.LoadingShowDelay
                                }
                                is LoadState.Idle -> {
                                    if (showLoadingState.value == ShowLoadingState.LoadingShow && isFadeOutLoading
                                    ) {
                                        ShowLoadingState.FadingOut
                                    } else {
                                        ShowLoadingState.None
                                    }
                                }
                                else -> ShowLoadingState.None
                            }
                        }

                        LaunchedEffect(showLoadingState.value) {
                            if(showLoadingState.value == ShowLoadingState.LoadingShowDelay){
                                delay(DELAY_LOADING_SHOW_MS)
                                if(showLoadingState.value == ShowLoadingState.LoadingShowDelay){
                                    showLoadingState.value = ShowLoadingState.LoadingShow
                                }
                            }else if (showLoadingState.value == ShowLoadingState.FadingOut) {
                                delay(DELAY_LOADING_FADE_OUT_MS)
                                if(showLoadingState.value == ShowLoadingState.FadingOut){
                                    showLoadingState.value = ShowLoadingState.None
                                }
                            }
                        }

                        val alpha by animateFloatAsState(
                            targetValue = if (showLoadingState.value == ShowLoadingState.FadingOut) 0.0f else 1f,
                            animationSpec = tween(durationMillis = ANIMATION_LOADING_FADE_OUT_MS),
                            label = "loadingContentAlpha"
                        )

                        if(showLoadingState.value == ShowLoadingState.LoadingShowDelay || showLoadingState.value == ShowLoadingState.LoadingShow){
                            Surface(modifier = Modifier.fillMaxSize(), color = Color.Transparent) {}
                        }

                        if(showLoadingState.value == ShowLoadingState.LoadingShow || showLoadingState.value == ShowLoadingState.FadingOut){
                            Box(modifier = Modifier.alpha(alpha)) {
                                loadingContent()
                            }
                        }
                    }
                }
            }
        }
    )
}