package com.fancymansion.data.repository

import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.data.datasource.appStorage.book.BookStorageSource
import com.fancymansion.data.datasource.firebase.database.book.BookFirestoreDatabase
import com.fancymansion.data.datasource.firebase.database.book.model.asData
import com.fancymansion.domain.interfaceRepository.BookRemoteRepository
import com.fancymansion.domain.model.book.BookInfoModel
import com.fancymansion.domain.model.book.EpisodeInfoModel
import javax.inject.Inject

class BookRemoteRepositoryImpl @Inject constructor(
    private val bookStorageSource: BookStorageSource,
    private val bookFirestoreDatabase: BookFirestoreDatabase
) : BookRemoteRepository {

    override suspend fun createBookInfo(
        episodeRef: EpisodeRef,
        bookInfo: BookInfoModel,
        episodeInfo: EpisodeInfoModel
    ) {
        bookFirestoreDatabase.saveBook(bookInfo.asData())
        bookFirestoreDatabase.saveEpisode(episodeRef.bookId, episodeInfo.asData())
    }

    override suspend fun uploadBookArchive(episodeRef: EpisodeRef) {
    }

    override suspend fun uploadBookCoverImage(
        episodeRef: EpisodeRef,
        coverFileName: String
    ) {
    }
}