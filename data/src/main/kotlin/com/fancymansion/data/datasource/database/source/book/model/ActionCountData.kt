package com.fancymansion.data.datasource.database.source.book.model

import androidx.room.Entity

@Entity(
    tableName = "ActionCountData",
    primaryKeys = ["userId", "mode", "bookId", "actionId"]
)
data class ActionCountData(
    val userId: String,
    val mode: String,
    val bookId: String,
    val actionId: Long,
    val count: Int
)

@Entity(
    tableName = "ReadingProgress",
    primaryKeys = ["userId", "mode", "bookId"]
)
data class ReadingProgress(
    val userId: String,
    val mode: String,
    val bookId: String,
    val pageId: Long
)