package com.fancymansion.presentation.editor.routeContent.composables

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
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
import com.fancymansion.presentation.editor.R
import com.fancymansion.presentation.editor.common.ConditionState
import com.fancymansion.presentation.editor.routeContent.EditorRouteContentContract
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@Composable
fun EditorRouteContentScreenFrame(
    uiState: EditorRouteContentContract.State,
    routeConditionStates : SnapshotStateList<ConditionState>,
    loadState: LoadState,
    effectFlow: SharedFlow<EditorRouteContentContract.Effect>?,
    onCommonEventSent: (event: CommonEvent) -> Unit,
    onEventSent: (event: EditorRouteContentContract.Event) -> Unit,
    onNavigationRequested: (EditorRouteContentContract.Effect.Navigation) -> Unit
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

    val focusManager = LocalFocusManager.current
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            if(effect is EditorRouteContentContract.Effect.Navigation){
                onNavigationRequested(effect)
            }
        }?.collect()
    }

    BaseScreen(
        loadState = loadState,
        description = EditorRouteContentContract.NAME,
        statusBarColor = MaterialTheme.colorScheme.surface,
        typePane = TypePane.MOBILE,
        initContent = {
            EditorRouteContentSkeletonScreen()
        },
        topBar = {
            FancyMansionTopBar(
                typePane = TypePane.MOBILE,
                topBarColor = MaterialTheme.colorScheme.surface,
                idLeftIcon = com.fancymansion.core.presentation.R.drawable.ic_back,
                onClickLeftIcon = {
                    focusManager.clearFocus()
                    onCommonEventSent(CommonEvent.CloseEvent)
                },
                // TODO 05.15
                title = stringResource(id = R.string.topbar_editor_title_selector_content),
                subTitle = "${uiState.pageTitle} - ${uiState.selectorText}",
                sideRightText = if(uiState.isInitSuccess) stringResource(id = R.string.topbar_editor_side_save) else null,
                onClickRightIcon = {
                    focusManager.clearFocus()
                    // TODO 05.15
                },
                shadowElevation = 1.dp
            )
        }
    ) {
        EditorRouteContentScreenContent(
            modifier = Modifier.fillMaxSize(),
            uiState = uiState,
            routeConditionStates = routeConditionStates,
            onEventSent = onEventSent,
            onCommonEventSent = onCommonEventSent,
            focusManager = focusManager
        )
    }

    BackHandler {
        onCommonEventSent(CommonEvent.CloseEvent)
    }
}

@Composable
fun EditorRouteContentSkeletonScreen() {
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.secondary),
        ) {

        }
    }
}