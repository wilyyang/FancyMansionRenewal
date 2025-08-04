package com.fancymansion.domain.usecase.book

import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.testEpisodeRef
import com.fancymansion.core.common.di.DispatcherIO
import com.fancymansion.domain.interfaceRepository.BookLocalRepository
import com.fancymansion.domain.model.book.BookInfoModel
import com.fancymansion.domain.model.book.EpisodeInfoModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UseCaseBookList @Inject constructor(
    @DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val bookLocalRepository: BookLocalRepository
) {
    suspend fun addUserEditBook(userId: String): Boolean =
        withContext(dispatcher) {
            // TODO Main Tab Editor 08.04
            false
        }

    suspend fun deleteUserEditBook(userId: String, bookId : String): Boolean =
        withContext(dispatcher) {
            // TODO Main Tab Editor 08.04
            false
        }

    suspend fun getUserEditBookInfoList(userId: String): List<Pair<BookInfoModel, EpisodeInfoModel>> =
        withContext(dispatcher) {
            // TODO Main Tab Editor 08.04
            sampleBookInfoList
        }

    suspend fun makeSampleEpisode(episodeRef: EpisodeRef = testEpisodeRef) =
        withContext(dispatcher) {
            bookLocalRepository.makeSampleEpisode(episodeRef)
        }
}