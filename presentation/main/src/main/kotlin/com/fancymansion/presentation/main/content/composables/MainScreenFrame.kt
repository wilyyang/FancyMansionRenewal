package com.fancymansion.presentation.main.content.composables

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.core.presentation.base.SIDE_EFFECTS_KEY
import com.fancymansion.core.presentation.base.tab.TabScreenComponents
import com.fancymansion.core.presentation.compose.shape.borderLine
import com.fancymansion.core.presentation.compose.theme.onSurfaceDimmed
import com.fancymansion.presentation.main.common.MainScreenTab
import com.fancymansion.presentation.main.content.MainContract
import com.fancymansion.presentation.main.tab.editor.EditorTabContract
import com.fancymansion.presentation.main.tab.editor.composables.EditorTabScreenFrame
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

val TAB_BAR_HEIGHT = 58.dp
val TAB_BUTTON_SIZE = 58.dp

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
                MainScreenTab.Home -> {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.tertiary)){
                        Text(
                            modifier = Modifier.align(Alignment.TopStart),
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            text = "위 시작"
                        )
                        Text(
                            modifier = Modifier.align(Alignment.TopEnd),
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            text = "위 끝"
                        )
                        Text(
                            modifier = Modifier.align(Alignment.BottomStart),
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            text = "아래 시작"
                        )
                        Text(
                            modifier = Modifier.align(Alignment.BottomEnd),
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            text = "아래 끝"
                        )
                    }
                }
                // TODO : Tab Test
                MainScreenTab.MyInfo -> {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.tertiaryContainer)){
                        Text(
                            modifier = Modifier.align(Alignment.TopStart),
                            color = MaterialTheme.colorScheme.tertiary,
                            text = "위 시작"
                        )
                        Text(
                            modifier = Modifier.align(Alignment.TopEnd),
                            color = MaterialTheme.colorScheme.tertiary,
                            text = "위 끝"
                        )
                        Text(
                            modifier = Modifier.align(Alignment.BottomStart),
                            color = MaterialTheme.colorScheme.tertiary,
                            text = "아래 시작"
                        )
                        Text(
                            modifier = Modifier.align(Alignment.BottomEnd),
                            color = MaterialTheme.colorScheme.tertiary,
                            text = "아래 끝"
                        )
                    }
                }
            }
        }

        // 하단 탭바
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(TAB_BAR_HEIGHT)
                .background(MaterialTheme.colorScheme.surface)
                .borderLine(
                    density = LocalDensity.current,
                    color = MaterialTheme.colorScheme.outline,
                    top = 1.dp
                )
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            for(tab in MainScreenTab.entries){
                TabButton(
                    modifier = Modifier
                        .size(TAB_BUTTON_SIZE)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            onEventSent(MainContract.Event.TabSelected(tab))
                        },
                    tabInfo = tab,
                    isFocus = tab == uiState.currentTab
                )
            }
        }
    }

    BackHandler {
        onCommonEventSent(CommonEvent.CloseEvent)
    }
}

@Composable
fun TabButton(
    modifier: Modifier = Modifier,
    tabInfo: MainScreenTab,
    isFocus: Boolean
){
    Box(
        modifier = modifier.padding(top = 6.dp, bottom = 8.dp)
    ){
        Image(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .height(30.dp),
            painter =
                if (isFocus) painterResource(id = tabInfo.iconFillResId)
                else painterResource(id = tabInfo.iconResId),
            contentScale = ContentScale.FillHeight,
            contentDescription = tabInfo.tabName
        )
        Text(
            modifier = Modifier.align(Alignment.BottomCenter),
            text = stringResource(tabInfo.titleResId),
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = if (isFocus) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isFocus) MaterialTheme.colorScheme.onSurface else onSurfaceDimmed
            )
        )
    }
}