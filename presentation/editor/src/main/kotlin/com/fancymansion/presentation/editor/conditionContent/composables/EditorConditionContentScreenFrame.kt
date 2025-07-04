package com.fancymansion.presentation.editor.conditionContent.composables

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.fancymansion.core.common.const.ACTION_ID_NOT_ASSIGNED
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.core.presentation.base.SIDE_EFFECTS_KEY
import com.fancymansion.core.presentation.base.window.TypePane
import com.fancymansion.core.presentation.compose.frame.BaseScreen
import com.fancymansion.core.presentation.compose.frame.FancyMansionTopBar
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.presentation.editor.R
import com.fancymansion.presentation.editor.common.ITEM_ID_NOT_ASSIGNED
import com.fancymansion.presentation.editor.common.composables.BottomSelectListDialog
import com.fancymansion.presentation.editor.conditionContent.ConditionRuleWrapper
import com.fancymansion.presentation.editor.conditionContent.EditorConditionContentContract
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

sealed class DialogActionType {
    object Self : DialogActionType()
    object Target : DialogActionType()
}

sealed class DialogItemType {
    object Page : DialogItemType()
    object Selector : DialogItemType()
}

@Composable
fun EditorConditionContentScreenFrame(
    uiState: EditorConditionContentContract.State,
    loadState: LoadState,
    effectFlow: SharedFlow<EditorConditionContentContract.Effect>?,
    onCommonEventSent: (event: CommonEvent) -> Unit,
    onEventSent: (event: EditorConditionContentContract.Event) -> Unit,
    onNavigationRequested: (EditorConditionContentContract.Effect.Navigation) -> Unit
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
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            if(effect is EditorConditionContentContract.Effect.Navigation){
                onNavigationRequested(effect)
            }
        }?.collect()
    }

    var dialogActionType by remember { mutableStateOf<DialogActionType?>(null) }
    var dialogItemType by remember { mutableStateOf<DialogItemType?>(null) }

    BaseScreen(
        loadState = loadState,
        description = EditorConditionContentContract.NAME,
        statusBarColor = MaterialTheme.colorScheme.surface,
        typePane = TypePane.MOBILE,
        initContent = {
            EditorConditionContentSkeletonScreen()
        },
        topBar = {
            FancyMansionTopBar(
                typePane = TypePane.MOBILE,
                topBarColor = MaterialTheme.colorScheme.surface,
                idLeftIcon = com.fancymansion.core.presentation.R.drawable.ic_back,
                onClickLeftIcon = {
                    onCommonEventSent(CommonEvent.CloseEvent)
                },
                title = stringResource(id = uiState.barTitleResId),
                subTitle = uiState.barSubTitle,
                sideRightText = if(uiState.isInitSuccess) stringResource(id = R.string.topbar_editor_side_save) else null,
                onClickRightIcon = {
                    onEventSent(EditorConditionContentContract.Event.ConditionSaveToFile)
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
            dialogActionType?.let { actionType ->
                dialogItemType?.let { itemType ->
                    when(actionType){
                        is DialogActionType.Self -> uiState.conditionRule.selfActionId
                        is DialogActionType.Target -> {
                            (uiState.conditionRule as? ConditionRuleWrapper.TargetConditionRuleWrapper)?.targetActionId
                        }
                    }?.let { actionId ->
                        BottomSelectListDialog(
                            title = stringResource(
                                id = when (itemType) {
                                    is DialogItemType.Page -> R.string.edit_condition_content_label_page_list
                                    is DialogItemType.Selector -> R.string.edit_condition_content_label_selector_list
                                }
                            ),
                            selectedId = when (itemType) {
                                is DialogItemType.Page -> actionId.pageId.takeIf { it != ACTION_ID_NOT_ASSIGNED } ?: ITEM_ID_NOT_ASSIGNED
                                is DialogItemType.Selector -> actionId.selectorId.takeIf { it != ACTION_ID_NOT_ASSIGNED } ?: ITEM_ID_NOT_ASSIGNED
                            },
                            checkIconTint = when (itemType) {
                                is DialogItemType.Page -> MaterialTheme.colorScheme.primary
                                is DialogItemType.Selector -> MaterialTheme.colorScheme.secondary
                            },
                            itemList = when (itemType) {
                                is DialogItemType.Page -> uiState.pageItemList
                                is DialogItemType.Selector -> uiState.selectorItemMap[actionId.pageId]?:emptyList()
                            },
                            onSelectItem = {
                                val event = when (actionType) {
                                    is DialogActionType.Self -> when (itemType) {
                                        is DialogItemType.Page -> EditorConditionContentContract.Event.SelectSelfPage(it)
                                        is DialogItemType.Selector -> EditorConditionContentContract.Event.SelectSelfSelector(it)
                                    }

                                    is DialogActionType.Target -> when (itemType) {
                                        is DialogItemType.Page -> EditorConditionContentContract.Event.SelectTargetPage(it)
                                        is DialogItemType.Selector -> EditorConditionContentContract.Event.SelectTargetSelector(it)
                                    }
                                }
                                onEventSent(event)

                                coroutineScope.launch {
                                    bottomDrawerState.close()
                                }
                            }
                        )
                    }
                }
            }
        }
    ) {
        EditorConditionContentScreenContent(
            modifier = Modifier.fillMaxSize(),
            uiState = uiState,
            onEventSent = onEventSent,
            onCommonEventSent = onCommonEventSent,
            onOpenSelfActionPageList = {
                coroutineScope.launch {
                    dialogActionType = DialogActionType.Self
                    dialogItemType = DialogItemType.Page
                    bottomDrawerState.open()
                }
            },
            onOpenSelfActionSelectorList = {
                coroutineScope.launch {
                    dialogActionType = DialogActionType.Self
                    dialogItemType = DialogItemType.Selector
                    bottomDrawerState.open()
                }
            },
            onOpenTargetActionPageList = {
                coroutineScope.launch {
                    dialogActionType = DialogActionType.Target
                    dialogItemType = DialogItemType.Page
                    bottomDrawerState.open()
                }
            },
            onOpenTargetActionSelectorList = {
                coroutineScope.launch {
                    dialogActionType = DialogActionType.Target
                    dialogItemType = DialogItemType.Selector
                    bottomDrawerState.open()
                }
            }
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
fun EditorConditionContentSkeletonScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface),
    ) {
        FancyMansionTopBar(
            typePane = TypePane.MOBILE,
            topBarColor = MaterialTheme.colorScheme.surface,
            title = stringResource(id = R.string.topbar_editor_title_condition_content),
            subTitle = stringResource(id = R.string.topbar_editor_sub_title),
            shadowElevation = 1.dp
        )
    }
}