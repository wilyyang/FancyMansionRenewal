package com.fancymansion.domain.interfaceRepository

import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.domain.model.book.BookInfoModel
import com.fancymansion.domain.model.book.EpisodeInfoModel
import com.fancymansion.domain.model.homeBook.HomeBookItemModel
import java.io.File

interface BookRemoteRepository {
    suspend fun getPublishedId(): String
    suspend fun createBookInfo(publishedId: String, bookInfo: BookInfoModel, episodeInfo: EpisodeInfoModel)
    suspend fun uploadBookArchive(publishedId: String, version: Int, episodeRef: EpisodeRef)
    suspend fun uploadBookCoverImage(publishedId: String, episodeRef: EpisodeRef, coverFileName: String)
    suspend fun getHomeBookItems(): List<HomeBookItemModel>
    suspend fun downloadBookArchive(userId: String, version: Int, publishedId: String): File
    suspend fun getBookCoverImageUrl(publishedId: String, imageFileName: String): String
    suspend fun getPublishedBookVersion(publishedId: String): Int
    suspend fun deleteBookWithEpisodes(publishedId: String)
    suspend fun deleteBookStorageByPublishedId(publishedId: String)
}