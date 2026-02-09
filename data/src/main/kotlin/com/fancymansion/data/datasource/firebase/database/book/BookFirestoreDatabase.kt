package com.fancymansion.data.datasource.firebase.database.book

import com.fancymansion.data.datasource.firebase.database.book.model.BookInfoData
import com.fancymansion.data.datasource.firebase.database.book.model.EpisodeInfoData
import com.fancymansion.data.datasource.firebase.database.book.model.HomeBookItemData

interface BookFirestoreDatabase {
    suspend fun getPublishedId(): String
    suspend fun saveBook(publishedId: String, book: BookInfoData)
    suspend fun updateBook(publishedId: String, book: BookInfoData, version: Int)
    suspend fun saveEpisode(publishedId: String, episode: EpisodeInfoData)
    suspend fun updateEpisode(publishedId: String, episode: EpisodeInfoData)
    suspend fun loadBookList(): List<HomeBookItemData>
    suspend fun getPublishedBookVersion(publishedId: String): Int
    suspend fun deleteBookWithEpisodes(publishedId: String)
}