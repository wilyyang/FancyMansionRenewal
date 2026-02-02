package com.fancymansion.data.datasource.firebase.database.book.model

import com.fancymansion.domain.model.homeBook.HomeBookItemModel
import com.fancymansion.domain.model.book.keywordMap

data class HomeBookItemData(
    val book: BookInfoData,
    val episode: EpisodeInfoData
)

fun HomeBookItemData.asHomeModel() = HomeBookItemModel(
    book = book.asHomeModel(keywordMap),
    episode = episode.asHomeModel()
)