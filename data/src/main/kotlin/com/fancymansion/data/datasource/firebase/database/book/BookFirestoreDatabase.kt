package com.fancymansion.data.datasource.firebase.database.book

import com.fancymansion.data.datasource.firebase.database.book.model.BookInfoData
import com.fancymansion.data.datasource.firebase.database.book.model.EpisodeInfoData

interface BookFirestoreDatabase {
    suspend fun saveBook(book: BookInfoData): String
    suspend fun saveEpisode(publishedId: String, episode: EpisodeInfoData)
}