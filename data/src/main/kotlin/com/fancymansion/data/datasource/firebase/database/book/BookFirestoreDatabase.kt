package com.fancymansion.data.datasource.firebase.database.book

import com.fancymansion.core.common.const.RemoteBookSortOrder
import com.fancymansion.data.datasource.firebase.database.book.model.BookInfoData
import com.fancymansion.data.datasource.firebase.database.book.model.EpisodeInfoData
import com.fancymansion.data.datasource.firebase.database.book.model.HomeBookItemData
import com.fancymansion.data.datasource.firebase.database.book.model.result.BookQueryDataResult
import com.fancymansion.data.datasource.firebase.database.book.model.result.LoadBookDataResult
import com.fancymansion.data.datasource.firebase.database.book.model.result.NextBookIdDataResult

interface BookFirestoreDatabase {
    suspend fun getPublishedId(): String
    suspend fun saveBook(publishedId: String, book: BookInfoData)
    suspend fun updateBook(publishedId: String, book: BookInfoData)
    suspend fun saveEpisode(publishedId: String, episode: EpisodeInfoData)
    suspend fun updateEpisode(publishedId: String, episode: EpisodeInfoData)
    suspend fun loadBookListWithQuery(searchText: String, sortOrder: RemoteBookSortOrder, cursorBookId: String?, limit: Int): BookQueryDataResult
    suspend fun getNextBookIds(searchText: String, sortOrder: RemoteBookSortOrder, beforeBookId: String, limit: Int): NextBookIdDataResult
    suspend fun loadBookList(): List<HomeBookItemData>
    suspend fun loadSelectedBookList(bookIds: List<String>): List<HomeBookItemData>
    suspend fun loadSelectedBook(bookId: String): LoadBookDataResult
    suspend fun getPublishedBookVersion(publishedId: String): Int
    suspend fun withdrawBookWithEpisodes(publishedId: String)
}