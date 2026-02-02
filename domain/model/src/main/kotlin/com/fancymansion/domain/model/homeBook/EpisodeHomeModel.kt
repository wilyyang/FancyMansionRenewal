package com.fancymansion.domain.model.homeBook

import com.fancymansion.core.common.const.ReadMode

data class EpisodeHomeModel(
    val id: String,
    val bookId: String,
    val title: String,
    val pageCount: Int,
    val version: Long = 0L,
    val createTime: Long = System.currentTimeMillis(),
    val editTime: Long = System.currentTimeMillis(),
    val updateTime: Long = 0L,
    val readMode: ReadMode = ReadMode.EDIT
)