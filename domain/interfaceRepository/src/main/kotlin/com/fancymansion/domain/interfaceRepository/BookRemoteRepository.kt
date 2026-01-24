package com.fancymansion.domain.interfaceRepository

import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.domain.model.book.BookInfoModel
import com.fancymansion.domain.model.book.EpisodeInfoModel


interface BookRemoteRepository {
    suspend fun createBookInfo(episodeRef: EpisodeRef, bookInfo: BookInfoModel, episodeInfo: EpisodeInfoModel)
    suspend fun uploadBookArchive(episodeRef: EpisodeRef)
    suspend fun uploadBookCoverImage(episodeRef: EpisodeRef, coverFileName: String)
}