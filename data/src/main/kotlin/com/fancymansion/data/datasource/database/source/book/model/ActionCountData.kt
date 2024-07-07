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
    val actionId: String,
    val count: Int
)