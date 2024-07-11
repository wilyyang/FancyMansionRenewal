package com.fancymansion.domain.model.book

import com.fancymansion.core.common.const.ACTION_ID_NOT_ASSIGNED
import com.fancymansion.core.common.const.ConditionType
import com.fancymansion.core.common.const.LogicalOp
import com.fancymansion.core.common.const.PageType
import com.fancymansion.core.common.const.RelationOp

data class LogicModel(val id: Long, val logics: List<PageLogicModel> = listOf())

data class PageLogicModel(
    val pageId: Long,
    val type: PageType = PageType.NORMAL,
    val title: String,
    val selectors: List<SelectorModel> = listOf()
)

data class SelectorModel(
    val pageId: Long,
    val selectorId: Long,
    val text: String,
    val showConditions: List<ConditionModel> = listOf(),
    val routes: List<RouteModel> = listOf()
)

data class RouteModel(
    val pageId: Long,
    val selectorId: Long,
    val routeId: Long,
    val routeTargetPageId: Long,
    val routeConditions: List<ConditionModel> = listOf()
)

data class ConditionModel(
    val pageId: Long,
    val selectorId: Long,
    val routeId: Long = ACTION_ID_NOT_ASSIGNED,
    val conditionId: Long,
    val type: ConditionType,
    val selfActionId: ActionIdModel = ActionIdModel(),
    val targetActionId: ActionIdModel = ActionIdModel(),
    val count: Int = 0,
    val relationOp: RelationOp = RelationOp.EQUAL,
    val logicalOp: LogicalOp = LogicalOp.AND
)

data class ActionIdModel(
    val pageId: Long = ACTION_ID_NOT_ASSIGNED,
    val selectorId: Long = ACTION_ID_NOT_ASSIGNED,
    val routeId: Long = ACTION_ID_NOT_ASSIGNED
)