package com.fancymansion.data.datasource.appStorage.book.model

import com.fancymansion.core.common.const.ACTION_ID_NOT_ASSIGNED
import com.fancymansion.core.common.const.ConditionType
import com.fancymansion.core.common.const.LogicalOp
import com.fancymansion.core.common.const.PageType
import com.fancymansion.core.common.const.RelationOp
import com.fancymansion.domain.model.book.ActionIdModel
import com.fancymansion.domain.model.book.ConditionModel
import com.fancymansion.domain.model.book.LogicModel
import com.fancymansion.domain.model.book.PageLogicModel
import com.fancymansion.domain.model.book.RouteModel
import com.fancymansion.domain.model.book.SelectorModel

data class LogicData(val id: Long, val logics: List<PageLogicData> = listOf())

data class PageLogicData(
    val pageId: Long,
    val type: PageType = PageType.NORMAL,
    val title: String,
    val selectors: List<SelectorData> = listOf()
)

data class SelectorData(
    val pageId: Long,
    val selectorId: Long,
    val text: String,
    val showConditions: List<ConditionData> = listOf(),
    val routes: List<RouteData> = listOf()
)

data class RouteData(
    val pageId: Long,
    val selectorId: Long,
    val routeId: Long,
    val routeTargetPageId: Long,
    val routeConditions: List<ConditionData> = listOf()
)

data class ConditionData(
    val pageId: Long,
    val selectorId: Long,
    val routeId: Long,
    val conditionId: Long,
    val type: ConditionType,
    val selfActionId: ActionIdData = ActionIdData(),
    val targetActionId: ActionIdData = ActionIdData(),
    val count: Int = 0,
    val relationOp: RelationOp = RelationOp.EQUAL,
    val logicalOp: LogicalOp = LogicalOp.AND
)

data class ActionIdData(
    val pageId: Long = ACTION_ID_NOT_ASSIGNED,
    val selectorId: Long = ACTION_ID_NOT_ASSIGNED,
    val routeId: Long = ACTION_ID_NOT_ASSIGNED
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
    pageId = pageId,
    type = type,
    title = title,
    selectors = selectors.map { it.asModel() }
)

fun PageLogicModel.asData() = PageLogicData(
    pageId = pageId,
    type = type,
    title = title,
    selectors = selectors.map { it.asData() }
)

fun SelectorData.asModel() = SelectorModel(
    pageId = pageId,
    selectorId = selectorId,
    text = text,
    showConditions = showConditions.map { it.asModel() },
    routes = routes.map { it.asModel() }
)

fun SelectorModel.asData() = SelectorData(
    pageId = pageId,
    selectorId = selectorId,
    text = text,
    showConditions = showConditions.map { it.asData() },
    routes = routes.map { it.asData() }
)

fun RouteData.asModel() = RouteModel(
    pageId = pageId,
    selectorId = selectorId,
    routeId = routeId,
    routeTargetPageId = routeTargetPageId,
    routeConditions = routeConditions.map { it.asModel() }
)

fun RouteModel.asData() = RouteData(
    pageId = pageId,
    selectorId = selectorId,
    routeId = routeId,
    routeTargetPageId = routeTargetPageId,
    routeConditions = routeConditions.map { it.asData() }
)

fun ConditionData.asModel() = ConditionModel(
    pageId = pageId,
    selectorId = selectorId,
    routeId = routeId,
    conditionId = conditionId,
    type = type,
    selfActionId = selfActionId.asModel(),
    targetActionId = targetActionId.asModel(),
    count = count,
    relationOp = relationOp,
    logicalOp = logicalOp
)

fun ConditionModel.asData() = ConditionData(
    pageId = pageId,
    selectorId = selectorId,
    routeId = routeId,
    conditionId = conditionId,
    type = type,
    selfActionId = selfActionId.asStorageData(),
    targetActionId = targetActionId.asStorageData(),
    count = count,
    relationOp = relationOp,
    logicalOp = logicalOp
)

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