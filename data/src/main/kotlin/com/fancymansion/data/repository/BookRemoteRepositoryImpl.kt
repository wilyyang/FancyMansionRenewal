package com.fancymansion.data.repository

import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.data.datasource.appStorage.book.BookStorageSource
import com.fancymansion.domain.interfaceRepository.BookRemoteRepository
import com.fancymansion.domain.model.book.BookInfoModel
import javax.inject.Inject

class BookRemoteRepositoryImpl @Inject constructor(
    private val bookStorageSource: BookStorageSource
) : BookRemoteRepository {
    override suspend fun createBookInfo(
        episodeRef: EpisodeRef,
        bookInfo: BookInfoModel
    ): Boolean {
        return true
    }

    override suspend fun uploadBookArchive(
        episodeRef: EpisodeRef,
        bookInfo: BookInfoModel
    ): Boolean {
        return true
    }

    override suspend fun uploadBookCoverImage(
        episodeRef: EpisodeRef,
        bookInfo: BookInfoModel
    ): Boolean {
        return true
    }
}