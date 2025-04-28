package com.fancymansion.presentation.editor.selectorContent.composables

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.core.presentation.base.SIDE_EFFECTS_KEY
import com.fancymansion.core.presentation.base.window.TypePane
import com.fancymansion.core.presentation.compose.frame.BaseScreen
import com.fancymansion.core.presentation.compose.frame.FancyMansionTopBar
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.presentation.editor.R
import com.fancymansion.presentation.editor.common.ConditionState
import com.fancymansion.presentation.editor.selectorContent.EditorSelectorContentContract
import com.fancymansion.presentation.editor.selectorContent.RouteState
import com.fancymansion.presentation.editor.selectorContent.composables.part.BottomRouteListDialog
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@Composable
fun EditorSelectorContentScreenFrame(
    uiState: EditorSelectorContentContract.State,
    showConditionStates : SnapshotStateList<ConditionState>,
    routeStates : SnapshotStateList<RouteState>,
    loadState: LoadState,
    effectFlow: SharedFlow<EditorSelectorContentContract.Effect>?,
    onCommonEventSent: (event: CommonEvent) -> Unit,
    onEventSent: (event: EditorSelectorContentContract.Event) -> Unit,
    onNavigationRequested: (EditorSelectorContentContract.Effect.Navigation) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val lifecycle = lifecycleOwner.lifecycle
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                onCommonEventSent(CommonEvent.OnResume)
            }
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    val bottomDrawerState = remember { DrawerState(initialValue = DrawerValue.Closed) }
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            if(effect is EditorSelectorContentContract.Effect.Navigation){
                onNavigationRequested(effect)
            }
        }?.collect()
    }

    BaseScreen(
        loadState = loadState,
        description = EditorSelectorContentContract.NAME,
        statusBarColor = MaterialTheme.colorScheme.surface,
        typePane = TypePane.MOBILE,
        initContent = {
            EditorSelectorContentSkeletonScreen()
        },
        topBar = {
            FancyMansionTopBar(
                typePane = TypePane.MOBILE,
                topBarColor = MaterialTheme.colorScheme.surface,
                idLeftIcon = com.fancymansion.core.presentation.R.drawable.ic_back,
                onClickLeftIcon = {
                    onCommonEventSent(CommonEvent.CloseEvent)
                },
                title = stringResource(id = R.string.topbar_editor_title_selector_content),
                subTitle = "${uiState.bookTitle} - ${uiState.pageTitle}",
                sideRightText = if(uiState.isInitSuccess) stringResource(id = R.string.topbar_editor_side_save) else null,
                onClickRightIcon = {
                    focusManager.clearFocus()
                    onEventSent(EditorSelectorContentContract.Event.SelectorSaveToFile)
                },
                shadowElevation = 1.dp
            )
        },
        bottomDrawerState = bottomDrawerState,
        bottomDrawerBackground = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Black.copy(alpha = 0.5f))
                    .clickSingle {
                        coroutineScope.launch {
                            bottomDrawerState.close()
                        }
                    })
        },
        bottomDrawerContent = {
            BottomRouteListDialog(
                routeStates = routeStates,
                onEventSent = onEventSent
            )
        }
    ) {
        EditorSelectorContentScreenContent(
            modifier = Modifier.fillMaxSize(),
            uiState = uiState,
            showConditionStates = showConditionStates,
            routeStates = routeStates,
            onEventSent = onEventSent,
            onCommonEventSent = onCommonEventSent,
            onOpenEditRoute = {
                coroutineScope.launch {
                    bottomDrawerState.open()
                }
            },
            focusManager = focusManager
        )
    }

    BackHandler {
        if(bottomDrawerState.currentValue == DrawerValue.Open){
            coroutineScope.launch {
                bottomDrawerState.close()
            }
        }else{
            onCommonEventSent(CommonEvent.CloseEvent)
        }
    }
}

@Composable
fun EditorSelectorContentSkeletonScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface),
    ) {}
}