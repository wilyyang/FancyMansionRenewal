package com.fancymansion.data.datasource.database.source.book.model

import androidx.room.Entity

@Entity(
    tableName = "ReadingProgress",
    primaryKeys = ["userId", "mode", "bookId", "episodeId"]
)
data class ReadingProgressData(
    val userId: String,
    val mode: String,
    val bookId: String,
    val episodeId: String,
    val pageId: Long
)