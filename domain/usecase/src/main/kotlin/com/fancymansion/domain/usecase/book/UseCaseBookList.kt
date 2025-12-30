package com.fancymansion.domain.usecase.book

import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.common.const.getEpisodeId
import com.fancymansion.core.common.const.sampleEpisodeRef
import com.fancymansion.core.common.di.DispatcherIO
import com.fancymansion.domain.interfaceRepository.BookLocalRepository
import com.fancymansion.domain.model.book.BookInfoModel
import com.fancymansion.domain.model.book.EpisodeInfoModel
import com.fancymansion.domain.model.book.LogicModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UseCaseBookList @Inject constructor(
    @DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val bookLocalRepository: BookLocalRepository
) {
    suspend fun addUserEditBook(
        episodeRef: EpisodeRef,
        bookInfo: BookInfoModel,
        episodeInfo: EpisodeInfoModel,
        logic: LogicModel
    ): Boolean =
        withContext(dispatcher) {
            bookLocalRepository.makeBookDir(episodeRef.userId, episodeRef.mode, episodeRef.bookId)
            bookLocalRepository.makeEpisodeDir(episodeRef)

            bookLocalRepository.makeBookInfo(
                episodeRef.userId,
                episodeRef.mode,
                episodeRef.bookId,
                bookInfo
            ) && bookLocalRepository.makeEpisodeInfo(
                episodeRef,
                episodeInfo
            ) && bookLocalRepository.makeLogic(episodeRef, logic)
        }

    suspend fun deleteUserEditBook(userId: String, bookId : String) =
        withContext(dispatcher) {
            bookLocalRepository.deleteBookDir(userId, ReadMode.EDIT, bookId)
        }

    suspend fun getUserEditBookInfoList(userId: String): List<Pair<BookInfoModel, EpisodeInfoModel>> =
        withContext(dispatcher) {
            val result = bookLocalRepository.getUserBookFolderNameList(userId = userId, mode = ReadMode.EDIT).map {
                val bookInfo = bookLocalRepository.loadBookInfo(
                    userId = userId,
                    bookId = it,
                    mode = ReadMode.EDIT
                )
                val episodeInfo = bookLocalRepository.loadEpisodeInfo(
                    EpisodeRef(
                        userId,
                        ReadMode.EDIT,
                        it,
                        getEpisodeId(it)
                    )
                )

                (bookInfo to episodeInfo)
            }
            result
        }

    suspend fun makeSampleEpisode(episodeRef: EpisodeRef = sampleEpisodeRef) =
        withContext(dispatcher) {
            bookLocalRepository.makeSampleEpisode(episodeRef)
        }
}