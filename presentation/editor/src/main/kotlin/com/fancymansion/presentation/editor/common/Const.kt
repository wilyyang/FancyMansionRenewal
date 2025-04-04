package com.fancymansion.presentation.editor.common

import androidx.compose.ui.unit.dp
import com.fancymansion.core.common.const.LogicalOp
import com.fancymansion.core.common.const.RelationOp
import com.fancymansion.core.common.const.SELECTOR_ID_NOT_ASSIGNED
import com.fancymansion.domain.model.book.ConditionModel
import com.fancymansion.domain.model.book.ConditionRuleModel
import com.fancymansion.domain.model.book.LogicModel
import com.fancymansion.presentation.editor.common.ActionInfo.CountInfo
import com.fancymansion.presentation.editor.common.ActionInfo.PageInfo
import com.fancymansion.presentation.editor.common.ActionInfo.SelectorInfo

val itemMarginHeight = 15.dp

data class ConditionState(var editIndex : Int, val condition : ConditionWrapper)

enum class ConditionGroup{
    ShowSelectorCondition,
    RouteCondition
}

sealed class ActionInfo{
    data class CountInfo(val count: Int): ActionInfo()
    data class PageInfo(val pageTitle: String): ActionInfo()
    data class SelectorInfo(val pageTitle: String, val selectorText: String): ActionInfo()
}

data class ConditionWrapper(
    val conditionId: Long,
    val conditionGroup : ConditionGroup,
    val selfActionInfo : ActionInfo,
    val targetActionInfo : ActionInfo,
    val logicalOp: LogicalOp,
    val relationOp: RelationOp
)

fun ConditionModel.toWrapper(logic: LogicModel): ConditionWrapper {
    val selfInfo = logic.findActionInfo(
        conditionRule.selfActionId.pageId,
        conditionRule.selfActionId.selectorId
    )

    val targetInfo = when (val rule = conditionRule) {
        is ConditionRuleModel.CountConditionRuleModel -> CountInfo(rule.count)
        is ConditionRuleModel.TargetConditionRuleModel ->
            logic.findActionInfo(rule.targetActionId.pageId, rule.targetActionId.selectorId)
    }

    return ConditionWrapper(
        conditionId = conditionId,
        conditionGroup = when (this) {
            is ConditionModel.ShowSelectorConditionModel -> ConditionGroup.ShowSelectorCondition
            is ConditionModel.RouteConditionModel -> ConditionGroup.RouteCondition
        },
        selfActionInfo = selfInfo,
        targetActionInfo = targetInfo,
        logicalOp = conditionRule.logicalOp,
        relationOp = conditionRule.relationOp
    )
}

private fun LogicModel.findActionInfo(pageId: Long, selectorId: Long): ActionInfo {
    val page = logics.firstOrNull { it.pageId == pageId } ?: return PageInfo("")
    val pageTitle = page.title

    return if (selectorId != SELECTOR_ID_NOT_ASSIGNED) {
        page.selectors.firstOrNull { it.selectorId == selectorId }?.let {
            SelectorInfo(pageTitle, it.text)
        } ?: PageInfo(pageTitle)
    } else {
        PageInfo(pageTitle)
    }
}