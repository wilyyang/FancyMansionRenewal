package com.fancymansion.data.datasource.appStorage.book.model

import com.fancymansion.core.common.const.ACTION_ID_NOT_ASSIGNED
import com.fancymansion.core.common.const.LogicalOp
import com.fancymansion.core.common.const.RelationOp
import com.fancymansion.domain.model.book.ActionIdModel
import com.fancymansion.domain.model.book.ConditionModel
import com.fancymansion.domain.model.book.ConditionRuleModel
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext

sealed class ConditionData(
    val type : String,
    open val conditionId: Long,
    open val conditionRule : ConditionRuleData
) {
    data class ShowSelectorConditionData(
        override val conditionId: Long,
        override val conditionRule : ConditionRuleData,
        val pageId: Long,
        val selectorId: Long,
    ) : ConditionData(TYPE_CONDITION_SHOW_SELECTOR, conditionId, conditionRule)

    data class RouteConditionData(
        override val conditionId: Long,
        override val conditionRule : ConditionRuleData,
        val pageId: Long,
        val selectorId: Long,
        val routeId: Long
    ) : ConditionData(TYPE_CONDITION_ROUTE, conditionId, conditionRule)

    fun toJson(context: JsonSerializationContext): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.addProperty("type", type)
        jsonObject.addProperty("conditionId", conditionId)
        jsonObject.add("conditionRule", conditionRule.toJson(context))
        when (this) {
            is ShowSelectorConditionData -> {
                jsonObject.addProperty("pageId", pageId)
                jsonObject.addProperty("selectorId", selectorId)
            }
            is RouteConditionData -> {
                jsonObject.addProperty("pageId", pageId)
                jsonObject.addProperty("selectorId", selectorId)
                jsonObject.addProperty("routeId", routeId)
            }
        }
        return jsonObject
    }

    companion object {
        const val TYPE_CONDITION_SHOW_SELECTOR = "show_selector"
        const val TYPE_CONDITION_ROUTE = "route"

        fun fromJson(json: JsonElement, context: JsonDeserializationContext): ConditionData {
            if (!json.isJsonObject) {
                throw IllegalArgumentException("Invalid JSON format for ConditionData")
            }
            val jsonObject = json.asJsonObject
            val type = jsonObject.get("type").asString
            val conditionId = jsonObject.get("conditionId").asLong
            val conditionRule = ConditionRuleData.fromJson(jsonObject.get("conditionRule"), context)
            return when (type) {
                TYPE_CONDITION_SHOW_SELECTOR -> {
                    val pageId = jsonObject.get("pageId").asLong
                    val selectorId = jsonObject.get("selectorId").asLong
                    ShowSelectorConditionData(conditionId, conditionRule, pageId, selectorId)
                }
                TYPE_CONDITION_ROUTE -> {
                    val pageId = jsonObject.get("pageId").asLong
                    val selectorId = jsonObject.get("selectorId").asLong
                    val routeId = jsonObject.get("routeId").asLong
                    RouteConditionData(conditionId, conditionRule, pageId, selectorId, routeId)
                }
                else -> throw IllegalArgumentException("Unknown ConditionData type: $type")
            }
        }
    }
}

sealed class ConditionRuleData(
    val type : String,
    open val selfActionId: ActionIdData,
    open val relationOp: RelationOp = RelationOp.EQUAL,
    open val logicalOp: LogicalOp = LogicalOp.AND
) {
    data class CountConditionRuleData(
        override val selfActionId: ActionIdData,
        override val relationOp: RelationOp = RelationOp.EQUAL,
        override val logicalOp: LogicalOp = LogicalOp.AND,
        val count: Int
    ) : ConditionRuleData(TYPE_CONDITION_RULE_COUNT, selfActionId, relationOp, logicalOp)

    data class TargetConditionRuleData(
        override val selfActionId: ActionIdData,
        override val relationOp: RelationOp = RelationOp.EQUAL,
        override val logicalOp: LogicalOp = LogicalOp.AND,
        val targetActionId: ActionIdData
    ) : ConditionRuleData(TYPE_CONDITION_RULE_TARGET, selfActionId, relationOp, logicalOp)

    fun toJson(context: JsonSerializationContext): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.addProperty("type", type)
        jsonObject.add("selfActionId", selfActionId.toJson(context))
        jsonObject.addProperty("relationOp", relationOp.name)
        jsonObject.addProperty("logicalOp", logicalOp.name)
        when (this) {
            is CountConditionRuleData -> jsonObject.addProperty("count", count)
            is TargetConditionRuleData -> jsonObject.add("targetActionId", targetActionId.toJson(context))
        }
        return jsonObject
    }

    companion object {
        const val TYPE_CONDITION_RULE_COUNT = "count"
        const val TYPE_CONDITION_RULE_TARGET = "target"

        fun fromJson(json: JsonElement, context: JsonDeserializationContext): ConditionRuleData {
            if (!json.isJsonObject) {
                throw IllegalArgumentException("Invalid JSON format for ConditionRuleData")
            }
            val jsonObject = json.asJsonObject
            val type = jsonObject.get("type").asString
            val selfActionId = ActionIdData.fromJson(jsonObject.get("selfActionId"), context)
            val relationOp = RelationOp.valueOf(jsonObject.get("relationOp").asString)
            val logicalOp = LogicalOp.valueOf(jsonObject.get("logicalOp").asString)
            return when (type) {
                TYPE_CONDITION_RULE_COUNT -> {
                    val count = jsonObject.get("count").asInt
                    CountConditionRuleData(selfActionId, relationOp, logicalOp, count)
                }
                TYPE_CONDITION_RULE_TARGET -> {
                    val targetActionId = ActionIdData.fromJson(jsonObject.get("targetActionId"), context)
                    TargetConditionRuleData(selfActionId, relationOp, logicalOp, targetActionId)
                }
                else -> throw IllegalArgumentException("Unknown ConditionRuleData type: $type")
            }
        }
    }
}

data class ActionIdData(
    val pageId: Long = ACTION_ID_NOT_ASSIGNED,
    val selectorId: Long = ACTION_ID_NOT_ASSIGNED,
    val routeId: Long = ACTION_ID_NOT_ASSIGNED
)

fun ConditionData.ShowSelectorConditionData.asModel() =
    ConditionModel.ShowSelectorConditionModel(
        conditionId = conditionId,
        conditionRule = conditionRule.asModel(),
        pageId = pageId,
        selectorId = selectorId,
    )

fun ConditionData.RouteConditionData.asModel() =
    ConditionModel.RouteConditionModel(
        conditionId = conditionId,
        conditionRule = conditionRule.asModel(),
        pageId = pageId,
        selectorId = selectorId,
        routeId = routeId
    )

fun ConditionModel.ShowSelectorConditionModel.asData() =
    ConditionData.ShowSelectorConditionData(
        conditionId = conditionId,
        conditionRule = conditionRule.asData(),
        pageId = pageId,
        selectorId = selectorId,
    )

fun ConditionModel.RouteConditionModel.asData() =
    ConditionData.RouteConditionData(
        conditionId = conditionId,
        conditionRule = conditionRule.asData(),
        pageId = pageId,
        selectorId = selectorId,
        routeId = routeId
    )


fun ConditionRuleData.asModel(): ConditionRuleModel =
    when (this) {
        is ConditionRuleData.CountConditionRuleData -> {
            ConditionRuleModel.CountConditionRuleModel(
                selfActionId = selfActionId.asModel(),
                relationOp = relationOp,
                logicalOp = logicalOp,
                count = count
            )
        }

        is ConditionRuleData.TargetConditionRuleData -> {
            ConditionRuleModel.TargetConditionRuleModel(
                selfActionId = selfActionId.asModel(),
                relationOp = relationOp,
                logicalOp = logicalOp,
                targetActionId = targetActionId.asModel()
            )
        }
    }

fun ConditionRuleModel.asData(): ConditionRuleData =
    when (this) {
        is ConditionRuleModel.CountConditionRuleModel -> {
            ConditionRuleData.CountConditionRuleData(
                selfActionId = selfActionId.asStorageData(),
                relationOp = relationOp,
                logicalOp = logicalOp,
                count = count
            )
        }

        is ConditionRuleModel.TargetConditionRuleModel -> {
            ConditionRuleData.TargetConditionRuleData(
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