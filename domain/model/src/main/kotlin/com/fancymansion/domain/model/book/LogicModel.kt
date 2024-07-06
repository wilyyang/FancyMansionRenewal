package com.fancymansion.domain.model.book

import com.fancymansion.core.common.const.COMPARE_ACTION_ID_NOT_ASSIGNED
import com.fancymansion.core.common.const.ConditionType
import com.fancymansion.core.common.const.LogicalOp
import com.fancymansion.core.common.const.PageType
import com.fancymansion.core.common.const.RelationOp

data class LogicModel(val id: Long, val logics: List<PageLogicModel> = listOf())

data class PageLogicModel(
    val id: Long,
    val type: PageType = PageType.NORMAL,
    val title: String,
    val selectors: List<SelectorModel> = listOf()
)

data class SelectorModel(
    val id: Long,
    val text: String,
    val showConditions: List<ConditionModel> = listOf(),
    val routes: List<RouteModel> = listOf()
)

data class RouteModel(
    val id: Long,
    val routePageId: Long,
    val routeConditions: List<ConditionModel> = listOf()
)

data class ConditionModel(
    val id: Long,
    val type: ConditionType,
    val selfActionId: Long = COMPARE_ACTION_ID_NOT_ASSIGNED,
    val targetActionId: Long = COMPARE_ACTION_ID_NOT_ASSIGNED,
    val count: Int = 0,
    val relationOp: RelationOp = RelationOp.EQUAL,
    val logicalOp: LogicalOp = LogicalOp.AND
)