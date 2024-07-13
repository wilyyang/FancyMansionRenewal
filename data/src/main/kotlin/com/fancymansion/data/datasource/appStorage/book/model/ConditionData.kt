package com.fancymansion.data.datasource.appStorage.book.model

import com.fancymansion.core.common.const.ACTION_ID_NOT_ASSIGNED
import com.fancymansion.core.common.const.LogicalOp
import com.fancymansion.core.common.const.RelationOp
import com.fancymansion.domain.model.book.ActionIdModel
import com.fancymansion.domain.model.book.ConditionModel
import com.fancymansion.domain.model.book.ConditionRuleModel

enum class ConditionType {
    SHOW_SELECTOR,
    ROUTE
}

enum class ConditionRuleType {
    COUNT,
    TARGET
}

data class ConditionData(
    val type : ConditionType,
    val conditionId: Long,
    val conditionRule : ConditionRuleData,
    val pageId: Long,
    val selectorId: Long,
    val routeId: Long = 0L
)
data class ConditionRuleData(
    val type : ConditionRuleType,
    val selfActionId: ActionIdData,
    val relationOp: RelationOp = RelationOp.EQUAL,
    val logicalOp: LogicalOp = LogicalOp.AND,
    val count: Int = 0,
    val targetActionId: ActionIdData = ActionIdData()
)
data class ActionIdData(
    val pageId: Long = ACTION_ID_NOT_ASSIGNED,
    val selectorId: Long = ACTION_ID_NOT_ASSIGNED,
    val routeId: Long = ACTION_ID_NOT_ASSIGNED
)

fun ConditionData.asModel() : ConditionModel {
    return when(type){
        ConditionType.SHOW_SELECTOR -> ConditionModel.ShowSelectorConditionModel(
            conditionId = conditionId,
            conditionRule = conditionRule.asModel(),
            pageId = pageId,
            selectorId = selectorId
        )
        ConditionType.ROUTE -> ConditionModel.RouteConditionModel(
            conditionId = conditionId,
            conditionRule = conditionRule.asModel(),
            pageId = pageId,
            selectorId = selectorId,
            routeId = routeId
        )
    }
}

fun ConditionModel.asData(): ConditionData {
    return when (this) {
        is ConditionModel.ShowSelectorConditionModel -> ConditionData(
            type = ConditionType.SHOW_SELECTOR,
            conditionId = conditionId,
            conditionRule = conditionRule.asData(),
            pageId = pageId,
            selectorId = selectorId
        )

        is ConditionModel.RouteConditionModel -> ConditionData(
            type = ConditionType.ROUTE,
            conditionId = conditionId,
            conditionRule = conditionRule.asData(),
            pageId = pageId,
            selectorId = selectorId,
            routeId = routeId
        )
    }
}

fun ConditionRuleData.asModel() : ConditionRuleModel {
    return when(type){
        ConditionRuleType.COUNT -> ConditionRuleModel.CountConditionRuleModel(
            selfActionId = selfActionId.asModel(),
            relationOp = relationOp,
            logicalOp = logicalOp,
            count = count
        )
        ConditionRuleType.TARGET -> ConditionRuleModel.TargetConditionRuleModel(
            selfActionId = selfActionId.asModel(),
            relationOp = relationOp,
            logicalOp = logicalOp,
            targetActionId = targetActionId.asModel()
        )
    }
}

fun ConditionRuleModel.asData(): ConditionRuleData {
    return when (this) {
        is ConditionRuleModel.CountConditionRuleModel -> ConditionRuleData(
            type = ConditionRuleType.COUNT,
            selfActionId = selfActionId.asStorageData(),
            relationOp = relationOp,
            logicalOp = logicalOp,
            count = count
        )

        is ConditionRuleModel.TargetConditionRuleModel -> ConditionRuleData(
            type = ConditionRuleType.TARGET,
            selfActionId = selfActionId.asStorageData(),
            relationOp = relationOp,
            logicalOp = logicalOp,
            targetActionId = targetActionId.asStorageData()
        )
    }
}

fun ActionIdData.asModel() = ActionIdModel(
    pageId = pageId,
    selectorId = selectorId,
    routeId = routeId
)

fun ActionIdModel.asStorageData() = ActionIdData(
    pageId = pageId,
    selectorId = selectorId,
    routeId = routeId
)