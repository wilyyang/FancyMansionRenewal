package com.fancymansion.core.presentation.frame

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fancymansion.core.presentation.base.ChangeStatusBarColor
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.core.presentation.dialog.AlarmDialog
import com.fancymansion.core.presentation.dialog.CustomDialog
import com.fancymansion.core.presentation.dialog.ErrorDialog
import com.fancymansion.core.presentation.dialog.Loading
import com.fancymansion.core.presentation.window.TypePane
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
    topBarHeight: Dp = if(typePane == TypePane.SINGLE) topBarDpMobile else topBarDpTablet,

    // drawer
    drawerState : DrawerState,
    drawerContent : @Composable () -> Unit,

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

    ModalNavigationDrawer(
        // drawer
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerShape = RectangleShape,
                content = {
                    drawerContent()
                }
            )
        },
        gesturesEnabled = drawerState.isOpen,
        content = {
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
        })

    CommonPopupLayerProcess(
        loadState = loadState,
        isLoadingContent = loadingContent != null
    )
}

@Composable
fun BaseScreen(
    modifier : Modifier = Modifier,
    description : String = "BaseScreen",
    containerColor : Color? = null,
    statusBarColor : Color? = null,
    isStatusBarTextDark : Boolean = true,
    typePane: TypePane,

    isOverlayTopBar : Boolean = false,
    topBar: @Composable (() -> Unit)? = null,
    topBarHeight: Dp = if(typePane == TypePane.SINGLE) topBarDpMobile else topBarDpTablet,

    // ui state
    loadState : LoadState,
    loadingContent: (@Composable () -> Unit)? = null,
    isFadeOutLoading: Boolean = false,

    content : @Composable (paddingValues : PaddingValues) -> Unit
) {

    statusBarColor?.let { color ->
        LocalView.current.ChangeStatusBarColor(color = color, isStatusBarTextDark = isStatusBarTextDark)
    }

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
                confirmText = loadState.confirmText.asString(context),
                dismissText = loadState.dismissText?.asString(context),
                onConfirm = loadState.onConfirm,
                onDismiss = loadState.onDismiss
            )
        }
        is LoadState.AlarmDialog -> {
            AlarmDialog(
                title = loadState.title?.asString(context),
                alarmMessage = loadState.message?.asString(context),
                confirmText = loadState.confirmText?.asString(context),
                dismissText = loadState.dismissText?.asString(context),
                onConfirm = loadState.onConfirm,
                onDismiss = loadState.onDismiss
            )
        }

        is LoadState.CustomDialog -> {
            CustomDialog(
                customDialog = loadState.customDialog
            )
        }
        else -> {}
    }
}

enum class LoadingState {
    Loading,
    FadingOut,
    None
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
    val beforeLoadState = remember { mutableStateOf(loadState) }

    val loadingState = remember { mutableStateOf(if(loadState is LoadState.Loading) LoadingState.Loading else LoadingState.None) }

    LaunchedEffect(loadState) {
        if(beforeLoadState.value is LoadState.Loading && loadState is LoadState.Idle){
            loadingState.value = LoadingState.FadingOut
        }else if (loadingState.value == LoadingState.FadingOut) {
            delay(2000)
            loadingState.value = LoadingState.None
        }
        beforeLoadState.value = loadState
    }

    val alpha by animateFloatAsState(
        targetValue = if (loadingState.value == LoadingState.FadingOut) 0f else 1f,
        animationSpec = tween(durationMillis = 2000),
        label = "loadingContentAlpha"
    )

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
                    Box(modifier = Modifier.alpha(alpha)) {
                        if (loadingContent != null) {
                            loadingContent()
                        }
                    }
                }
            }
        }
    )
}