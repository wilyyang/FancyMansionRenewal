package com.fancymansion.domain.usecase.publishBook

import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.di.DispatcherIO
import com.fancymansion.domain.interfaceRepository.PublishBookRepository
import com.fancymansion.domain.model.book.BookInfoModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UseCaseUploadBook @Inject constructor(
    @param:DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val publishBookRepository: PublishBookRepository
) {

    suspend operator fun invoke(episodeRef: EpisodeRef, bookInfo: BookInfoModel): Boolean =
        withContext(dispatcher) {
            publishBookRepository.createBookInfo(
                episodeRef,
                bookInfo
            ) && publishBookRepository.uploadBookArchive(
                episodeRef,
                bookInfo
            ) && publishBookRepository.uploadBookCoverImage(episodeRef, bookInfo)
        }
}