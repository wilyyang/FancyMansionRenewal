package com.fancymansion.data.datasource.database.source.book.model

import androidx.room.Entity
import com.fancymansion.core.common.const.ACTION_ID_NOT_ASSIGNED
import com.fancymansion.domain.model.book.ActionIdModel

@Entity(
    tableName = "ActionCountData",
    primaryKeys = ["userId", "mode", "bookId", "episodeId", "actionId"]
)
data class ActionCountData(
    val userId: String,
    val mode: String,
    val bookId: String,
    val episodeId: String,
    val actionId: ActionIdData,
    val count: Int
)

@Entity(
    tableName = "ActionIdData",
    primaryKeys = ["pageId", "selectorId", "routeId"]
)
data class ActionIdData(
    val pageId: Long = ACTION_ID_NOT_ASSIGNED,
    val selectorId: Long = ACTION_ID_NOT_ASSIGNED,
    val routeId: Long = ACTION_ID_NOT_ASSIGNED
)

fun ActionIdData.asModel() = ActionIdModel(
    pageId = pageId,
    selectorId = selectorId,
    routeId = routeId
)

fun ActionIdModel.asDatabaseData() = ActionIdData(
    pageId = pageId,
    selectorId = selectorId,
    routeId = routeId
)