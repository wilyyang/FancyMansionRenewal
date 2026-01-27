package com.fancymansion.domain.interfaceRepository

import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.domain.model.book.BookInfoModel
import com.fancymansion.domain.model.book.EpisodeInfoModel
import com.fancymansion.domain.model.book.HomeBookItemModel


interface BookRemoteRepository {
    suspend fun createBookInfo(episodeRef: EpisodeRef, bookInfo: BookInfoModel, episodeInfo: EpisodeInfoModel): String
    suspend fun uploadBookArchive(episodeRef: EpisodeRef, publishedId: String)
    suspend fun uploadBookCoverImage(episodeRef: EpisodeRef, publishedId: String, coverFileName: String)
    suspend fun getHomeBookItems(): List<HomeBookItemModel>
}