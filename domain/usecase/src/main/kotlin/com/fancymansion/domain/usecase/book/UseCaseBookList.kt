package com.fancymansion.domain.usecase.book

import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.common.const.getEpisodeId
import com.fancymansion.core.common.di.DispatcherIO
import com.fancymansion.domain.interfaceRepository.BookLocalRepository
import com.fancymansion.domain.model.book.BookInfoModel
import com.fancymansion.domain.model.book.EditorModel
import com.fancymansion.domain.model.book.EpisodeInfoModel
import com.fancymansion.domain.model.book.LogicModel
import com.fancymansion.domain.model.book.PageModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UseCaseBookList @Inject constructor(
    @param:DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val bookLocalRepository: BookLocalRepository
) {
    suspend fun addUserEditBook(
        episodeRef: EpisodeRef,
        bookInfo: BookInfoModel,
        episodeInfo: EpisodeInfoModel,
        logic: LogicModel,
        startPage: PageModel
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
            ) && bookLocalRepository.makeLogic(
                episodeRef, logic
            ) && bookLocalRepository.makePage(episodeRef, startPage.id, startPage)
        }

    suspend fun deleteUserEditBook(userId: String, bookId : String) =
        withContext(dispatcher) {
            bookLocalRepository.deleteBookDir(userId, ReadMode.EDIT, bookId)
        }

    suspend fun getLocalBookInfoList(userId: String, readMode: ReadMode): List<Pair<BookInfoModel, EpisodeInfoModel>> =
        withContext(dispatcher) {
            val result = bookLocalRepository.getUserBookFolderNameList(userId = userId, mode = readMode).map {
                val bookInfo = bookLocalRepository.loadBookInfo(
                    userId = userId,
                    bookId = it,
                    mode = readMode
                )
                val episodeInfo = bookLocalRepository.loadEpisodeInfo(
                    EpisodeRef(
                        userId,
                        readMode,
                        it,
                        getEpisodeId(it)
                    )
                )

                (bookInfo to episodeInfo)
            }
            result
        }

    suspend fun makeSampleEpisode(episodeRef: EpisodeRef, editorModel: EditorModel) =
        withContext(dispatcher) {
            bookLocalRepository.makeSampleEpisode(episodeRef, editorModel)
        }
}