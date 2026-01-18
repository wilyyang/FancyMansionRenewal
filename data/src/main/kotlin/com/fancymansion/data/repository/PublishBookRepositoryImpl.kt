package com.fancymansion.data.repository

import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.data.datasource.appStorage.book.BookStorageSource
import com.fancymansion.domain.interfaceRepository.PublishBookRepository
import com.fancymansion.domain.model.book.BookInfoModel
import javax.inject.Inject

class PublishBookRepositoryImpl @Inject constructor(
    private val bookStorageSource: BookStorageSource
) : PublishBookRepository {
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