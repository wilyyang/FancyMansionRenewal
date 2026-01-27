package com.fancymansion.data.datasource.firebase.database.book.model

import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.domain.model.book.HomeBookItemModel
import com.fancymansion.domain.model.book.keywordMap

data class HomeBookItemData(
    val book: BookInfoData,
    val episode: EpisodeInfoData
)

fun HomeBookItemData.asModel() = HomeBookItemModel(
    book = book.asModel(keywordMap),
    episode = episode.asModel(ReadMode.READ)
)