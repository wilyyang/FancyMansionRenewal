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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
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
import com.fancymansion.presentation.main.R
import com.fancymansion.presentation.main.common.MainScreenTab
import com.fancymansion.presentation.main.content.MainContract
import com.fancymansion.presentation.main.tab.editor.EditorTabContract
import com.fancymansion.presentation.main.tab.editor.composables.EditorTabScreenFrame
import com.fancymansion.presentation.main.tab.editor.composables.part.EditBookHolder
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

val TAB_BAR_HEIGHT = 57.dp
val TAB_BUTTON_SIZE = 57.dp

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

    val listStateHome = rememberLazyListState()
    val listStateStudy = rememberLazyListState()

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
                    Column (modifier = Modifier
                        .fillMaxSize().padding(top = 50.dp)){

                        Text(modifier = Modifier.padding(start = 15.dp), text = "홈 화면", style = MaterialTheme.typography.titleLarge)

                        Spacer(modifier = Modifier.height(10.dp))

                        LazyColumn(
                            state = listStateHome,
                            modifier = Modifier
                                .fillMaxSize()
                        ){

                            itemsIndexed (uiState.homeBookList) { idx, data ->
                                EditBookHolder(
                                    bookState = data,
                                    painter = painterResource(id = R.drawable.holder_book_image_no_available),
                                    isEditMode = false,
                                    onClickHolder = {
                                        // TODO
                                    }
                                )

                                if (idx < uiState.homeBookList.size - 1) {
                                    HorizontalDivider(
                                        modifier = Modifier
                                            .padding(horizontal = 14.dp)
                                            .height(0.3.dp)
                                            .fillMaxWidth(),
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }
                            }
                        }
                    }
                }

                MainScreenTab.Study -> {
                    Column (modifier = Modifier
                        .fillMaxSize().padding(top = 50.dp)){

                        Text(modifier = Modifier.padding(start = 15.dp), text = "서재 화면", style = MaterialTheme.typography.titleLarge)

                        Spacer(modifier = Modifier.height(10.dp))

                        LazyColumn(
                            state = listStateHome,
                            modifier = Modifier
                                .fillMaxSize()
                        ){

                            itemsIndexed (uiState.studyBookList) { idx, data ->
                                EditBookHolder(
                                    bookState = data,
                                    painter = painterResource(id = R.drawable.holder_book_image_no_available),
                                    isEditMode = false,
                                    onClickHolder = {
                                        // TODO
                                    }
                                )

                                if (idx < uiState.studyBookList.size - 1) {
                                    HorizontalDivider(
                                        modifier = Modifier
                                            .padding(horizontal = 14.dp)
                                            .height(0.3.dp)
                                            .fillMaxWidth(),
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }
                            }
                        }
                    }
                }

                MainScreenTab.MyInfo -> {
                    Box(modifier = Modifier
                        .fillMaxSize()){
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = "나의 정보 화면",
                            style = MaterialTheme.typography.titleSmall
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
        modifier = modifier.padding(top = 5.5.dp, bottom = 7.5.dp)
    ){
        Image(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .height(28.dp),
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