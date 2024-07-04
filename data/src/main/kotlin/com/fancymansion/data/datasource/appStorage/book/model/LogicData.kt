package com.fancymansion.data.datasource.appStorage.book.model

import com.fancymansion.core.common.const.COMPARE_ID_NOT_ASSIGNED
import com.fancymansion.core.common.const.ConditionType
import com.fancymansion.core.common.const.LogicalOp
import com.fancymansion.core.common.const.PageType
import com.fancymansion.core.common.const.RelationOp
import com.fancymansion.domain.model.book.ConditionModel
import com.fancymansion.domain.model.book.LogicModel
import com.fancymansion.domain.model.book.PageLogicModel
import com.fancymansion.domain.model.book.RouteModel
import com.fancymansion.domain.model.book.SelectorModel

data class LogicData(val id: Long, val logics: List<PageLogicData> = listOf())

data class PageLogicData(
    val id: Long,
    val type: PageType = PageType.NORMAL,
    val title: String,
    val selectors: List<SelectorData> = listOf()
)

data class SelectorData(
    val id: Long,
    val text: String,
    val showConditions: List<ConditionData> = listOf(),
    val routes: List<RouteData> = listOf()
)

data class RouteData(
    val id: Long,
    val routePageId: Long,
    val routeConditions: List<ConditionData> = listOf()
)

data class ConditionData(
    val id: Long,
    val type: ConditionType,
    val selfViewsId: Long = COMPARE_ID_NOT_ASSIGNED,
    val targetViewsId: Long = COMPARE_ID_NOT_ASSIGNED,
    val count: Int = 0,
    val relationOp: RelationOp = RelationOp.EQUAL,
    val logicalOp: LogicalOp = LogicalOp.AND
)

fun LogicData.asModel() = LogicModel(
    id = id,
    logics = logics.map { it.asModel() }
)

fun LogicModel.asData() = LogicData(
    id = id,
    logics = logics.map { it.asData() }
)

fun PageLogicData.asModel() = PageLogicModel(
    id = id,
    type = type,
    title = title,
    selectors = selectors.map { it.asModel() }
)

fun PageLogicModel.asData() = PageLogicData(
    id = id,
    type = type,
    title = title,
    selectors = selectors.map { it.asData() }
)

fun SelectorData.asModel() = SelectorModel(
    id = id,
    text = text,
    showConditions = showConditions.map { it.asModel() },
    routes = routes.map { it.asModel() }
)

fun SelectorModel.asData() = SelectorData(
    id = id,
    text = text,
    showConditions = showConditions.map { it.asData() },
    routes = routes.map { it.asData() }
)

fun RouteData.asModel() = RouteModel(
    id = id,
    routePageId = routePageId,
    routeConditions = routeConditions.map { it.asModel() }
)

fun RouteModel.asData() = RouteData(
    id = id,
    routePageId = routePageId,
    routeConditions = routeConditions.map { it.asData() }
)

fun ConditionData.asModel() = ConditionModel(
    id = id,
    type = type,
    selfViewsId = selfViewsId,
    targetViewsId = targetViewsId,
    count = count,
    relationOp = relationOp,
    logicalOp = logicalOp
)

fun ConditionModel.asData() = ConditionData(
    id = id,
    type = type,
    selfViewsId = selfViewsId,
    targetViewsId = targetViewsId,
    count = count,
    relationOp = relationOp,
    logicalOp = logicalOp
)