package com.fancymansion.domain.model.book

import com.fancymansion.core.common.const.COMPARE_ID_NOT_ASSIGNED
import com.fancymansion.core.common.const.ConditionType
import com.fancymansion.core.common.const.LogicalOp
import com.fancymansion.core.common.const.PageType
import com.fancymansion.core.common.const.RelationOp

data class Logic(val id: Long, val logics: List<PageLogic> = listOf())

data class PageLogic(
    val id: Long,
    val type: PageType = PageType.NORMAL,
    val title: String,
    val selectors: List<Selector> = listOf()
)

data class Selector(
    val id: Long,
    val text: String,
    val showConditions: List<Condition> = listOf(),
    val routes: List<Route> = listOf()
)

data class Route(
    val id: Long,
    val routePageId: Long,
    val routeConditions: List<Condition> = listOf()
)

data class Condition(
    val id: Long,
    val type: ConditionType,
    val selfViewsId: Long = COMPARE_ID_NOT_ASSIGNED,
    val targetViewsId: Long = COMPARE_ID_NOT_ASSIGNED,
    val count: Int = 0,
    val relationOp: RelationOp = RelationOp.EQUAL,
    val logicalOp: LogicalOp = LogicalOp.AND
)