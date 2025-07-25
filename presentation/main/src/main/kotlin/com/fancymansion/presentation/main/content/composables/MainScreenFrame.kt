package com.fancymansion.presentation.main.content.composables

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.core.presentation.base.SIDE_EFFECTS_KEY
import com.fancymansion.core.presentation.base.tab.TabScreenComponents
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.theme.onSurfaceSub
import com.fancymansion.presentation.main.common.MainScreenTab
import com.fancymansion.presentation.main.content.MainContract
import com.fancymansion.presentation.main.tab.editor.EditorTabContract
import com.fancymansion.presentation.main.tab.editor.composables.EditorTabScreenFrame
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun MainScreenFrame(
    uiState: MainContract.State,
    loadState: LoadState,
    effectFlow: SharedFlow<MainContract.Effect>?,
    onCommonEventSent: (event: CommonEvent) -> Unit,
    onEventSent: (event: MainContract.Event) -> Unit,
    onNavigationRequested: (MainContract.Effect.Navigation) -> Unit,
    editorTabComponents: TabScreenComponents<EditorTabContract.State, EditorTabContract.Event, EditorTabContract.Effect>
) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val navigationBarPaddingDp = with(LocalDensity.current) { WindowInsets.navigationBars.getBottom(this).toDp() }
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
            if(effect is MainContract.Effect.Navigation){
                onNavigationRequested(effect)
            }
        }?.collect()
    }

    // TODO 07.14 탭 적용
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(bottom = navigationBarPaddingDp)) {
        Box(modifier = Modifier.weight(1f)) {
            when (uiState.currentTab) {
                // TODO : Tab Test
                MainScreenTab.Home -> {
                    Box(modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.tertiary))
                }
                MainScreenTab.Editor -> {
                    EditorTabScreenFrame(
                        uiState = editorTabComponents.uiState,
                        loadState = editorTabComponents.loadState,
                        effectFlow = editorTabComponents.effectFlow,
                        onEventSent = editorTabComponents.onEventSent,
                        onCommonEventSent = editorTabComponents.onCommonEventSent,
                        onNavigationRequested = editorTabComponents.onNavigationRequested
                    )
                }
                // TODO : Tab Test
                MainScreenTab.MyInfo -> {
                    Box(modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.tertiaryContainer))}
            }
        }

        // 하단 탭바
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(MaterialTheme.colorScheme.surface),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // TODO : Tab Test Home
            Column(
                modifier = Modifier
                    .size(50.dp)
                    .background(color = MaterialTheme.colorScheme.secondary)
                    .clickable {
                        onEventSent(MainContract.Event.TabSelected(MainScreenTab.Home))
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    tint = if (uiState.currentTab is MainScreenTab.Home) MaterialTheme.colorScheme.onSurface else onSurfaceSub,
                    contentDescription = "Home"
                )
                Text(text = "Home", style = MaterialTheme.typography.labelSmall)
            }

            // Editor Tab 버튼
            Column(
                modifier = Modifier
                    .size(50.dp)
                    .background(color = MaterialTheme.colorScheme.secondary)
                    .clickable {
                        onEventSent(MainContract.Event.TabSelected(MainScreenTab.Editor))
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    tint = if (uiState.currentTab is MainScreenTab.Editor) MaterialTheme.colorScheme.onSurface else onSurfaceSub,
                    contentDescription = "Editor"
                )
                Text(text = "Editor", style = MaterialTheme.typography.labelSmall)
            }

            // 다른 탭 버튼도 여기에 추가 가능

            // TODO : Tab Test MyInfo
            Column(
                modifier = Modifier
                    .size(50.dp)
                    .background(color = MaterialTheme.colorScheme.secondary)
                    .clickable {
                        onEventSent(MainContract.Event.TabSelected(MainScreenTab.MyInfo))
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    tint = if (uiState.currentTab is MainScreenTab.MyInfo) MaterialTheme.colorScheme.onSurface else onSurfaceSub,
                    contentDescription = "Info"
                )
                Text(text = "Info", style = MaterialTheme.typography.labelSmall)
            }
        }
    }

    BackHandler {
        onCommonEventSent(CommonEvent.CloseEvent)
    }
}