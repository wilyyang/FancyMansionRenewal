package com.fancymansion.domain.model.homeBook

data class EpisodeHomeModel(
    val id: String,
    val bookId: String,
    val title: String,
    val pageCount: Int,
    val version: Long = 0L,
    val createTime: Long = System.currentTimeMillis(),
    val editTime: Long = System.currentTimeMillis(),
)