package com.fancymansion.domain.model.book

import com.fancymansion.core.common.const.BookId
import com.fancymansion.core.common.const.COMPARE_ID_NOT_ASSIGNED
import com.fancymansion.core.common.const.ConditionId
import com.fancymansion.core.common.const.LogicalOp
import com.fancymansion.core.common.const.PageId
import com.fancymansion.core.common.const.PageType
import com.fancymansion.core.common.const.ROUTE_PAGE_ID_NOT_ASSIGNED
import com.fancymansion.core.common.const.RelationOp
import com.fancymansion.core.common.const.RouteId
import com.fancymansion.core.common.const.SelectorId

data class Logic(val id: BookId, val logics: List<PageLogic> = listOf())

data class PageLogic(
    val id: PageId,
    val type: PageType = PageType.NORMAL,
    val title: String,
    val selectors: List<Selector> = listOf()
)

data class Selector(
    val id: SelectorId,
    val text: String,
    val showConditions: List<Condition> = listOf(),
    val routes: List<Route> = listOf()
)

data class Route(
    val id: RouteId,
    val routePageId: PageId,
    val routeConditions: List<Condition> = listOf()
)

data class Condition(
    val id: ConditionId,
    val targetId: Long = COMPARE_ID_NOT_ASSIGNED,
    val compareId: Long = COMPARE_ID_NOT_ASSIGNED,
    val count: Int = 0,
    val relationOp: RelationOp = RelationOp.EQUAL,
    val logicalOp: LogicalOp = LogicalOp.AND
)