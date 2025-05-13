package com.fancymansion.presentation.editor.selectorContent.composables

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.core.presentation.base.SIDE_EFFECTS_KEY
import com.fancymansion.core.presentation.base.window.TypePane
import com.fancymansion.core.presentation.compose.component.FadeInOutSkeleton
import com.fancymansion.core.presentation.compose.component.RoundedTextField
import com.fancymansion.core.presentation.compose.frame.BaseScreen
import com.fancymansion.core.presentation.compose.frame.FancyMansionTopBar
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.shape.borderLine
import com.fancymansion.core.presentation.compose.theme.Paddings
import com.fancymansion.core.presentation.compose.theme.onSurfaceSub
import com.fancymansion.presentation.editor.R
import com.fancymansion.presentation.editor.common.ConditionState
import com.fancymansion.presentation.editor.common.composables.CommonEditInfoTitle
import com.fancymansion.presentation.editor.common.itemMarginHeight
import com.fancymansion.presentation.editor.selectorContent.EditorSelectorContentContract
import com.fancymansion.presentation.editor.selectorContent.RouteState
import com.fancymansion.presentation.editor.selectorContent.composables.part.BottomRouteListDialog
import com.fancymansion.presentation.editor.selectorContent.composables.part.SelectorContentHeader
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
                    focusManager.clearFocus()
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
                focusManager.clearFocus()
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
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface),
        ) {
            FancyMansionTopBar(
                typePane = TypePane.MOBILE,
                topBarColor = MaterialTheme.colorScheme.surface,
                title = stringResource(id = R.string.topbar_editor_title_selector_content),
                subTitle = stringResource(id = R.string.topbar_editor_sub_title),
                shadowElevation = 1.dp
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .borderLine(density = LocalDensity.current, color = MaterialTheme.colorScheme.outline, bottom = 0.3.dp)
                    .padding(
                        vertical = Paddings.Basic.vertical,
                        horizontal = Paddings.Basic.horizontal
                    )
            ) {
                FadeInOutSkeleton(
                    modifier = Modifier.padding(vertical = Paddings.Basic.vertical).height(18.dp).width(145.dp)
                )
            }

            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Paddings.Basic.horizontal)
                .padding(top = Paddings.Basic.vertical))
            {
                CommonEditInfoTitle(
                    title = stringResource(id = R.string.edit_selector_content_top_label_selector_text)
                )

                RoundedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = Paddings.Basic.vertical
                        ),
                    value = "",
                    isEnabled = false
                ) {}

                Spacer(modifier = Modifier.height(itemMarginHeight))
            }

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                CommonEditInfoTitle(
                    modifier = Modifier
                        .padding(vertical = Paddings.Basic.vertical)
                        .padding(start = Paddings.Basic.horizontal),
                    title = stringResource(id = R.string.edit_selector_content_top_label_show_condition)
                )

                Text(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(horizontal = Paddings.Basic.horizontal / 2)
                        .padding(
                            vertical = Paddings.Basic.vertical,
                            horizontal = Paddings.Basic.horizontal / 2
                        ),
                    text = stringResource(id = R.string.edit_selector_content_show_condition_item_add),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
                )
            }

            HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 0.3.dp, color = onSurfaceSub)

            for(i in 0..2){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(85.dp)
                        .padding(horizontal = 8.dp)
                        .borderLine(
                            density = LocalDensity.current,
                            color = MaterialTheme.colorScheme.outline,
                            bottom = 0.3.dp
                        )
                        .padding(horizontal = 10.dp)
                        .padding(start = 15.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .fillMaxWidth(0.75f)
                    ) {

                        FadeInOutSkeleton(
                            modifier = Modifier.height(13.dp).width(70.dp)
                        )

                        Spacer(modifier = Modifier.height(7.dp))

                        FadeInOutSkeleton(
                            modifier = Modifier.height(18.dp).width(260.dp)
                        )
                    }

                    Box(
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ){
                        FadeInOutSkeleton(
                            modifier = Modifier.height(25.dp).width(35.dp),
                            shape = MaterialTheme.shapes.large
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .height(80.dp)
                .background(MaterialTheme.colorScheme.surface)
                .borderLine(
                    density = LocalDensity.current,
                    color = MaterialTheme.colorScheme.outline,
                    top = 1.dp
                )
        ) {

            Box(
                modifier = Modifier
                    .padding(vertical = 11.dp, horizontal = 14.dp)
                    .fillMaxSize()
                    .clip(
                        shape = MaterialTheme.shapes.small
                    )
                    .background(color = MaterialTheme.colorScheme.background)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(id = R.string.edit_selector_content_button_page_preview),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}