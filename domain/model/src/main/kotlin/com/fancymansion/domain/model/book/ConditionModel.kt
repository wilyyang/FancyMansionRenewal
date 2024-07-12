package com.fancymansion.domain.model.book

import com.fancymansion.core.common.const.LogicalOp
import com.fancymansion.core.common.const.RelationOp

sealed class ConditionModel(
    open val conditionId: Long,
    open val conditionRule : ConditionRuleModel
) {
    data class ShowSelectorConditionModel(
        override val conditionId: Long,
        override val conditionRule : ConditionRuleModel,
        val pageId: Long,
        val selectorId: Long,
    ) : ConditionModel(conditionId, conditionRule)

    data class RouteConditionModel(
        override val conditionId: Long,
        override val conditionRule : ConditionRuleModel,
        val pageId: Long,
        val selectorId: Long,
        val routeId: Long
    ) : ConditionModel(conditionId, conditionRule)
}

sealed class ConditionRuleModel(
    open val selfActionId: ActionIdModel,
    open val relationOp: RelationOp = RelationOp.EQUAL,
    open val logicalOp: LogicalOp = LogicalOp.AND
) {
    data class CountConditionRuleModel(
        override val selfActionId: ActionIdModel,
        override val relationOp: RelationOp = RelationOp.EQUAL,
        override val logicalOp: LogicalOp = LogicalOp.AND,
        val count: Int
    ) : ConditionRuleModel(selfActionId, relationOp, logicalOp)

    data class TargetConditionRuleModel(
        override val selfActionId: ActionIdModel,
        override val relationOp: RelationOp = RelationOp.EQUAL,
        override val logicalOp: LogicalOp = LogicalOp.AND,
        val targetActionId: ActionIdModel
    ) : ConditionRuleModel(selfActionId, relationOp, logicalOp)
}