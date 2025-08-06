package com.fancymansion.presentation.main.tab.editor.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.core.presentation.base.SIDE_EFFECTS_KEY
import com.fancymansion.core.presentation.base.window.TypePane
import com.fancymansion.core.presentation.compose.frame.tab.TabBaseScreen
import com.fancymansion.presentation.main.tab.editor.EditorTabContract
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun EditorTabScreenFrame(
    uiState: EditorTabContract.State,
    loadState: LoadState,
    effectFlow: SharedFlow<EditorTabContract.Effect>?,
    onCommonEventSent: (event: CommonEvent) -> Unit,
    onEventSent: (event: EditorTabContract.Event) -> Unit,
    onNavigationRequested: (EditorTabContract.Effect.Navigation) -> Unit
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
            if(effect is EditorTabContract.Effect.Navigation){
                onNavigationRequested(effect)
            }
        }?.collect()
    }

    TabBaseScreen(
        loadState = loadState,
        description = EditorTabContract.NAME,
        statusBarColor = MaterialTheme.colorScheme.surface,
        typePane = TypePane.MOBILE,
        initContent = {
            EditorTabSkeletonScreen()
        }
    ) {
        EditorTabScreenContent(
            modifier = Modifier.fillMaxSize(),
            uiState = uiState,
            onEventSent = onEventSent,
            onCommonEventSent = onCommonEventSent
        )
    }
}

@Composable
fun EditorTabSkeletonScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface),
    ) {
    }
}