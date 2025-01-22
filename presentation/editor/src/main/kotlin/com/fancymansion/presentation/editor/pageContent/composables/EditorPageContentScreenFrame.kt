package com.fancymansion.presentation.editor.pageContent.composables

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
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
import com.fancymansion.core.presentation.compose.component.FadeInOutSkeleton
import com.fancymansion.core.presentation.compose.frame.BaseScreen
import com.fancymansion.core.presentation.compose.frame.FancyMansionTopBar
import com.fancymansion.core.presentation.compose.shape.borderLine
import com.fancymansion.core.presentation.compose.theme.Paddings
import com.fancymansion.core.presentation.compose.theme.onSurfaceSub
import com.fancymansion.presentation.editor.R
import com.fancymansion.presentation.editor.pageContent.EditorPageContentContract
import com.fancymansion.presentation.editor.pageContent.SourceWrapper
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun EditorPageContentScreenFrame(
    uiState: EditorPageContentContract.State,
    contentSourceStates: SnapshotStateList<SourceWrapper>,
    loadState: LoadState,
    effectFlow: SharedFlow<EditorPageContentContract.Effect>?,
    onCommonEventSent: (event: CommonEvent) -> Unit,
    onEventSent: (event: EditorPageContentContract.Event) -> Unit,
    onNavigationRequested: (EditorPageContentContract.Effect.Navigation) -> Unit
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

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            if(effect is EditorPageContentContract.Effect.Navigation){
                onNavigationRequested(effect)
            }
        }?.collect()
    }

    val focusManager = LocalFocusManager.current
    BaseScreen(
        loadState = loadState,
        description = EditorPageContentContract.NAME,
        statusBarColor = MaterialTheme.colorScheme.surface,
        typePane = TypePane.MOBILE,
        initContent = {
            EditorPageContentSkeletonScreen()
        },
        topBar = {
            FancyMansionTopBar(
                typePane = TypePane.MOBILE,
                topBarColor = MaterialTheme.colorScheme.surface,
                idLeftIcon = com.fancymansion.core.presentation.R.drawable.ic_back,
                onClickLeftIcon = {
                    onCommonEventSent(CommonEvent.CloseEvent)
                },
                title = uiState.title,
                subTitle = stringResource(id = R.string.topbar_editor_sub_title),
                sideRightText = if(uiState.isInitSuccess) stringResource(id = R.string.topbar_editor_side_save) else null,
                onClickRightIcon = {
                    // TODO : Save Page Content Event
                },
                shadowElevation = 1.dp
            )
        }
    ) {
        EditorPageContentScreenContent(
            modifier = Modifier.fillMaxSize(),
            uiState = uiState,
            contentSourceStates = contentSourceStates,
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
fun EditorPageContentSkeletonScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface),
    ) {
        FancyMansionTopBar(
            typePane = TypePane.MOBILE,
            topBarColor = MaterialTheme.colorScheme.surface,
            title = "",
            subTitle = stringResource(id = R.string.topbar_editor_sub_title),
            shadowElevation = 1.dp
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .borderLine(density = LocalDensity.current, color = onSurfaceSub, bottom = 0.3.dp)
                .padding(
                    vertical = Paddings.Basic.vertical,
                    horizontal = Paddings.Basic.horizontal
                )
        ) {
            FadeInOutSkeleton(
                modifier = Modifier.padding(vertical = Paddings.Basic.vertical).height(18.dp).width(100.dp)
            )
        }
    }
}