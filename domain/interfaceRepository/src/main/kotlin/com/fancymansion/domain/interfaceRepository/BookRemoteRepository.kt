package com.fancymansion.domain.interfaceRepository

import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.domain.model.book.BookInfoModel


interface BookRemoteRepository {
    suspend fun createBookInfo(episodeRef: EpisodeRef, bookInfo: BookInfoModel): Boolean
    suspend fun uploadBookArchive(episodeRef: EpisodeRef, bookInfo: BookInfoModel): Boolean
    suspend fun uploadBookCoverImage(episodeRef: EpisodeRef, bookInfo: BookInfoModel): Boolean
}