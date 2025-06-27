package com.fancymansion.presentation.editor.conditionContent.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fancymansion.core.common.resource.StringValue
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.compose.screen.NoDataScreen
import com.fancymansion.core.presentation.compose.theme.Paddings
import com.fancymansion.core.presentation.compose.theme.onSurfaceSub
import com.fancymansion.presentation.editor.R
import com.fancymansion.presentation.editor.conditionContent.CompareType
import com.fancymansion.presentation.editor.conditionContent.ConditionRuleWrapper.CountConditionRuleWrapper
import com.fancymansion.presentation.editor.conditionContent.ConditionRuleWrapper.TargetConditionRuleWrapper
import com.fancymansion.presentation.editor.conditionContent.EditorConditionContentContract
import com.fancymansion.presentation.editor.conditionContent.composables.part.SelectActionTarget
import com.fancymansion.presentation.editor.conditionContent.composables.part.SelectCompareType

@Composable
fun EditorConditionContentScreenContent(
    modifier: Modifier = Modifier,
    uiState: EditorConditionContentContract.State,
    onEventSent: (event: EditorConditionContentContract.Event) -> Unit,
    onCommonEventSent: (event: CommonEvent) -> Unit,
    onOpenSelfActionPageList: () -> Unit,
    onOpenSelfActionSelectorList: () -> Unit,
    onOpenTargetActionPageList: () -> Unit,
    onOpenTargetActionSelectorList: () -> Unit
) {
    if (!uiState.isInitSuccess) {
        Box(
            modifier = modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .fillMaxSize(), contentAlignment = Alignment.Center
        ) {

            NoDataScreen(
                modifier = Modifier.padding(horizontal = 20.dp),
                option1Title = StringValue.StringResource(resId = com.fancymansion.core.presentation.R.string.button_back),
                onClickOption1 = {
                    onCommonEventSent(CommonEvent.CloseEvent)
                }
            )
        }
    } else {
        val listState = rememberLazyListState()

        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)) {

            LazyColumn(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.surface),
                state = listState
            ) {

                item {
                    SelectActionTarget(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Paddings.Basic.horizontal)
                            .padding(top = Paddings.Basic.vertical),
                        title = stringResource(R.string.edit_condition_content_select_action_self_title),
                        mainItemText = uiState.conditionRule.selfActionId.pageTitle,
                        subItemText = uiState.conditionRule.selfActionId.selectorText?.let { selector ->
                            stringResource(
                                R.string.edit_condition_content_select_action_selector_prefix,
                                selector
                            )
                        },
                        onClickMainItemText = {
                            onOpenSelfActionPageList()
                        },
                        onClickSubItemText = {
                            onOpenSelfActionSelectorList()
                        }
                    )
                }

                item {
                    HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 0.3.dp, color = onSurfaceSub)

                    SelectCompareType(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Paddings.Basic.horizontal)
                            .padding(top = Paddings.Basic.vertical),
                        selectedType = when (uiState.conditionRule) {
                            is CountConditionRuleWrapper -> CompareType.Count
                            is TargetConditionRuleWrapper -> CompareType.TargetActionId
                        },
                        onClickDropdownTitle = {},
                        onItemSelected = {
                            onEventSent(EditorConditionContentContract.Event.SelectCompareType(it))
                        }
                    )
                }

                if(uiState.conditionRule is TargetConditionRuleWrapper){
                    item {
                        SelectActionTarget(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = Paddings.Basic.horizontal)
                                .padding(top = Paddings.Basic.vertical),
                            title = stringResource(R.string.edit_condition_content_select_action_target_title),
                            mainItemText = uiState.conditionRule.targetActionId.pageTitle,
                            subItemText = uiState.conditionRule.targetActionId.selectorText?.let { selector ->
                                stringResource(
                                    R.string.edit_condition_content_select_action_selector_prefix,
                                    selector
                                )
                            },
                            onClickMainItemText = {
                                onOpenTargetActionPageList()
                            },
                            onClickSubItemText = {
                                onOpenTargetActionSelectorList()
                            }
                        )
                    }
                }
            }
        }
    }
}