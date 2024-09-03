package com.fancymansion.data.datasource.database.book.model

import androidx.room.Entity
import com.fancymansion.core.common.const.ACTION_ID_NOT_ASSIGNED

@Entity(
    tableName = "ActionCountData",
    primaryKeys = ["userId", "mode", "bookId", "episodeId", "pageId", "selectorId", "routeId"]
)
data class ActionCountData(
    val userId: String,
    val mode: String,
    val bookId: String,
    val episodeId: String,
    val pageId: Long = ACTION_ID_NOT_ASSIGNED,
    val selectorId: Long = ACTION_ID_NOT_ASSIGNED,
    val routeId: Long = ACTION_ID_NOT_ASSIGNED,
    val count: Int
)