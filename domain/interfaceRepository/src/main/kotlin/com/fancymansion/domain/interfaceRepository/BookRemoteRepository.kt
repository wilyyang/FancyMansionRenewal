package com.fancymansion.domain.interfaceRepository

import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.common.const.RemoteBookSortOrder
import com.fancymansion.domain.model.book.BookInfoModel
import com.fancymansion.domain.model.book.EpisodeInfoModel
import com.fancymansion.domain.model.homeBook.HomeBookItemModel
import com.fancymansion.domain.model.homeBook.result.BookQueryResult
import com.fancymansion.domain.model.homeBook.result.LoadBookResult
import java.io.File

interface BookRemoteRepository {
    suspend fun getPublishedId(): String
    suspend fun createBookInfo(publishedId: String, bookInfo: BookInfoModel, episodeInfo: EpisodeInfoModel)
    suspend fun updateBookInfo(publishedId: String, bookInfo: BookInfoModel, episodeInfo: EpisodeInfoModel, version: Int)
    suspend fun uploadBookArchive(publishedId: String, version: Int, episodeRef: EpisodeRef)
    suspend fun uploadBookCoverImage(publishedId: String, episodeRef: EpisodeRef, coverFileName: String)
    suspend fun getHomeBookItemsWithQuery(searchText: String, sortOrder: RemoteBookSortOrder, cursorBookId: String?, limit: Long): BookQueryResult
    suspend fun getHomeBookItems(): List<HomeBookItemModel>
    suspend fun getSelectedHomeBookItems(bookIds: List<String>): List<HomeBookItemModel>
    suspend fun getSelectedHomeBookItem(bookId: String): LoadBookResult
    suspend fun downloadBookArchive(userId: String, version: Int, publishedId: String, readMode: ReadMode): File
    suspend fun getBookCoverImageUrl(publishedId: String, imageFileName: String): String
    suspend fun getPublishedBookVersion(publishedId: String): Int
    suspend fun withdrawBookWithEpisodes(publishedId: String)
    suspend fun deleteBookStorageByPublishedId(publishedId: String)
    suspend fun pruneEpisodeZipFile(publishedId: String, currentVersion: Int)
}