package com.fancymansion.presentation.editor.selectorList.composables

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
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
import com.fancymansion.presentation.editor.selectorList.EditorSelectorListContract
import com.fancymansion.presentation.editor.selectorList.SelectorState
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun EditorSelectorListScreenFrame(
    uiState: EditorSelectorListContract.State,
    selectorStates : SnapshotStateList<SelectorState>,
    loadState: LoadState,
    effectFlow: SharedFlow<EditorSelectorListContract.Effect>?,
    onCommonEventSent: (event: CommonEvent) -> Unit,
    onEventSent: (event: EditorSelectorListContract.Event) -> Unit,
    onNavigationRequested: (EditorSelectorListContract.Effect.Navigation) -> Unit
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
            if(effect is EditorSelectorListContract.Effect.Navigation){
                onNavigationRequested(effect)
            }
        }?.collect()
    }

    BaseScreen(
        loadState = loadState,
        description = EditorSelectorListContract.NAME,
        statusBarColor = MaterialTheme.colorScheme.surface,
        typePane = TypePane.MOBILE,
        initContent = {
            EditorSelectorListSkeletonScreen()
        },
        topBar = {
            FancyMansionTopBar(
                typePane = TypePane.MOBILE,
                topBarColor = MaterialTheme.colorScheme.surface,
                idLeftIcon = com.fancymansion.core.presentation.R.drawable.ic_back,
                onClickLeftIcon = {
                    onCommonEventSent(CommonEvent.CloseEvent)
                },
                title = stringResource(id = R.string.topbar_editor_title_selector_list),
                subTitle = "${uiState.bookTitle} - ${uiState.pageTitle}",
                sideRightText = if(uiState.isInitSuccess) stringResource(id = R.string.topbar_editor_side_save) else null,
                onClickRightIcon = {
                    onEventSent(EditorSelectorListContract.Event.SelectorSaveToFile)
                },
                shadowElevation = 1.dp
            )
        }
    ) {
        EditorSelectorListScreenContent(
            modifier = Modifier.fillMaxSize(),
            uiState = uiState,
            selectorStates = selectorStates,
            onEventSent = onEventSent,
            onCommonEventSent = onCommonEventSent
        )
    }

    BackHandler {
        onCommonEventSent(CommonEvent.CloseEvent)
    }
}

@Composable
fun EditorSelectorListSkeletonScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface),
    ) {
        FancyMansionTopBar(
            typePane = TypePane.MOBILE,
            topBarColor = MaterialTheme.colorScheme.surface,
            title = stringResource(id = R.string.topbar_editor_title_selector_list),
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

        for (i in 0..10) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(85.dp)
                    .padding(horizontal = 8.dp)
                    .borderLine(
                        density = LocalDensity.current,
                        color = onSurfaceSub,
                        bottom = 0.3.dp
                    )
                    .padding(horizontal = 10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .fillMaxWidth(0.75f)
                ) {

                    FadeInOutSkeleton(
                        modifier = Modifier.height(16.dp).width(180.dp)
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    FadeInOutSkeleton(
                        modifier = Modifier.height(15.6.dp).width(120.dp)
                    )
                }

                Box(
                    modifier = Modifier.align(Alignment.CenterEnd)
                ){
                    FadeInOutSkeleton(
                        modifier = Modifier.height(16.dp).width(60.dp)
                    )
                }
            }
        }
    }
}